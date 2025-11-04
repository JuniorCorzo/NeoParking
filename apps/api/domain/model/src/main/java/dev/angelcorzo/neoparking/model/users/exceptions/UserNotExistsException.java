package dev.angelcorzo.neoparking.model.users.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

import java.util.UUID;

/**
 * Thrown when a user cannot be found by their identifier (ID or email).
 *
 * <p><strong>Layer:</strong> Domain</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 */
public class UserNotExistsException extends RuntimeException {
    public UserNotExistsException(UUID id) {
        super(String.format("El usuario con ID %s no existe", id));
    }

    public UserNotExistsException(String email) {
        super(ErrorMessagesModel.USER_NOT_EXIST_EMAIL.format(email));
    }

}
