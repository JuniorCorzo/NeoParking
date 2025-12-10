package dev.angelcorzo.neoparking.model.users;

import dev.angelcorzo.neoparking.model.users.enums.Roles;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserAuthentication(UUID userId, UUID tenantId, Roles role) {}
