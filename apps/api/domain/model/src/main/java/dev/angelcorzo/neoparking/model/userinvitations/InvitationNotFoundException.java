package dev.angelcorzo.neoparking.model.userinvitations;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException() {
        super(ErrorMessagesModel.INVITATION_NOT_FOUND.toString());
    }
}
