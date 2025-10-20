package dev.angelcorzo.neoparking.model.tenants.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

import java.util.UUID;

/**
 * Thrown when an operation is attempted on a Tenant that does not exist.
 *
 * <p><strong>Layer:</strong> Domain</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 */
public class TenantNotExistsException extends IllegalArgumentException {
  public TenantNotExistsException(UUID uuid) {
    super(ErrorMessagesModel.TENANT_NOT_EXISTS.format(uuid));
  }
}
