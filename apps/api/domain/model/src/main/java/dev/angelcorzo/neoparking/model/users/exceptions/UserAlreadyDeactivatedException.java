package dev.angelcorzo.neoparking.model.users.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

import java.util.UUID;

public class UserAlreadyDeactivatedException extends RuntimeException {
    public UserAlreadyDeactivatedException(UUID id) {
        super(ErrorMessagesModel.USER_ALREADY_DEACTIVATED.format(id));
    }
}
