package dev.angelcorzo.neoparking.model.payments.exceptions;

import dev.angelcorzo.neoparking.model.commons.exceptions.AppException;
import dev.angelcorzo.neoparking.model.commons.result.DomainError;

public class ProcessPaymentException extends AppException {
  public ProcessPaymentException(DomainError error) {
    super(error.message(), error.status(), error.code());
  }
}
