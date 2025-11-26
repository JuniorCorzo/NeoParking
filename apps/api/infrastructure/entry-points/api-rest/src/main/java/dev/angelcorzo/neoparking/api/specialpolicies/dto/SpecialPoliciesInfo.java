package dev.angelcorzo.neoparking.api.specialpolicies.dto;

import dev.angelcorzo.neoparking.model.specialpolicies.enums.ModifiesTypes;
import dev.angelcorzo.neoparking.model.specialpolicies.enums.OperationsTypes;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record SpecialPoliciesInfo(
    UUID id,
    String name,
    ModifiesTypes modifies,
    OperationsTypes operation,
    BigDecimal valueToModify,
    boolean active) {}
