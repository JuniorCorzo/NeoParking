package dev.angelcorzo.neoparking.model.payments.exceptions;

import dev.angelcorzo.neoparking.model.commons.exceptions.AppException;

public class ConfirmPaymentException extends AppException {
  public ConfirmPaymentException(PaymentError error) {
    super(error.message(), error.status(), error.code());
  }
}
