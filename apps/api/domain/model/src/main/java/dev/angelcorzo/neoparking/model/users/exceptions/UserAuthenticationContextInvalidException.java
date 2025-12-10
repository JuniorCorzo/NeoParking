package dev.angelcorzo.neoparking.model.users.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

public class UserAuthenticationContextInvalidException extends RuntimeException {
  public UserAuthenticationContextInvalidException() {
    super(ErrorMessagesModel.USER_AUTHENTICATION_INVALID.format());
  }
}
