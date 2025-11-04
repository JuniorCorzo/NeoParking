
package dev.angelcorzo.neoparking.usecase.acceptinvitation.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

/**
 * Thrown when an attempt is made to accept an invitation that has passed its expiration date.
 *
 * <p><strong>Layer:</strong> Application (Use Case)</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 */
public class InvitationExpiredException extends RuntimeException {
    public InvitationExpiredException() {
        super(ErrorMessagesModel.INVITATION_EXPIRED.toString());
    }
}
