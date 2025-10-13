package dev.angelcorzo.neoparking.model.userinvitations.gateways;

import dev.angelcorzo.neoparking.model.userinvitations.UserInvitations;

import java.util.Optional;
import java.util.UUID;

public interface UserInvitationsRepository {
    UserInvitations findAllInvitationsByTenantId(UUID tenantId);
    Optional<UserInvitations> findByToken(UUID token);
    UserInvitations save(UserInvitations userInvitations);
    UserInvitations acceptedInvitation(UUID id);
    Boolean revokeInvitation(UUID id);
}
