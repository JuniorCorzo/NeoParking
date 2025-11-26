package dev.angelcorzo.neoparking.model.specialpolicies.valueobjects;

import dev.angelcorzo.neoparking.model.specialpolicies.SpecialPolicies;
import dev.angelcorzo.neoparking.model.specialpolicies.enums.ModifiesTypes;
import dev.angelcorzo.neoparking.model.specialpolicies.enums.OperationsTypes;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record SpecialPoliciesReference(
    UUID id,
    String name,
    ModifiesTypes modifies,
    OperationsTypes operation,
    BigDecimal valueToModify,
    boolean active) {
  public static SpecialPoliciesReference of(SpecialPolicies specialPolicies) {
    return new SpecialPoliciesReference(
        specialPolicies.getId(),
        specialPolicies.getName(),
        specialPolicies.getModifies(),
        specialPolicies.getOperation(),
        specialPolicies.getValueToModify(),
        specialPolicies.isActive());
  }
}
