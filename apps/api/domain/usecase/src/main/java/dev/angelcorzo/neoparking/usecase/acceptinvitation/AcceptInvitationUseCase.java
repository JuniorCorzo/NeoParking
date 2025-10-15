package dev.angelcorzo.neoparking.usecase.acceptinvitation;

import dev.angelcorzo.neoparking.model.tenants.Tenants;
import dev.angelcorzo.neoparking.model.tenants.gateways.TenantsRepository;
import dev.angelcorzo.neoparking.model.userinvitations.InvitationNotFoundException;
import dev.angelcorzo.neoparking.model.userinvitations.UserInvitationStatus;
import dev.angelcorzo.neoparking.model.userinvitations.UserInvitations;
import dev.angelcorzo.neoparking.model.userinvitations.gateways.UserInvitationsRepository;
import dev.angelcorzo.neoparking.model.users.Users;
import dev.angelcorzo.neoparking.model.users.exceptions.UserAlreadyExistsInTenantException;
import dev.angelcorzo.neoparking.model.users.gateways.UsersRepository;
import dev.angelcorzo.neoparking.usecase.acceptinvitation.exceptions.InvitationAlreadyAcceptedException;
import dev.angelcorzo.neoparking.usecase.acceptinvitation.exceptions.InvitationExpiredException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public final class AcceptInvitationUseCase {
    private final UserInvitationsRepository invitationsRepository;
    private final UsersRepository usersRepository;

    public UserInvitations accept(final Accept command) {
        final UserInvitations invitation = this.invitationsRepository.findByToken(command.token)
                .orElseThrow(InvitationNotFoundException::new);

        this.validateInvitation(invitation);

        final Users user = usersRepository.findByEmail(invitation.getInvitedEmail())
                .orElse(command.user);

        this.updateUserFromInvitation(user, invitation);
        this.usersRepository.save(user);

        return this.invitationsRepository.acceptedInvitation(invitation.getId());
    }

    private void updateUserFromInvitation(final Users user, final UserInvitations invitation) {
        user.setEmail(invitation.getInvitedEmail());
        user.setTenant(invitation.getTenant());
        user.setRole(invitation.getRole());
    }

    private void validateInvitation(final UserInvitations invitation) {
        if (this.usersRepository.existsByEmailAndTenantId(invitation.getInvitedEmail(), invitation.getTenant().getId())) {
            throw new UserAlreadyExistsInTenantException(invitation.getInvitedEmail());
        }
        if (invitation.getStatus() == UserInvitationStatus.ACCEPTED) {
            throw new InvitationAlreadyAcceptedException(invitation.getAcceptedAt());
        }
        if (OffsetDateTime.now().isAfter(invitation.getExpiredAt())) {
            throw new InvitationExpiredException();
        }
    }


    @Builder(toBuilder = true)
    public record Accept(Users user, UUID token) {
    }
}
