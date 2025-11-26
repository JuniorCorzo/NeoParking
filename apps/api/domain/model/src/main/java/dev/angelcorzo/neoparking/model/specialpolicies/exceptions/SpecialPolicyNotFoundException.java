package dev.angelcorzo.neoparking.model.specialpolicies.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;
import java.util.UUID;

public class SpecialPolicyNotFoundException extends RuntimeException {
  public SpecialPolicyNotFoundException(UUID specialPolicyId) {
    super(ErrorMessagesModel.SPECIAL_POLICY_NOT_FOUNT.format(specialPolicyId));
  }
}
