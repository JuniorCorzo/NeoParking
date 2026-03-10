package dev.angelcorzo.neoparking.usecase.registertenant.exceptions;

import dev.angelcorzo.neoparking.model.commons.exceptions.ErrorMessagesModel;

public class BadCredentialsException extends RuntimeException {
  public BadCredentialsException() {
    super(ErrorMessagesModel.USER_BAD_CREDENTIALS.toString());
  }
}
