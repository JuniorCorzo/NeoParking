package dev.angelcorzo.neoparking.api.users.dto;

import dev.angelcorzo.neoparking.model.users.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record InviteUserDTO(
        @NotEmpty
        @Email
        String email,
        @NotEmpty
        Roles role,
        @NotEmpty
        UUID tenantId,
        @NotEmpty
        UUID inviteBy

) {}
