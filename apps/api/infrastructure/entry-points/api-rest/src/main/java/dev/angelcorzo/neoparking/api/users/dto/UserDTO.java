package dev.angelcorzo.neoparking.api.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.angelcorzo.neoparking.api.tenants.dto.TenantInfo;
import dev.angelcorzo.neoparking.model.tenants.Tenants;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public record UserDTO(
    UUID id,
    String fullName,
    String email,
    String password,
    Roles role,
    TenantInfo tenant,
    String contactInfo,
    UUID deletedBy,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    OffsetDateTime deletedAt) {}
