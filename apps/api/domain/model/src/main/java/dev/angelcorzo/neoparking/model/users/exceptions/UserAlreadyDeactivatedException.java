package dev.angelcorzo.neoparking.model.users.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

import java.util.UUID;

/**
 * Thrown when an operation is attempted on a user who is already in a deactivated state.
 *
 * <p><strong>Layer:</strong> Domain</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 */
public class UserAlreadyDeactivatedException extends RuntimeException {
    public UserAlreadyDeactivatedException(UUID id) {
        super(ErrorMessagesModel.USER_ALREADY_DEACTIVATED.format(id));
    }
}
