package dev.angelcorzo.neoparking.usecase.acceptinvitation.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

public class InvitationExpiredException extends RuntimeException {
    public InvitationExpiredException() {
        super(ErrorMessagesModel.INVITATION_EXPIRED.toString());
    }
}
