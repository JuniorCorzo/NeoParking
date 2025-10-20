package dev.angelcorzo.neoparking.usecase.acceptinvitation;

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

/**
 * Use case for handling the acceptance of a user invitation.
 *
 * <p>This class orchestrates the business logic required to validate an invitation,
 * create or update a user, and mark the invitation as accepted.</p>
 *
 * <p><strong>Layer:</strong> Application (Use Case)</p>
 * <p><strong>Responsibility:</strong> To execute the business flow for accepting an invitation.</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 */
@RequiredArgsConstructor
public final class AcceptInvitationUseCase {
    private final UserInvitationsRepository invitationsRepository;
    private final UsersRepository usersRepository;

    /**
     * Executes the process of accepting an invitation.
     *
     * @param command The command containing the invitation token and user details.
     * @return The updated {@link UserInvitations} object with an ACCEPTED status.
     * @throws InvitationNotFoundException if the invitation token is invalid.
     * @throws UserAlreadyExistsInTenantException if the user's email already exists in the target tenant.
     * @throws InvitationAlreadyAcceptedException if the invitation has already been accepted.
     * @throws InvitationExpiredException if the invitation has passed its expiration date.
     */
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

    /**
     * Updates a user's details based on the information from the invitation.
     *
     * @param user The user to update.
     * @param invitation The invitation containing the new details.
     */
    private void updateUserFromInvitation(final Users user, final UserInvitations invitation) {
        user.setEmail(invitation.getInvitedEmail());
        user.setTenant(invitation.getTenant());
        user.setRole(invitation.getRole());
    }

    /**
     * Validates the invitation against business rules.
     *
     * @param invitation The invitation to validate.
     */
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


    /**
     * Command object for the {@link AcceptInvitationUseCase}.
     *
     * @param user The user details to be created if the user does not exist.
     * @param token The unique invitation token.
     */
    @Builder(toBuilder = true)
    public record Accept(Users user, UUID token) {
    }
}
