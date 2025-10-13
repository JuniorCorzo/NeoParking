package dev.angelcorzo.neoparking.usecase.inviteuserwithrol;

import dev.angelcorzo.neoparking.model.userinvitations.UserInvitationStatus;
import dev.angelcorzo.neoparking.model.userinvitations.UserInvitations;
import dev.angelcorzo.neoparking.usecase.acceptinvitation.exceptions.InvalidRoleException;
import dev.angelcorzo.neoparking.model.userinvitations.gateways.UserInvitationsRepository;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import dev.angelcorzo.neoparking.model.users.exceptions.UserAlreadyExistsInTenantException;
import dev.angelcorzo.neoparking.model.users.exceptions.UserNotExistsException;
import dev.angelcorzo.neoparking.model.users.gateways.UsersRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class InviteUserWithRolUseCase {
    private static final int EXPIRATION_DAYS = 3;
    private final UsersRepository usersRepository;
    private final UserInvitationsRepository userInvitationsRepository;


    public UserInvitations registerInvitation(final InviteUserWithRole inviteUserWithRole) {
        this.validateEmailExistsAndTenantId(inviteUserWithRole.email(), inviteUserWithRole.tenantId());
        this.validateRolePermitted(inviteUserWithRole.role());
        this.validateInviteByExists(inviteUserWithRole.inviteBy());

        final UserInvitations userInvitations = UserInvitations.builder()
                .tenantId(inviteUserWithRole.tenantId())
                .invitedEmail(inviteUserWithRole.email())
                .role(inviteUserWithRole.role())
                .token(UUID.randomUUID())
                .status(UserInvitationStatus.PENDING)
                .invitedBy(inviteUserWithRole.inviteBy())
                .createdAt(OffsetDateTime.now())
                .expiredAt(OffsetDateTime.now().plusDays(EXPIRATION_DAYS))
                .build();

        return this.userInvitationsRepository.save(userInvitations);
    }

    private void validateEmailExistsAndTenantId(final String email, final UUID tenantId) {
        if (this.usersRepository.existsByEmailAndTenantId(email, tenantId)) {
            throw new UserAlreadyExistsInTenantException(email);
        }
    }

    private void validateRolePermitted(Roles rol) {
        Set<Roles> rolesNotPermitted = Set.of(Roles.OWNER, Roles.SUPERADMIN);
        if (rolesNotPermitted.contains(rol)) throw new InvalidRoleException(rol);
    }

    private void validateInviteByExists(final UUID inviteBy) {
        if (!this.usersRepository.existsById(inviteBy)) {
            throw new UserNotExistsException(inviteBy);
        }
    }

    @Builder(toBuilder = true)
    public record InviteUserWithRole(String email, Roles role, UUID tenantId, UUID inviteBy) {
    }
}