package dev.angelcorzo.neoparking.api.userinvitations.dto;

import dev.angelcorzo.neoparking.model.tenants.Tenants;
import dev.angelcorzo.neoparking.model.userinvitations.UserInvitationStatus;
import dev.angelcorzo.neoparking.model.users.Users;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record UserInvitationsDTO(
        UUID id,
        Tenants tenant,
        String invitedEmail,
        Roles role,
        UUID token,
        UserInvitationStatus status,
        Users invitedBy,
        OffsetDateTime createdAt,
        OffsetDateTime acceptedAt,
        OffsetDateTime expiredAt
) {}
