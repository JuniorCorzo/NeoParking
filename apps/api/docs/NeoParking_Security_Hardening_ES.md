Versión: 1.0  
Fecha: 2026-02-03  
Autor: Equipo de Análisis de Seguridad  
Alcance: Endurecimiento de seguridad del sistema de pagos NeoParking  

---

## 📊 Resumen ejecutivo
Este documento describe vulnerabilidades críticas de seguridad identificadas en el sistema de pagos NeoParking y propone soluciones integrales para mitigarlas. El análisis identifica 10 fallas de seguridad, desde problemas de integridad transaccional hasta brechas de protección de datos.

## 🎯 Matriz de riesgos
| Vulnerabilidad | Criticidad | Impacto | Explotabilidad | Prioridad |
|---|---|---|---|---|
| Condiciones de carrera en ConfirmPaymentUseCase | CRÍTICA | Alta | Fácil | 1 |
| Falta de idempotencia en transacciones | CRÍTICA | Alta | Fácil | 2 |
| Validación insuficiente de webhooks | CRÍTICA | Alta | Media | 3 |
| Pago doble por concurrencia | ALTA | Alta | Media | 4 |
| Inconsistencia entre estado de ticket y pago | ALTA | Media | Media | 5 |
| Exposición de PII en logs | MEDIA | Media | Difícil | 6 |
| Manejo inseguro de excepciones | MEDIA | Media | Media | 7 |
| Falta de rate limiting | MEDIA | Media | Fácil | 8 |
| Estados REVERSED no manejados | BAJA | Baja | Difícil | 9 |
| Falta de rollback automático | BAJA | Baja | Difícil | 10 |

## 📅 Cronograma de implementación
Fase 1 (Semanas 1-2): Vulnerabilidades críticas  
Fase 2 (Semanas 3-4): Integridad transaccional  
Fase 3 (Semanas 5-6): Protección avanzada

---

## 🔍 Análisis detallado de vulnerabilidades

### 1. CRÍTICA: Condiciones de carrera en ConfirmPaymentUseCase
Ubicación: ConfirmPaymentUseCase.java:21-26
```java
// VULNERABLE CODE
public Payments execute(Map<String, String> receipt, String transactionId) {
    final Payments previousPayment = this.paymentsRepository
        .findByTransactionId(transactionId)
        .orElseThrow(() -> new PaymentNotFound(transactionId));
    if (previousPayment.getStatus() != PaymentStatus.PENDING) return previousPayment;
    // RACE CONDITION WINDOW
    final Payments currentPayment = this.paymentProviderGateway.confirmationPay(previousPayment, receipt);
    return this.paymentsRepository.save(currentPayment);
}
```
Vector de ataque:
- Dos confirmaciones de webhook concurrentes para la misma transacción
- Ambos hilos leen el estado PENDING al mismo tiempo
- Ambos continúan a actualizar el estado del pago
- Resultado: el pago se procesa dos veces o queda un estado inconsistente

Impacto: Pérdida financiera, inconsistencias contables, disputas con clientes

Solución: Transacción atómica con bloqueo a nivel de fila (row-level locking)

---

### 2. CRÍTICA: Falta de idempotencia en transacciones
Ubicación: PaymentController.java:21-26
```java
// VULNERABLE CODE
@PostMapping(value = "confirmation", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public ResponseEntity<Object> confirm(@RequestParam MultiValueMap<String, String> form) {
    final String transactionId = form.getFirst("x_transaction_id");
    this.confirmPaymentUseCase.execute(form.toSingleValueMap(), transactionId);
    return ResponseEntity.ok().build();
}
```
Vector de ataque:
- Reintentos de red del mismo webhook
- Procesamiento duplicado de la misma transacción
- Múltiples actualizaciones al estado del pago

Solución: Mecanismo de idempotency key con deduplicación (esto es una práctica recomendada para callbacks/webhooks en integraciones de pasarela de pago). [web:6]

---

### 3. CRÍTICA: Validación insuficiente de webhooks
Ubicación: EpaycoSignature.java:12-25
```java
// VULNERABLE CODE
public static void validatedSignature(EpaycoConfirmationDTO confirmation, String publicKey) {
    // Only signature validation - no timestamp, no replay protection
    final String signatureRaw = String.join("^", /* fields */);
    final String signature = EpaycoSignature.sha256Hex(signatureRaw);
    if (!confirmation.signature().equalsIgnoreCase(signature)) throw new SignatureInvalid();
}
```
Vector de ataque:
- Ataques de replay con firmas válidas
- No hay validación de timestamp
- No hay verificación de nonce

Solución: Agregar timestamp y protección contra replay; OWASP recomienda usar un ID de transacción único con vencimiento y rechazar callbacks reutilizados/expirados. [web:6]

---

### 4. ALTA: Pago doble por concurrencia
Ubicación: V10__create_payments_table.sql:5
```sql
-- VULNERABLE DESIGN
CREATE TABLE IF NOT EXISTS payments (
    -- ...
    parking_ticket_id UUID UNIQUE NOT NULL,  -- Only applies to entire table
    -- ...
);
```
Vector de ataque:
- Creación concurrente de pagos para el mismo ticket
- Un pago PENDING y otro FAILED
- Condición de carrera en la validación de la restricción única

Solución: Restricción única condicional por estado (por ejemplo, índice único parcial para pagos completados)

---

### 5. ALTA: Inconsistencia entre estado de ticket y pago
Ubicación: PayLinkPaymentStrategy.java:48-50
```java
// VULNERABLE CODE
private void updateTicketStatus(UUID ticketId, BigDecimal amountToCharge) {
    this.parkingTicketsRepository.prepareCheckout(ticketId, amountToCharge);
}
```
Vector de ataque:
- Ticket actualizado a CHECKOUT_PREPARED
- Falla la creación del pago
- El ticket queda en estado inconsistente
- No existe mecanismo de rollback

Solución: Patrón saga transaccional con compensación

---

## 🛡️ Soluciones integrales de seguridad

### 3.1 PATRONES DE TRANSACCIÓN SEGURA

**Solución 1: Transacción atómica con bloqueo a nivel de fila**
```java
@Transactional(isolation = Isolation.SERIALIZABLE)
public Payments execute(Map<String, String> receipt, String transactionId) {
    // Acquire pessimistic lock to prevent concurrent modifications
    final Payments previousPayment = this.paymentsRepository
        .findByTransactionIdForUpdate(transactionId)
        .orElseThrow(() -> new PaymentNotFound(transactionId));

    if (previousPayment.getStatus() != PaymentStatus.PENDING) {
        return previousPayment; // Early return for non-pending payments
    }
    // Validate idempotency key to prevent duplicate processing
    final String idempotencyKey = receipt.get("x_idempotency_key");
    if (this.paymentsRepository.existsByIdempotencyKey(idempotencyKey)) {
        return previousPayment; // Return existing payment for duplicate
    }
    final Payments currentPayment = this.paymentProviderGateway
        .confirmationPay(previousPayment, receipt);

    // Store idempotency key for future duplicates
    currentPayment.setIdempotencyKey(idempotencyKey);

    return this.paymentsRepository.save(currentPayment);
}
```

**Solución 2: Repositorio de pagos mejorado con locking**
```java
@Repository
public interface PaymentsRepository extends JpaRepository<Payments, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Payments p WHERE p.providerMetadata.externalId.transactionId = :transactionId")
    Optional<Payments> findByTransactionIdForUpdate(String transactionId);

    boolean existsByIdempotencyKey(String idempotencyKey);

    // Conditional unique constraint support
    @Modifying
    @Query("UPDATE Payments p SET p.status = :status WHERE p.parkingTicket.id = :ticketId AND p.status = 'PENDING'")
    int updatePaymentStatusForTicket(@Param("ticketId") UUID ticketId, @Param("status") PaymentStatus status);
}
```

**Solución 3: Mejora del esquema de base de datos**
```sql
-- Enhanced payments table with security constraints
CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    user_id UUID NOT NULL REFERENCES users(id),
    parking_ticket_id UUID NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,

    payment_date TIMESTAMP WITH TIME ZONE,
    payment_method VARCHAR(50) NOT NULL CHECK(payment_method IN ('EFFECTIVE', 'PAY_LINK')),
    status VARCHAR(20) DEFAULT 'PAID' CHECK (status IN ('PENDING', 'PAID', 'FAILED', 'CANCELED', 'REFUNDED')),
    provider_metadata JSONB CHECK (jsonb_typeof(provider_metadata) = 'object'),

    -- Security enhancements
    idempotency_key VARCHAR(255) UNIQUE,
    processed_at TIMESTAMP WITH TIME ZONE,
    retry_count INTEGER DEFAULT 0,
    last_retry_at TIMESTAMP WITH TIME ZONE,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Version for optimistic locking
    version BIGINT DEFAULT 0,

    CONSTRAINT fk_payments_ticket_tenant FOREIGN KEY(tenant_id, parking_ticket_id) 
        REFERENCES parking_tickets(tenant_id, id),

    -- Conditional unique constraint: only one COMPLETED payment per ticket
    CONSTRAINT ux_payments_one_completed_per_ticket 
        UNIQUE (tenant_id, parking_ticket_id) 
        DEFERRABLE INITIALLY DEFERRED
);
-- Partial unique index for completed payments
CREATE UNIQUE INDEX ux_payments_completed_per_ticket 
ON payments(tenant_id, parking_ticket_id) 
WHERE status = 'PAID';
-- Index for idempotency lookup
CREATE INDEX idx_payments_idempotency_key ON payments(idempotency_key);
-- Index for transaction locking
CREATE INDEX idx_payments_transaction_id 
ON payments((provider_metadata->>'externalId'->>'transactionId'));
```

---

## 3.2 ENDURECIMIENTO DE ENDPOINTS

**Solución 4: Validación mejorada de webhooks con protección contra replay**
```java
@Component
public class EnhancedWebhookValidator {

    private static final long MAX_TIMESTAMP_AGE_MINUTES = 5;
    private final RedisTemplate<String, String> redisTemplate;

    public void validateWebhook(EpaycoConfirmationDTO confirmation, String publicKey) {
        // 1. Signature validation
        validateSignature(confirmation, publicKey);

        // 2. Timestamp validation
        validateTimestamp(confirmation);

        // 3. Replay attack protection
        validateReplayProtection(confirmation);

        // 4. Amount validation
        validateAmountConsistency(confirmation);
    }

    private void validateTimestamp(EpaycoConfirmationDTO confirmation) {
        // Add timestamp field to confirmation if not present
        if (confirmation.timestamp() == null) {
            throw new InvalidWebhookException("Missing timestamp");
        }

        long webhookTime = confirmation.timestamp().toEpochSecond();
        long currentTime = System.currentTimeMillis() / 1000;
        long ageMinutes = (currentTime - webhookTime) / 60;

        if (ageMinutes > MAX_TIMESTAMP_AGE_MINUTES) {
            throw new InvalidWebhookException("Webhook too old: " + ageMinutes + " minutes");
        }
    }

    private void validateReplayProtection(EpaycoConfirmationDTO confirmation) {
        String replayKey = "webhook:replay:" + confirmation.refPayco();

        if (Boolean.TRUE.equals(redisTemplate.hasKey(replayKey))) {
            throw new InvalidWebhookException("Duplicate webhook detected");
        }

        // Store replay key with expiration
        redisTemplate.opsForValue().set(replayKey, "processed", Duration.ofHours(24));
    }

    private void validateAmountConsistency(EpaycoConfirmationDTO confirmation) {
        // Validate amount against expected amount for the transaction
        BigDecimal expectedAmount = getExpectedAmountForTransaction(confirmation.refPayco());

        if (expectedAmount != null && !expectedAmount.equals(confirmation.amount())) {
            throw new InvalidWebhookException("Amount mismatch");
        }
    }
}
```

**Solución 5: Rate limiting y lista blanca de IPs**
```java
@RestController()
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final RateLimiter rateLimiter;
    private final WebhookIpValidator ipValidator;

    @PostMapping(value = "confirmation", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @RateLimited(requests = 10, window = "1m") // 10 requests per minute
    public ResponseEntity<Object> confirm(
            @RequestParam MultiValueMap<String, String> form,
            HttpServletRequest request) {

        // IP whitelisting for payment provider
        if (!ipValidator.isValidProviderIp(request.getRemoteAddr())) {
            throw new UnauthorizedIpException(request.getRemoteAddr());
        }

        // Apply rate limiting
        if (!rateLimiter.tryAcquire("webhook:" + request.getRemoteAddr())) {
            throw new RateLimitExceededException();
        }

        final String transactionId = form.getFirst("x_transaction_id");
        final String idempotencyKey = form.getFirst("x_idempotency_key");

        this.confirmPaymentUseCase.execute(form.toSingleValueMap(), transactionId, idempotencyKey);

        return ResponseEntity.ok().build();
    }
}
@Component
public class WebhookIpValidator {

    @Value("${payment.provider.allowed-ips}")
    private List<String> allowedIps;

    public boolean isValidProviderIp(String clientIp) {
        return allowedIps.contains(clientIp) || 
               allowedIps.stream().anyMatch(ip -> isIpInRange(clientIp, ip));
    }

    private boolean isIpInRange(String clientIp, String range) {
        // CIDR range validation implementation
        // ... implementation details
    }
}
```

---

## 3.3 MANEJO SEGURO DE EXCEPCIONES

**Solución 6: Manejador global de excepciones con sanitización**
```java
@RestControllerAdvice
@Slf4j
public class PaymentExceptionHandler {

    @ExceptionHandler(PaymentNotFound.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFound ex) {
        log.warn("Payment not found: {}", ex.getTransactionId());

        ErrorResponse error = ErrorResponse.builder()
            .code("PAYMENT_NOT_FOUND")
            .message("Payment transaction not found")
            .timestamp(Instant.now())
            .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SignatureInvalid.class)
    public ResponseEntity<ErrorResponse> handleInvalidSignature(SignatureInvalid ex) {
        log.error("Invalid webhook signature detected");

        ErrorResponse error = ErrorResponse.builder()
            .code("INVALID_SIGNATURE")
            .message("Request signature validation failed")
            .timestamp(Instant.now())
            .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(InvalidWebhookException.class)
    public ResponseEntity<ErrorResponse> handleInvalidWebhook(InvalidWebhookException ex) {
        log.error("Invalid webhook: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
            .code("INVALID_WEBHOOK")
            .message("Webhook validation failed")
            .timestamp(Instant.now())
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error in payment processing", ex);

        // Never expose stack traces or internal details
        ErrorResponse error = ErrorResponse.builder()
            .code("INTERNAL_ERROR")
            .message("An error occurred while processing your request")
            .timestamp(Instant.now())
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
@Data
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private Instant timestamp;
}
```

**Solución 7: Logging seguro sin PII**
```java
@Component
public class PaymentAuditLogger {

    private final ObjectMapper objectMapper;
    private final SensitiveDataMasker dataMasker;

    public void logPaymentConfirmation(String transactionId, PaymentStatus oldStatus, PaymentStatus newStatus) {
        PaymentAuditEvent event = PaymentAuditEvent.builder()
            .transactionId(maskTransactionId(transactionId))
            .eventType("PAYMENT_STATUS_CHANGE")
            .oldStatus(oldStatus.toString())
            .newStatus(newStatus.toString())
            .timestamp(Instant.now())
            .build();

        log.info("PAYMENT_AUDIT: {}", maskSensitiveData(event));
    }

    public void logWebhookReceived(EpaycoConfirmationDTO confirmation) {
        WebhookAuditEvent event = WebhookAuditEvent.builder()
            .refPayco(maskRefPayco(confirmation.refPayco()))
            .transactionState(confirmation.transactionState())
            .amount(confirmation.amount())
            .timestamp(Instant.now())
            .build();

        log.info("WEBHOOK_RECEIVED: {}", maskSensitiveData(event));
    }

    private String maskTransactionId(String transactionId) {
        if (transactionId == null || transactionId.length() < 8) return "***";
        return transactionId.substring(0, 4) + "***" + transactionId.substring(transactionId.length() - 4);
    }

    private String maskRefPayco(String refPayco) {
        return maskTransactionId(refPayco);
    }

    private String maskSensitiveData(Object event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            return dataMasker.mask(json);
        } catch (Exception e) {
            log.warn("Failed to mask audit event", e);
            return "***";
        }
    }
}
```

---

## 3.4 PROTECCIÓN DE DATOS

**Solución 8: Metadatos del proveedor cifrados**
```java
@Component
public class SecureMetadataHandler {

    private final AES256Encryptor encryptor;

    public ProviderMetadata encryptMetadata(ProviderMetadata metadata) {
        try {
            String encryptedCreate = encryptor.encrypt(objectMapper.writeValueAsString(metadata.create()));
            String encryptedConfirmation = encryptor.encrypt(objectMapper.writeValueAsString(metadata.confirmation()));

            return metadata.toBuilder()
                .create(encryptedCreate)
                .confirmation(encryptedConfirmation)
                .build();
        } catch (Exception e) {
            throw new MetadataEncryptionException("Failed to encrypt provider metadata", e);
        }
    }

    public ProviderMetadata decryptMetadata(ProviderMetadata encryptedMetadata) {
        try {
            Object decryptedCreate = objectMapper.readValue(
                encryptor.decrypt(encryptedMetadata.create().toString()), Object.class);
            Object decryptedConfirmation = objectMapper.readValue(
                encryptor.decrypt(encryptedMetadata.confirmation().toString()), Object.class);

            return encryptedMetadata.toBuilder()
                .create(decryptedCreate)
                .confirmation(decryptedConfirmation)
                .build();
        } catch (Exception e) {
            throw new MetadataDecryptionException("Failed to decrypt provider metadata", e);
        }
    }
}
@Component
public class AES256Encryptor {

    @Value("${encryption.key}")
    private String encryptionKey;

    public String encrypt(String plaintext) {
        // AES-256/GCM encryption implementation
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, generateIV());

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

            return Base64.getEncoder().encodeToString(ciphertext);
        } catch (Exception e) {
            throw new EncryptionException("Encryption failed", e);
        }
    }

    public String decrypt(String ciphertext) {
        // AES-256/GCM decryption implementation
        // ... implementation details
    }
}
```

---

## 3.5 PATRONES DE COMPENSACIÓN

**Solución 9: Patrón saga para el flujo de pagos**
```java
@Component
public class PaymentSagaOrchestrator {

    @SagaOrchestration(start = true)
    public PaymentSagaData initiatePayment(PaymentCommand command) {
        PaymentSagaData sagaData = PaymentSagaData.builder()
            .paymentId(UUID.randomUUID())
            .ticketId(command.ticket().getId())
            .amount(command.amounts().getTotal())
            .status(SagaStatus.STARTED)
            .build();

        // Step 1: Prepare ticket for checkout
        sagaManager.executeStep("PREPARE_TICKET", () -> 
            parkingTicketsRepository.prepareCheckout(command.ticket().getId(), command.amounts().getTotal()));

        // Step 2: Create payment intent
        sagaManager.executeStep("CREATE_PAYMENT", () -> 
            paymentsRepository.save(createPaymentIntent(command)));

        // Step 3: Process payment with provider
        sagaManager.executeStep("PROCESS_PAYMENT", () -> 
            paymentProviderGateway.processPayment(command.ticket(), command.amounts().getTotal(), command.checkOut()));

        return sagaData;
    }

    @SagaCompensation("PREPARE_TICKET")
    public void compensateTicketPreparation(PaymentSagaData sagaData) {
        parkingTicketsRepository.rollbackCheckout(sagaData.getTicketId());
    }

    @SagaCompensation("CREATE_PAYMENT")
    public void compensatePaymentCreation(PaymentSagaData sagaData) {
        paymentsRepository.markAsFailed(sagaData.getPaymentId(), "Saga compensation");
    }

    @SagaCompensation("PROCESS_PAYMENT")
    public void compensatePaymentProcessing(PaymentSagaData sagaData) {
        paymentProviderGateway.cancelPayment(sagaData.getPaymentId());
    }
}
```

**Solución 10: Consistencia de pagos orientada a eventos**
```java
@Component
public class PaymentEventHandler {

    @EventListener
    @Transactional
    public void handlePaymentProcessed(PaymentProcessedEvent event) {
        // Mark payment as completed
        paymentsRepository.updateStatus(event.getPaymentId(), PaymentStatus.PAID);

        // Update ticket status to COMPLETED
        parkingTicketsRepository.completeCheckout(event.getTicketId());

        // Emit completion event
        eventPublisher.publishEvent(new PaymentCompletedEvent(event.getPaymentId(), event.getTicketId()));
    }

    @EventListener
    @Transactional
    public void handlePaymentFailed(PaymentFailedEvent event) {
        // Mark payment as failed
        paymentsRepository.updateStatus(event.getPaymentId(), PaymentStatus.FAILED);

        // Rollback ticket to ACTIVE state
        parkingTicketsRepository.rollbackCheckout(event.getTicketId());

        // Emit failure event for notification
        eventPublisher.publishEvent(new PaymentFailureEvent(event.getPaymentId(), event.getTicketId(), event.getReason()));
    }

    @EventListener
    @Transactional
    public void handlePaymentReversed(PaymentReversedEvent event) {
        // Mark payment as refunded
        paymentsRepository.updateStatus(event.getPaymentId(), PaymentStatus.REFUNDED);

        // Update ticket status appropriately
        parkingTicketsRepository.handleRefund(event.getTicketId());

        // Emit refund event
        eventPublisher.publishEvent(new PaymentRefundEvent(event.getPaymentId(), event.getTicketId()));
    }
}
```

---

## 🚀 Guía de implementación

### Fase 1: Seguridad crítica (Semanas 1-2)
Semana 1: Integridad transaccional
- [ ] Implementar transacciones atómicas con bloqueo a nivel de fila
- [ ] Agregar mecanismo de idempotency key
- [ ] Mejorar el esquema de base de datos con restricciones de seguridad
- [ ] Desplegar migraciones de base de datos

Semana 2: Seguridad de webhooks
- [ ] Implementar validación mejorada de webhooks
- [ ] Agregar timestamp y protección contra replay
- [ ] Implementar rate limiting y lista blanca de IPs
- [ ] Desplegar monitoreo de seguridad

### Fase 2: Protección de datos (Semanas 3-4)
Semana 3: Manejo de excepciones
- [ ] Implementar manejador global de excepciones
- [ ] Agregar logging seguro sin PII
- [ ] Implementar enmascaramiento de datos
- [ ] Desplegar auditoría de logs

Semana 4: Protección de metadatos
- [ ] Implementar cifrado AES-256 para metadatos
- [ ] Agregar gestión segura de llaves
- [ ] Implementar cifrado de datos en reposo
- [ ] Desplegar pipeline de cifrado

### Fase 3: Patrones avanzados (Semanas 5-6)
Semana 5: Implementación de saga
- [ ] Implementar orquestación de saga
- [ ] Agregar transacciones de compensación
- [ ] Implementar consistencia orientada a eventos
- [ ] Desplegar infraestructura de saga

Semana 6: Monitoreo y pruebas
- [ ] Implementar monitoreo de seguridad
- [ ] Agregar pruebas automatizadas de seguridad
- [ ] Desplegar dashboard de seguridad
- [ ] Realizar revisión de seguridad

---

## 📊 Métricas de seguridad y monitoreo

Indicadores clave (KPI)
| Métrica | Objetivo | Medición | Umbral de alerta |
|---|---|---|---|
| Tasa de éxito del procesamiento de pagos | > 99.9% | (Pagos exitosos / Pagos totales) × 100 | < 99.5% |
| Tasa de pagos duplicados | < 0.01% | (Pagos duplicados / Pagos totales) × 100 | > 0.05% |
| Tasa de éxito de validación de webhooks | > 99.8% | (Webhooks válidos / Webhooks totales) × 100 | < 99.5% |
| Tiempo promedio de procesamiento de pago | < 2s | Tiempo de procesamiento por transacción | > 5s |
| Tasa de incidentes de seguridad | 0 incidentes/mes | Número de incidentes de seguridad | > 0 |

Alertas automatizadas
```yaml
alerts:
  - name: "High Duplicate Payment Rate"
    condition: "duplicate_payment_rate > 0.05"
    severity: "CRITICAL"
    action: "immediate_rollback_investigation"

  - name: "Webhook Validation Failure Spike"
    condition: "webhook_validation_failure_rate > 0.02"
    severity: "HIGH" 
    action: "security_team_notification"

  - name: "Payment Processing Degradation"
    condition: "avg_payment_processing_time > 5000ms"
    severity: "MEDIUM"
    action: "infrastructure_team_notification"
```

Componentes del dashboard de seguridad
1. Monitor en tiempo real del flujo de pagos
   - Transacciones activas por estado
   - Tendencias de tiempo de procesamiento
   - Tasas de error por tipo
2. Panel de eventos de seguridad
   - Fallas de validación de webhooks
   - Eventos de bloqueo de IP
   - Activaciones de rate limiting
3. Métricas de protección de datos
   - Conteo de operaciones de cifrado
   - Cobertura de enmascaramiento de datos
   - Completitud del log de auditoría
4. Dashboard de integridad transaccional
   - Colisiones de idempotency key
   - Operaciones de rollback
   - Transacciones de compensación

---

## 🧪 Pruebas de seguridad

Pruebas unitarias de seguridad
```java
@Test
public void shouldPreventDuplicatePaymentProcessing() {
    // Given
    String transactionId = "txn_123";
    String idempotencyKey = "idem_abc";

    // When - first attempt
    Payments firstPayment = confirmPaymentUseCase.execute(
        createTestReceipt(transactionId, idempotencyKey), transactionId, idempotencyKey);

    // When - second attempt with same idempotency key
    Payments secondPayment = confirmPaymentUseCase.execute(
        createTestReceipt(transactionId, idempotencyKey), transactionId, idempotencyKey);

    // Then
    assertThat(firstPayment.getId()).isEqualTo(secondPayment.getId());
    assertThat(firstPayment.getStatus()).isEqualTo(PaymentStatus.PAID);
}
@Test
public void shouldRejectOldWebhook() {
    // Given
    EpaycoConfirmationDTO oldWebhook = createTestWebhook(
        Instant.now().minus(10, ChronoUnit.MINUTES));

    // When & Then
    assertThatThrownBy(() -> webhookValidator.validateWebhook(oldWebhook, publicKey))
        .isInstanceOf(InvalidWebhookException.class)
        .hasMessageContaining("Webhook too old");
}
@Test
public void shouldMaskSensitiveDataInLogs() {
    // Given
    PaymentAuditEvent event = PaymentAuditEvent.builder()
        .transactionId("txn_12345678")
        .build();

    // When
    String loggedData = paymentAuditLogger.maskSensitiveData(event);

    // Then
    assertThat(loggedData).contains("txn_1234***5678");
    assertThat(loggedData).doesNotContain("txn_12345678");
}
```

Pruebas de integración
```java
@SpringBootTest
@Transactional
public class PaymentSecurityIntegrationTest {

    @Test
    public void shouldHandleConcurrentPaymentConfirmations() throws Exception {
        // Given
        Payments pendingPayment = createPendingPayment();

        // When - simulate concurrent confirmations
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<Payments>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            futures.add(executor.submit(() -> 
                confirmPaymentUseCase.execute(
                    createTestReceipt(pendingPayment), 
                    pendingPayment.getProviderMetadata().getExternalId().getTransactionId(),
                    "idem_" + UUID.randomUUID())));
        }

        // Then
        List<Payments> results = futures.stream()
            .map(this::get)
            .collect(Collectors.toList());

        assertThat(results).hasSize(10);
        assertThat(results.stream().filter(p -> p.getStatus() == PaymentStatus.PAID))
            .hasSize(1); // Only one should be processed
    }
}
```

---

## 📋 Checklist de implementación

Requisitos previos
- [ ] Revisión y aprobación del equipo de seguridad
- [ ] Línea base de pruebas de performance establecida
- [ ] Procedimientos de backup documentados
- [ ] Plan de rollback aprobado
- [ ] Sistemas de monitoreo configurados

Fase de desarrollo
- [ ] Code review completado para todos los cambios de seguridad
- [ ] Análisis estático de código aprobado
- [ ] Escaneo de vulnerabilidades de dependencias aprobado
- [ ] Pruebas de seguridad escritas y pasando
- [ ] Documentación actualizada

Fase de pruebas
- [ ] Cobertura de unit tests > 90%
- [ ] Pruebas de integración cubriendo escenarios de seguridad
- [ ] Pruebas de performance sin degradación
- [ ] Pentesting completado
- [ ] Auditoría de seguridad completada

Despliegue
- [ ] Migraciones de BD probadas en staging
- [ ] Cifrado de configuración verificado
- [ ] Dashboards de monitoreo operativos
- [ ] Sistema de alertas probado
- [ ] Procedimientos de rollback validados

Post-despliegue
- [ ] Métricas de seguridad monitoreadas por 7 días
- [ ] Métricas de performance verificadas
- [ ] Equipo de respuesta a incidentes entrenado
- [ ] Documentación publicada
- [ ] Revisión de seguridad programada (30 días)

---

## 🔒 Referencia de configuración

Configuración de seguridad
```yaml
# application-security.yml
payment:
  security:
    webhook:
      max-timestamp-age-minutes: 5
      replay-protection-enabled: true
      allowed-ips:
        - "190.109.131.0/24"  # Epayco IP range
        - "190.109.132.0/24"

    rate-limiting:
      webhooks:
        requests-per-minute: 10
        burst-capacity: 20
      payments:
        requests-per-minute: 100
        burst-capacity: 200

    encryption:
      algorithm: "AES/GCM/NoPadding"
      key-size: 256
      provider: "BC"  # Bouncy Castle

    auditing:
      log-level: "INFO"
      mask-sensitive-data: true
      retention-days: 365

    transactions:
      isolation-level: "SERIALIZABLE"
      timeout-seconds: 30
      retry-attempts: 3
```

Configuración de base de datos
```properties
# PostgreSQL security settings
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true
# Transaction settings
spring.transaction.default-timeout=30
spring.jpa.properties.hibernate.connection.provider_disables_autocommit=true
```

---

## 📞 Procedimientos de emergencia

Respuesta a incidentes de seguridad
1. Respuesta inmediata (0-15 minutos)
   - Alertar al equipo de seguridad
   - Habilitar logging mejorado
   - Bloquear IPs sospechosas
   - Escalar el monitoreo
2. Fase de investigación (15-60 minutos)
   - Analizar logs y métricas
   - Identificar transacciones afectadas
   - Documentar el alcance
   - Preparar comunicación
3. Fase de contención (1-4 horas)
   - Implementar fixes temporales
   - Aislar sistemas afectados
   - Iniciar validación de datos
   - Comenzar recuperación
4. Fase de recuperación (4-24 horas)
   - Aplicar fixes permanentes
   - Validar integridad de datos
   - Monitorear estabilidad
   - Preparar informe del incidente

Procedimientos de rollback
```bash
# Database rollback
flyway rollback -target=V9__create_reservations_table.sql
# Service rollback
kubectl rollout undo deployment/payment-service
# Configuration rollback
kubectl apply -f k8s/payment-service-v1.2.yaml
```

---

## 📚 Recursos adicionales

Buenas prácticas de seguridad
- OWASP Cheat Sheet Series (incluye guías de integración segura con pasarelas y recomendaciones como idempotencia y mitigación de replay): https://cheatsheetseries.owasp.org/ [web:7]
- NIST Cybersecurity Framework: https://www.nist.gov/cyberframework [web:16]
- PCI Security Standards Council (PCI DSS): https://www.pcisecuritystandards.org/standards/ [web:17]

Documentación tecnológica
- Spring Security Reference: https://docs.spring.io/spring-security/reference/
- Hibernate ORM User Guide: https://docs.jboss.org/hibernate/orm/6.2/userguide/
- PostgreSQL Security: https://www.postgresql.org/docs/current/security.html

Monitoreo
- Spring Boot Actuator: https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html
- Micrometer Metrics: https://micrometer.io/
- Prometheus Best Practices: https://prometheus.io/docs/practices/

---

Estado del documento: ✅ COMPLETO  
Próxima revisión: 2026-03-03  
Aprobación del equipo de seguridad: Pendiente  
Líder de implementación: Oficina del CTO  
