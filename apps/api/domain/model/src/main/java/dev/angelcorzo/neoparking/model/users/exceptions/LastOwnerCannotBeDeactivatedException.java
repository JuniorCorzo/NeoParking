package dev.angelcorzo.neoparking.model.users.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

public class LastOwnerCannotBeDeactivatedException extends RuntimeException {
    public LastOwnerCannotBeDeactivatedException() {
        super(ErrorMessagesModel.LAST_OWNER_CANNOT_BE_DEACTIVATED.toString());
    }
}
