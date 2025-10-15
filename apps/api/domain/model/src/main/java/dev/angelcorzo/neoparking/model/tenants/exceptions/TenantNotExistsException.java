package dev.angelcorzo.neoparking.model.tenants.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

import java.util.UUID;

public class TenantNotExistsException extends IllegalArgumentException {
  public TenantNotExistsException(UUID uuid) {
    super(ErrorMessagesModel.TENANT_NOT_EXISTS.format(uuid));
  }
}
