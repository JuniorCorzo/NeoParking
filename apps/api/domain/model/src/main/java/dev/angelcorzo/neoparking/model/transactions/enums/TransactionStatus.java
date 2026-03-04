package dev.angelcorzo.neoparking.model.transactions.enums;

import dev.angelcorzo.neoparking.model.payments.enums.PaymentStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TransactionStatus {
  PENDING(PaymentStatus.PENDING_PAYMENT),
  APPROVED(PaymentStatus.PAID),
  DECLINED(PaymentStatus.FAILED),
  FAILED(PaymentStatus.FAILED),
  REVERSED(PaymentStatus.REFUNDED),
  CANCELED(PaymentStatus.CANCELED),
  EXPIRED(PaymentStatus.EXPIRED);

  private final PaymentStatus paymentStatus;
}
