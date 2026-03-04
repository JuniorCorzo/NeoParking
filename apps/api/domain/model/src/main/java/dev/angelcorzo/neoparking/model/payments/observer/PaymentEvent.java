package dev.angelcorzo.neoparking.model.payments.observer;

import dev.angelcorzo.neoparking.model.payments.enums.PaymentStatus;
import java.util.UUID;

public record PaymentEvent(UUID paymentId, PaymentStatus status) {

  public static PaymentEvent of(UUID paymentId, PaymentStatus status) {
    return new PaymentEvent(paymentId, status);
  }
}
