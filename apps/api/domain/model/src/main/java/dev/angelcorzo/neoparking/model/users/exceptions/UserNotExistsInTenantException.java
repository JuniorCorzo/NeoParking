package dev.angelcorzo.neoparking.model.users.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

public class UserNotExistsInTenantException extends RuntimeException {
    public UserNotExistsInTenantException() {
        super(ErrorMessagesModel.USER_NOT_EXIST_IN_TENANT.toString());
    }
}
