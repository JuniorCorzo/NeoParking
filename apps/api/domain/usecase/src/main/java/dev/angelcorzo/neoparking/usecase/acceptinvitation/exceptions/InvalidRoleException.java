package dev.angelcorzo.neoparking.usecase.acceptinvitation.exceptions;

import dev.angelcorzo.neoparking.model.users.enums.Roles;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(Roles rol) {
        super(String.format("El rol %s no esta permitido en este tipo de invitaciones", rol.toString().toLowerCase()));
    }
}
