package dev.angelcorzo.neoparking.model.commons.encryption.gateways;

import dev.angelcorzo.neoparking.model.commons.encryption.exceptions.EncryptionError;
import dev.angelcorzo.neoparking.model.commons.result.Result;

public interface EncryptionGateway {
  <T> Result<String, EncryptionError> encrypt(T data);

  <T> Result<T, EncryptionError> decrypt(String data, Class<T> clazz);
}
