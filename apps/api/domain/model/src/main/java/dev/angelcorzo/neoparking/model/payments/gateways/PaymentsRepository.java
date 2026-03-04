package dev.angelcorzo.neoparking.model.payments.gateways;

import dev.angelcorzo.neoparking.model.commons.result.Result;
import dev.angelcorzo.neoparking.model.payments.Payments;
import dev.angelcorzo.neoparking.model.payments.exceptions.PaymentError;
import java.util.Optional;
import java.util.UUID;

public interface PaymentsRepository {
  Optional<Payments> findById(UUID id);

  Optional<Payments> findByCheckoutSessionId(String checkoutSessionId);

  Optional<Payments> findByParkingTicketId(UUID parkingTicketId);

  boolean existsByParkingTicketId(UUID parkingTicketId);

  Payments getReferenceById(UUID id);

  Result<Payments, PaymentError> processPayment(Payments payment);
}
