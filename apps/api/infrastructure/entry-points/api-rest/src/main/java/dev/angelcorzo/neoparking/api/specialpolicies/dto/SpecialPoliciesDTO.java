package dev.angelcorzo.neoparking.api.specialpolicies.dto;

import dev.angelcorzo.neoparking.api.tenants.dto.TenantInfo;
import dev.angelcorzo.neoparking.model.specialpolicies.enums.ModifiesTypes;
import dev.angelcorzo.neoparking.model.specialpolicies.enums.OperationsTypes;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record SpecialPoliciesDTO(
    UUID id,
    TenantInfo tenant,
    String name,
    ModifiesTypes modifies,
    OperationsTypes operation,
    BigDecimal valueToModify,
    boolean active,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt) {}
