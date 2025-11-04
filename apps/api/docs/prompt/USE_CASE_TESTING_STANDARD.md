# Estándar de Testing para Casos de Uso - NeoParking

Este documento describe el estándar y las mejores prácticas para escribir tests unitarios e integración para los **casos de uso** (`domain/usecase`) del proyecto NeoParking. Complementa el estándar de testing para adaptadores JPA proporcionando una estrategia específica para probar la lógica de negocio.

El objetivo es asegurar que los tests de casos de uso sean consistentes, legibles, robustos, mantenibles y que verifiquen tanto el comportamiento esperado como los casos límite y de error.

---

## 1. Checklist de Análisis Previo

Antes de escribir o modificar un test para un caso de uso, asegúrate de comprender el contexto:

- [ ] **Caso de Uso Bajo Prueba:** ¿Cuál es el identificador (ej: CU-001 - Registrar Nuevo Propietario) y cuál es su responsabilidad principal?
- [ ] **Entrada y Salida:** ¿Qué datos recibe como entrada y qué devuelve como resultado?
- [ ] **Gateways Involucrados:** ¿Qué interfaces de repositorio/gateway utiliza? (Ej: `TenantsGateway`, `UsersGateway`)
- [ ] **Modelos de Dominio:** ¿Qué entidades de dominio están involucradas? (Ej: `Tenants`, `Users`)
- [ ] **Precondiciones:** ¿Qué estado previo debe existir en el sistema?
- [ ] **Flujos Alternativos:** ¿Qué caminos alternativos o de error existen? (Ej: email duplicado, usuario sin permisos)
- [ ] **Postcondiciones:** ¿Cuál es el estado esperado del sistema después de ejecutar el caso de uso?
- [ ] **Dependencias:** ¿El caso de uso depende de otros casos de uso o servicios externos?

---

## 2. Estrategia de Testing por Capas

### 2.1 Niveles de Testing

Los casos de uso se prueban en **dos niveles**:

#### a) **Unit Tests (Unitarios)**
- Prueba la lógica del caso de uso **aislado**
- Usa **mocks** para los gateways (repositorios)
- Valida que el caso de uso orqueste correctamente las llamadas a los gateways
- Rápido de ejecutar, independiente de la BD

#### b) **Integration Tests (Integración)**
- Prueba el caso de uso con **gateways reales** (accediendo a BD de prueba)
- Valida el flujo completo: entrada → lógica → persistencia → salida
- Usa ` @DataJpaTest` para el contexto de BD
- Más lento, pero valida interacciones reales

### 2.2 Cuándo Usar Cada Nivel

| Aspecto | Unit Test | Integration Test |
|--------|-----------|-----------------|
| **Velocidad** | Muy rápido | Moderado |
| **Objetivo** | Lógica de negocio pura | Flujo completo |
| **Datos** | Mocks | Base de datos real (test) |
| **Ideal para** | Validaciones, decisiones lógicas | Persistencia, transacciones |
| **Cobertura** | Casos normales y edge cases | Happy path + errores críticos |

---

## 3. Plantilla de Test Unitario (Unit Test)

Usa la siguiente estructura como base para tus clases de test unitario de casos de uso.

```java
package dev.angelcorzo.neoparking.usecase.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

 @DisplayName("ExampleUseCase Tests")
class ExampleUseCaseTest {

    // --- Dependencias del Caso de Uso (Mocks) ---
    private ExampleGateway exampleGateway;
    private AnotherGateway anotherGateway;

    // --- Caso de Uso Bajo Prueba ---
    private ExampleUseCase exampleUseCase;

    // --- Ciclo de Vida y Setup ---

    @BeforeEach
    void setUp() {
        // 1. Crear mocks de los gateways
        exampleGateway = mock(ExampleGateway.class);
        anotherGateway = mock(AnotherGateway.class);

        // 2. Instanciar el caso de uso con los mocks inyectados
        exampleUseCase = new ExampleUseCase(exampleGateway, anotherGateway);

        // 3. Configurar comportamientos por defecto de los mocks (si aplica)
        // when(exampleGateway.findById(any())).thenReturn(Optional.empty());
    }

    // --- Agrupación de Tests por Operación ---

    @Nested @DisplayName("Happy Path - Casos Exitosos")
    class HappyPath {

        @Test @DisplayName("Should execute use case successfully with valid input")
        void shouldExecuteUseCaseSuccessfully() {
            // Arrange (Given): Prepara los datos de entrada y configura el comportamiento esperado
            ExampleRequest request = ExampleRequest.builder()
                    .field1("value1")
                    .field2("value2")
                    .build();

            ExampleModel mockModel = ExampleModel.builder()
                    .id(UUID.randomUUID())
                    .field1("value1")
                    .build();

            when(exampleGateway.save(any(ExampleModel.class)))
                    .thenReturn(mockModel);

            // Act (When): Ejecuta el método del caso de uso
            ExampleResponse response = exampleUseCase.execute(request);

            // Assert (Then): Verifica que el resultado es el esperado
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(mockModel.getId());
            assertThat(response.getField1()).isEqualTo("value1");

            // Verifica que el gateway fue llamado con los parámetros correctos
            verify(exampleGateway).save(any(ExampleModel.class));
        }
    }

    @Nested @DisplayName("Validation & Error Cases")
    class ValidationAndErrorCases {

        @Test @DisplayName("Should throw exception when required field is null")
        void shouldThrowExceptionWhenRequiredFieldIsNull() {
            // Arrange (Given): Prepara un request inválido
            ExampleRequest invalidRequest = ExampleRequest.builder()
                    .field1(null)  // Campo requerido nulo
                    .field2("value")
                    .build();

            // Act & Assert (When & Then): Verifica que se lanza una excepción
            assertThatThrownBy(() -> exampleUseCase.execute(invalidRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("field1 is required");

            // Verifica que el gateway NO fue invocado (porque fallo la validación)
            verify(exampleGateway, never()).save(any());
        }

        @Test @DisplayName("Should throw exception when gateway fails")
        void shouldThrowExceptionWhenGatewayFails() {
            // Arrange (Given): Configura el gateway para lanzar una excepción
            ExampleRequest request = ExampleRequest.builder()
                    .field1("value1")
                    .field2("value2")
                    .build();

            when(exampleGateway.save(any()))
                    .thenThrow(new RuntimeException("Database error"));

            // Act & Assert (When & Then)
            assertThatThrownBy(() -> exampleUseCase.execute(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Database error");
        }
    }

    @Nested @DisplayName("Business Rules & Edge Cases")
    class BusinessRulesAndEdgeCases {

        @Test @DisplayName("Should apply business rule: email must be unique")
        void shouldValidateEmailUniqueness() {
            // Arrange (Given): Configura el gateway para simular que el email ya existe
            String duplicateEmail = "test @example.com";
            ExampleRequest request = ExampleRequest.builder()
                    .email(duplicateEmail)
                    .build();

            when(exampleGateway.findByEmail(duplicateEmail))
                    .thenReturn(Optional.of(ExampleModel.builder().email(duplicateEmail).build()));

            // Act & Assert (When & Then)
            assertThatThrownBy(() -> exampleUseCase.execute(request))
                    .isInstanceOf(DuplicateEmailException.class);
        }

        @Test @DisplayName("Should handle empty/null response from gateway gracefully")
        void shouldHandleEmptyResponseGracefully() {
            // Arrange (Given)
            ExampleRequest request = ExampleRequest.builder()
                    .field1("value1")
                    .build();

            when(exampleGateway.findById(any()))
                    .thenReturn(Optional.empty());

            // Act (When)
            ExampleResponse response = exampleUseCase.execute(request);

            // Assert (Then): Verifica que el caso de uso maneja correctamente el empty
            assertThat(response).isNull();
            // O, si aplica: assertThat(response.getStatus()).isEqualTo("NOT_FOUND");
        }
    }

    @Nested @DisplayName("Multi-Gateway Interactions")
    class MultiGatewayInteractions {

        @Test @DisplayName("Should call gateways in correct order and with correct data")
        void shouldCallGatewaysInCorrectOrder() {
            // Arrange (Given)
            ExampleRequest request = ExampleRequest.builder().field1("test").build();
            ExampleModel savedModel = ExampleModel.builder().id(UUID.randomUUID()).build();
            AnotherModel anotherModel = AnotherModel.builder().id(UUID.randomUUID()).build();

            when(exampleGateway.save(any())).thenReturn(savedModel);
            when(anotherGateway.create(any())).thenReturn(anotherModel);

            // Act (When)
            ExampleResponse response = exampleUseCase.execute(request);

            // Assert (Then): Verifica las llamadas a los gateways
            verify(exampleGateway).save(any(ExampleModel.class));
            verify(anotherGateway).create(any(AnotherModel.class));

            assertThat(response.getExampleId()).isEqualTo(savedModel.getId());
            assertThat(response.getAnotherId()).isEqualTo(anotherModel.getId());
        }
    }
}
```

---

## 4. Plantilla de Test de Integración (Integration Test)

Usa la siguiente estructura para tests que requieren acceso a la base de datos.

```java
package dev.angelcorzo.neoparking.usecase.example.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

 @DataJpaTest @ActiveProfiles("test")
 @DisplayName("ExampleUseCase Integration Tests")
class ExampleUseCaseIntegrationTest {

    @Autowired
    private ExampleRepositoryAdapter exampleRepositoryAdapter;

    @Autowired
    private ExampleRepositoryData exampleRepositoryData;

    @Autowired
    private AnotherRepositoryAdapter anotherRepositoryAdapter;

    @Autowired
    private AnotherRepositoryData anotherRepositoryData;

    // --- Caso de Uso Bajo Prueba ---
    private ExampleUseCase exampleUseCase;

    // --- Datos base para los tests ---
    private ExampleModel baseExample;
    private AnotherModel baseAnother;

    @BeforeEach
    void setUp() {
        // 1. Limpiar todas las tablas relevantes
        anotherRepositoryData.deleteAll();
        exampleRepositoryData.deleteAll();

        // 2. Crear datos base (fixtures) comunes a muchos tests
        baseExample = ExampleModel.builder()
                .id(UUID.randomUUID())
                .field1("base")
                .field2("fixture")
                .build();
        exampleRepositoryData.save(baseExample);

        baseAnother = AnotherModel.builder()
                .id(UUID.randomUUID())
                .description("another fixture")
                .build();
        anotherRepositoryData.save(baseAnother);

        // 3. Instanciar el caso de uso con los adaptadores reales
        exampleUseCase = new ExampleUseCase(exampleRepositoryAdapter, anotherRepositoryAdapter);
    }

    @Nested @DisplayName("Happy Path - Complete Flow")
    class HappyPathCompleteFlow {

        @Test @DisplayName("Should execute complete use case and persist data to database")
        void shouldExecuteCompleteFlowAndPersist() {
            // Arrange (Given): Los datos base ya existen en la BD gracias a setUp()
            ExampleRequest request = ExampleRequest.builder()
                    .field1("newValue")
                    .field2("anotherValue")
                    .build();

            // Act (When): Ejecuta el caso de uso
            ExampleResponse response = exampleUseCase.execute(request);

            // Assert (Then): Verifica el resultado devuelto
            assertThat(response).isNotNull();
            assertThat(response.getId()).isNotNull();

            // Verifica que los datos se guardaron correctamente en la BD
            Optional<ExampleData> persistedData = exampleRepositoryData
                    .findById(response.getId());
            assertThat(persistedData).isPresent();
            assertThat(persistedData.get().getField1()).isEqualTo("newValue");
            assertThat(persistedData.get().getField2()).isEqualTo("anotherValue");
        }
    }

    @Nested @DisplayName("Data Consistency")
    class DataConsistency {

        @Test @DisplayName("Should maintain referential integrity between tables")
        void shouldMaintainReferentialIntegrity() {
            // Arrange (Given)
            ExampleRequest request = ExampleRequest.builder()
                    .field1("test")
                    .anotherModelId(baseAnother.getId())
                    .build();

            // Act (When)
            ExampleResponse response = exampleUseCase.execute(request);

            // Assert (Then)
            Optional<ExampleData> persistedData = exampleRepositoryData
                    .findById(response.getId());
            assertThat(persistedData).isPresent();
            assertThat(persistedData.get().getAnotherModelId())
                    .isEqualTo(baseAnother.getId());

            // Verifica que la referencia existe en la otra tabla
            Optional<AnotherData> relatedData = anotherRepositoryData
                    .findById(baseAnother.getId());
            assertThat(relatedData).isPresent();
        }
    }

    @Nested @DisplayName("Error Handling with Real DB")
    class ErrorHandlingWithRealDB {

        @Test @DisplayName("Should throw exception when foreign key constraint violated")
        void shouldThrowExceptionOnForeignKeyViolation() {
            // Arrange (Given): Usa un ID que no existe en otra tabla
            UUID nonExistentId = UUID.randomUUID();
            ExampleRequest request = ExampleRequest.builder()
                    .field1("test")
                    .anotherModelId(nonExistentId)
                    .build();

            // Act & Assert (When & Then)
            assertThatThrownBy(() -> exampleUseCase.execute(request))
                    .isInstanceOf(DataIntegrityViolationException.class);

            // Verifica que NO se creó un registro en la BD
            assertThat(exampleRepositoryData.count()).isEqualTo(1);  // Solo el fixture base
        }

        @Test @DisplayName("Should handle concurrent modifications")
        void shouldHandleConcurrentModifications() {
            // Arrange (Given): Simula que otro proceso modifica los datos
            ExampleRequest request = ExampleRequest.builder()
                    .field1("test")
                    .build();

            // Simula modificación externa (otro cliente/servidor)
            baseExample.setField1("modified");
            exampleRepositoryData.save(baseExample);

            // Act (When)
            ExampleResponse response = exampleUseCase.execute(request);

            // Assert (Then): El caso de uso debe manejar la modificación
            assertThat(response).isNotNull();
        }
    }

    @Nested @DisplayName("Boundary Conditions")
    class BoundaryConditions {

        @Test @DisplayName("Should handle maximum string length in fields")
        void shouldHandleMaxStringLength() {
            // Arrange (Given)
            String longString = "a".repeat(1000);
            ExampleRequest request = ExampleRequest.builder()
                    .field1(longString)
                    .build();

            // Act (When)
            ExampleResponse response = exampleUseCase.execute(request);

            // Assert (Then)
            assertThat(response).isNotNull();
            Optional<ExampleData> persisted = exampleRepositoryData
                    .findById(response.getId());
            assertThat(persisted.get().getField1()).hasSize(1000);
        }

        @Test @DisplayName("Should handle empty collections correctly")
        void shouldHandleEmptyCollections() {
            // Arrange (Given): Buscar en una tabla vacía
            anotherRepositoryData.deleteAll();

            ExampleRequest request = ExampleRequest.builder()
                    .field1("test")
                    .build();

            // Act (When)
            ExampleResponse response = exampleUseCase.execute(request);

            // Assert (Then)
            assertThat(response).isNotNull();
        }
    }
}
```

---

## 5. Guía de Convenciones

### 5.1 Anotaciones Principales

| Anotación | Uso | Propósito |
|-----------|-----|----------|
| ` @DisplayName` | Clases y métodos | Proporciona nombres legibles en reportes |
| ` @Nested` | Clases internas | Agrupa tests por funcionalidad o escenario |
| ` @BeforeEach` | Métodos setup | Ejecuta antes de cada test |
| ` @AfterEach` | Métodos teardown | Ejecuta después de cada test (limpieza) |
| ` @Test` | Métodos | Marca como caso de test |
| ` @DataJpaTest` | Clases (Integration) | Configura contexto de BD para tests |
| ` @ActiveProfiles("test")` | Clases (Integration) | Usa configuración de `application-test.yaml` |

### 5.2 Nomenclatura de Métodos de Test

Usa el patrón **`should<Accion><Objeto><Condicion>`**:

**Ejemplos para Casos de Uso:**
- `shouldRegisterNewTenantSuccessfully`
- `shouldThrowExceptionWhenEmailAlreadyExists`
- `shouldCalculateParkingFeeCorrectlyForTwoHours`
- `shouldReserveParkingSlotWithinAvailableRange`
- `shouldReturnEmptyListWhenNoParkingsFound`
- `shouldUpdateUserRoleWithCorrectPermissions`
- `shouldHandleCheckoutWithMultipleTariffZones`

### 5.3 Estructura del Test (Arrange-Act-Assert)

Cada test sigue el patrón **AAA** (Arrange-Act-Assert):

```
Arrange (Given):
  - Prepara los datos iniciales
  - Configura los mocks (si es unit test)
  - Configura el estado del sistema

Act (When):
  - Ejecuta el método/caso de uso bajo prueba

Assert (Then):
  - Verifica que el resultado es correcto
  - Verifica que se llamaron los gateways correctamente
  - Verifica que el estado final del sistema es consistente
```

### 5.4 Uso de AssertJ

Siempre usa **AssertJ** (`assertThat`) para aserciones más legibles:

```java
// ✅ Bueno - Claro y expresivo
assertThat(response)
    .isNotNull()
    .hasFieldOrPropertyWithValue("status", "SUCCESS");

assertThat(savedItems)
    .isNotEmpty()
    .hasSize(3)
    .extracting("id")
    .doesNotContainNull();

// ❌ Evita - Menos legible
assert response != null;
assertTrue(response.getStatus().equals("SUCCESS"));
```

---

## 6. Catálogo de Escenarios Comunes por Caso de Uso

Cada caso de uso debe cubrir mínimamente los siguientes escenarios:

### 6.1 Escenarios Universales (Aplican a Todos)

| Escenario | Descripción | Ejemplo |
|-----------|-------------|---------|
| **Happy Path** | Flujo exitoso con datos válidos | Usuario registrado correctamente |
| **Validación de Entrada** | Campos requeridos nulos/vacíos | Email nulo en registro |
| **Casos Límite** | Valores mínimos/máximos | String de 1 caracter, ID = 0 |
| **Estado Previo Incorrecto** | Precondiciones no cumplidas | Crear plaza en parqueadero inexistente |
| **Manejo de Errores** | Excepciones de gateways | Error de BD, timeout |

### 6.2 Escenarios Específicos por Módulo

#### **Módulo de Autenticación (CU-001, CU-002)**

```
✓ Registro exitoso de nuevo inquilino
✓ Email duplicado rechazado
✓ Contraseña débil rechazada
✓ Inquilino creado con Owner asociado
✓ Login exitoso
✓ Credenciales inválidas
✓ Usuario bloqueado después de N intentos
✓ Multi-tenancy: usuario solo accede a su tenant
```

#### **Módulo de Gestión de Parqueaderos (CU-003, CU-004, CU-005)**

```
✓ Crear parqueadero con datos válidos
✓ Capacidad negativa rechazada
✓ Actualizar datos del parqueadero
✓ Listar solo parqueaderos del tenant actual
✓ Eliminar parqueadero (soft delete)
✓ Validar unicidad de nombre dentro del tenant
```

#### **Módulo de Gestión de Plazas (CU-006, CU-007, CU-008)**

```
✓ Crear plaza con número único
✓ Número de plaza duplicado rechazado
✓ Cambiar tipo de vehículo de plaza
✓ Listar plazas con sus estados (libre/ocupada/reservada)
✓ Filtrar plazas por estado o tipo
✓ No permitir crear plaza en parqueadero inexistente
```

#### **Módulo de Configuración de Tarifas (CU-009, CU-010)**

```
✓ Definir tarifa por hora
✓ Definir tarifa diferenciada por zona
✓ Calcular cobro correcto para duración estándar
✓ Calcular cobro con fracciones de hora
✓ Aplicar descuentos especiales
✓ Tarifa negativa rechazada
✓ Calculo de tarifa cuando no hay tarifa configurada (error)
```

#### **Módulo de Reservas (CU-011, CU-012, CU-013)**

```
✓ Crear reserva exitosa para fecha/hora válida
✓ Reservar plaza que ya está reservada (overbooking) → ERROR
✓ Reservar en fecha pasada → ERROR
✓ Cancelar reserva dentro del plazo
✓ Cancelar fuera de plazo → Penalización o ERROR
✓ Consultar disponibilidad retorna plazas correctas
✓ Disponibilidad respeta reservas existentes
```

#### **Módulo de Operaciones (CU-014, CU-015, CU-016, CU-017)**

```
✓ Check-in con reserva válida
✓ Check-in sin reserva a plaza libre
✓ Check-in a plaza ocupada → ERROR
✓ Check-out genera cobro correcto
✓ Cobro con tarifa diferenciada por zona
✓ Check-out sin check-in previo → ERROR
✓ Recibo generado correctamente
✓ Transacción registrada en auditoría
```

#### **Módulo de Reportes (CU-018, CU-019, CU-020)**

```
✓ Dashboard muestra datos en tiempo real
✓ Reporte de ingresos en rango de fechas
✓ Reporte de ocupación con métricas
✓ Exportar reporte en CSV
✓ Exportar reporte en PDF
✓ Filtrar reportes por parqueadero
```

#### **Módulo de Gestión de Usuarios (CU-023, CU-024, CU-025, CU-026)**

```
✓ Invitar usuario con rol específico
✓ Aceptar invitación y crear cuenta
✓ Token de invitación expirado → ERROR
✓ Cambiar rol de usuario
✓ Prohibir cambiar único Owner
✓ Desactivar usuario revoca acceso
✓ Auditar cambios de rol
```

---

## 7. Estrategia de Cobertura y Documentación

### 7.1 Cobertura de Código

- **Mínimo esperado:** 80% cobertura en lógica de negocio
- **Objetivo:** 90%+ en casos de uso críticos
- **Excluir:** DTOs, entidades sin lógica, métodos generados

### 7.2 Documentación Inline

```java
 @Test @DisplayName("Should calculate parking fee with tiered pricing for extended stay")
void shouldCalculateParkingFeeWithTieredPricing() {
    // CU-010: Calcular Cobro
    // Escenario: Usuario estaciona 8 horas en zona premium con tarifa escalonada
    // - 0-2 horas: $2.000/hora
    // - 2-6 horas: $1.500/hora
    // - 6+ horas: $1.000/hora
    // Esperado: (2*2000) + (4*1500) + (2*1000) = $12.000
    
    // Arrange
    ...
}
```

---

## 8. Mejores Prácticas

### ✅ HACER

1. **Crear un archivo de test por caso de uso** en estructura paralela:
   - Código: `domain/usecase/example/ExampleUseCase.java`
   - Tests: `domain/usecase/example/ExampleUseCaseTest.java`
   - Tests Integración: `domain/usecase/example/integration/ExampleUseCaseIntegrationTest.java`

2. **Nombres descriptivos y autoexplicativos** para tests
3. **Separar happy path de error cases** con ` @Nested`
4. **Verificar comportamiento, no implementación**
5. **Usar fixtures y builders** para datos consistentes
6. **Limpiar datos antes de cada test** con ` @BeforeEach`
7. **Documentar el CU y escenario** en comentarios del test

### ❌ EVITAR

1. **Tests que dependen unos de otros** (cada test debe ser independiente)
2. **Usar valores hardcodeados** sin significado (usa constantes con nombres)
3. **Verificar multiple cosas en un solo test** (un test = una responsabilidad)
4. **Ignorar tests sin razón documentada** (` @Disabled("Razón clara")`)
5. **Lógica compleja dentro de tests** (crea helpers/builders)
6. **Esperar tiempo real** (mock tiempo si es necesario)

---

## 9. Configuración de application-test.yaml

```yaml
# application-test.yaml
spring:
  datasource:
    url: jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false
    driver-class-name: org.h2.Driver
    username: sa
    password:

  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true

  h2:
    console:
      enabled: true
```

---

## 10. Ejemplo Completo: CU-001 - Registrar Nuevo Propietario

### 10.1 Unit Test

```java
 @DisplayName("CU-001: Registrar Nuevo Propietario")
class RegisterNewTenantUseCaseTest {

    private TenantsGateway tenantsGateway;
    private UsersGateway usersGateway;
    private RegisterNewTenantUseCase registerNewTenantUseCase;

    @BeforeEach
    void setUp() {
        tenantsGateway = mock(TenantsGateway.class);
        usersGateway = mock(UsersGateway.class);
        registerNewTenantUseCase = new RegisterNewTenantUseCase(tenantsGateway, usersGateway);
    }

    @Nested @DisplayName("Happy Path")
    class HappyPath {

        @Test @DisplayName("Should register new tenant and owner successfully")
        void shouldRegisterNewTenantAndOwnerSuccessfully() {
            // Arrange
            RegistrationRequest request = RegistrationRequest.builder()
                    .email("owner @example.com")
                    .password("SecurePass123!")
                    .parkingName("Central Parking")
                    .build();

            Tenants savedTenant = Tenants.builder()
                    .id(UUID.randomUUID())
                    .name("Central Parking")
                    .build();

            Users savedOwner = Users.builder()
                    .id(UUID.randomUUID())
                    .email("owner @example.com")
                    .role(Role.OWNER)
                    .tenantId(savedTenant.getId())
                    .build();

            when(tenantsGateway.save(any(Tenants.class))).thenReturn(savedTenant);
            when(usersGateway.save(any(Users.class))).thenReturn(savedOwner);

            // Act
            RegistrationResponse response = registerNewTenantUseCase.execute(request);

            // Assert
            assertThat(response)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("tenantId", savedTenant.getId())
                    .hasFieldOrPropertyWithValue("userId", savedOwner.getId());

            verify(tenantsGateway).save(any(Tenants.class));
            verify(usersGateway).save(any(Users.class));
        }
    }

    @Nested @DisplayName("Validation")
    class Validation {

        @Test @DisplayName("Should throw exception when email is null")
        void shouldThrowExceptionWhenEmailIsNull() {
            RegistrationRequest invalidRequest = RegistrationRequest.builder()
                    .email(null)
                    .password("password")
                    .parkingName("Parking")
                    .build();

            assertThatThrownBy(() -> registerNewTenantUseCase.execute(invalidRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("email is required");

            verify(tenantsGateway, never()).save(any());
        }
    }

    @Nested @DisplayName("Business Rules")
    class BusinessRules {

        @Test @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailExists() {
            // Arrange
            RegistrationRequest request = RegistrationRequest.builder()
                    .email("existing @example.com")
                    .password("password")
                    .parkingName("New Parking")
                    .build();

            when(usersGateway.findByEmail("existing @example.com"))
                    .thenReturn(Optional.of(Users.builder().build()));

            // Act & Assert
            assertThatThrownBy(() -> registerNewTenantUseCase.execute(request))
                    .isInstanceOf(EmailAlreadyExistsException.class);

            verify(tenantsGateway, never()).save(any());
        }
    }
}
```

### 10.2 Integration Test

```java
 @DataJpaTest @ActiveProfiles("test")
 @DisplayName("CU-001: Registrar Nuevo Propietario - Integration Tests")
class RegisterNewTenantUseCaseIntegrationTest {

    @Autowired
    private TenantsRepositoryAdapter tenantsRepositoryAdapter;
    @Autowired
    private TenantsRepositoryData tenantsRepositoryData;

    @Autowired
    private UsersRepositoryAdapter usersRepositoryAdapter;
    @Autowired
    private UsersRepositoryData usersRepositoryData;

    private RegisterNewTenantUseCase registerNewTenantUseCase;

    @BeforeEach
    void setUp() {
        usersRepositoryData.deleteAll();
        tenantsRepositoryData.deleteAll();

        registerNewTenantUseCase = new RegisterNewTenantUseCase(
                tenantsRepositoryAdapter,
                usersRepositoryAdapter
        );
    }

    @Nested @DisplayName("Complete Flow")
    class CompleteFlow {

        @Test @DisplayName("Should persist tenant and owner to database")
        void shouldPersistTenantAndOwnerToDatabase() {
            // Arrange
            RegistrationRequest request = RegistrationRequest.builder()
                    .email("newowner @example.com")
                    .password("SecurePass123!")
                    .parkingName("North Parking")
                    .build();

            // Act
            RegistrationResponse response = registerNewTenantUseCase.execute(request);

            // Assert
            assertThat(response).isNotNull();

            Optional<TenantsData> persistedTenant = tenantsRepositoryData
                    .findById(response.getTenantId());
            assertThat(persistedTenant)
                    .isPresent()
                    .hasValueSatisfying(t -> assertThat(t.getName()).isEqualTo("North Parking"));

            Optional<UsersData> persistedUser = usersRepositoryData
                    .findByEmail("newowner @example.com");
            assertThat(persistedUser)
                    .isPresent()
                    .hasValueSatisfying(u -> {
                        assertThat(u.getRole()).isEqualTo(Role.OWNER);
                        assertThat(u.getTenantId()).isEqualTo(response.getTenantId());
                    });
        }
    }

    @Nested @DisplayName("Data Integrity")
    class DataIntegrity {

        @Test @DisplayName("Should not create user if tenant creation fails")
        void shouldNotCreateUserIfTenantFails() {
            // Arrange: Mock para simular fallo en tenant (usar spy)
            RegistrationRequest request = RegistrationRequest.builder()
                    .email("test @example.com")
                    .password("pass")
                    .parkingName("Test")
                    .build();

            // Act & Assert: Dependería de la implementación real
            // Este test validaría transacción
        }
    }
}
```

---

## 11. Checklist de Revisión de Tests

Antes de hacer commit de nuevos tests, verifica:

- [ ] Tests nombrados según convención `should<Accion><Objeto><Condicion>`
- [ ] Cada test tiene un único ` @Test` (una responsabilidad)
- [ ] Estructura Arrange-Act-Assert clara y visible
- [ ] Mocks configurados con `when(...).thenReturn(...)`
- [ ] Aserciones usan AssertJ (`assertThat`)
- [ ] Se verifica comportamiento, no implementación
- [ ] Tests son independientes (no dependen de orden ni de otros tests)
- [ ] `setUp()` con ` @BeforeEach` limpia estado previo
- [ ] Tests documentados con ` @DisplayName` y comentarios cuando necesario
- [ ] Cobertura ≥ 80% en lógica de negocio
- [ ] Tests pasan en CI/CD
- [ ] No hay tests ` @Disabled` sin razón documentada

---

## 12. Recursos y Referencias

- **AssertJ Documentation:** https://assertj.github.io/assertj-core-features-highlight.html
- **Mockito Documentation:** https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html
- **JUnit 5 Documentation:** https://junit.org/junit5/docs/current/user-guide/
- **Testing Best Practices (Martin Fowler):** https://martinfowler.com/testing.html
- **Spring Boot Test Documentation:** https://spring.io/guides/gs/testing-web/

---

**Versión:** 1.0  
**Proyecto:** NeoParking - Sistema de Gestión de Parqueaderos Multi-inquilino  
**Última Actualización:** Octubre 2025
