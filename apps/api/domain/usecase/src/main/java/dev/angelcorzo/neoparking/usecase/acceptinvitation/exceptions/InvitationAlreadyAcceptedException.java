package dev.angelcorzo.neoparking.usecase.acceptinvitation.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

import java.time.OffsetDateTime;

public class InvitationAlreadyAcceptedException extends RuntimeException {
    public InvitationAlreadyAcceptedException(OffsetDateTime acceptedAt) {
        super(ErrorMessagesModel.INVITATION_ALREADY_ACCEPTED.format(acceptedAt));
    }
}
