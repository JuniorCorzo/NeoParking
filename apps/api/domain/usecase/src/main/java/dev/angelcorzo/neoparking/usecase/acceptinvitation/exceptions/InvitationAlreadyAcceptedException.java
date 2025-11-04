package dev.angelcorzo.neoparking.usecase.acceptinvitation.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

import java.time.OffsetDateTime;

/**
 * Thrown when an attempt is made to accept an invitation that has already been accepted.
 *
 * <p><strong>Layer:</strong> Application (Use Case)</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 */
public class InvitationAlreadyAcceptedException extends RuntimeException {
    public InvitationAlreadyAcceptedException(OffsetDateTime acceptedAt) {
        super(ErrorMessagesModel.INVITATION_ALREADY_ACCEPTED.format(acceptedAt));
    }
}
