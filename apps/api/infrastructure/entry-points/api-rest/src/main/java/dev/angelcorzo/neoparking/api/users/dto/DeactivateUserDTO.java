package dev.angelcorzo.neoparking.api.users.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DeactivateUserDTO(
        UUID userIdToDeactivate,
        UUID deactivatedBy,
        UUID tenantId,
        String reason
) {
}