package dev.angelcorzo.neoparking.model.users.exceptions;

public final class EmailAlreadyExistsException extends IllegalArgumentException {
    public EmailAlreadyExistsException(final String email) {
        super(String.format("El email %s ya existe", email));
    }
}
