package dev.angelcorzo.neoparking.api.userinvitations.dto;

import dev.angelcorzo.neoparking.model.users.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.UUID;

 @Builder(toBuilder = true)
public record InviteUserDTO(
    @NotEmpty @Email String email,
    @NotEmpty Roles role,
    @NotEmpty UUID tenantId,
    @NotEmpty UUID inviteBy) {}
