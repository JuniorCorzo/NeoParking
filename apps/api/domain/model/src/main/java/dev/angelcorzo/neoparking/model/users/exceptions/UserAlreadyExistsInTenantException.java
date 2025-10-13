package dev.angelcorzo.neoparking.model.users.exceptions;

public class UserAlreadyExistsInTenantException extends RuntimeException {
    public UserAlreadyExistsInTenantException(String email) {
        super(String.format("El usuario %s ya existe en el tenant", email));
    }
}
