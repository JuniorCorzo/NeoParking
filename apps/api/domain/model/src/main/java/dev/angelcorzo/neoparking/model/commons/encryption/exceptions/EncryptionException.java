package dev.angelcorzo.neoparking.model.commons.encryption.exceptions;

import dev.angelcorzo.neoparking.model.commons.exceptions.AppException;

public class EncryptionException extends AppException {

  public EncryptionException(EncryptionError exception) {
    super(exception.message(), exception.status(), exception.code());
  }
}
