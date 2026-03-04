package dev.angelcorzo.neoparking.paymentprovider.exceptions;

import dev.angelcorzo.neoparking.model.commons.exceptions.AppException;

public class ProviderAuthenticationException extends AppException {

  public ProviderAuthenticationException(String message, int status) {
    super(message, status, "PROVIDER_AUTHENTICATION_FAILED");
  }
}
