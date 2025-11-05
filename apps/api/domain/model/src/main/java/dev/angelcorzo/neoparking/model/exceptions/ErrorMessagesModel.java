package dev.angelcorzo.neoparking.model.exceptions;

/**
 * Enum to standardize error message templates across the domain layer.
 *
 * <p><strong>Layer:</strong> Domain</p>
 * <p><strong>Responsibility:</strong> To provide a single source of truth for error message formats.</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 */
public enum ErrorMessagesModel {
    INVITATION_NOT_FOUND("Invitación no encontrada"),
    INVITATION_EXPIRED("La invitación expiro"),
    INVITATION_ALREADY_ACCEPTED("La invitación fue aceptada el %s"),
    USER_NOT_EXIST_EMAIL("El usuario con email %s no existe"),
    USER_NOT_EXIST_ID("El usuario con ID %s no existe"),
    USER_NOT_EXIST_IN_TENANT("El usuario no esta asociado al tenant"),
    USER_ALREADY_DEACTIVATED("El usuario %s ya fue desactivado"),
    USER_BAD_CREDENTIALS("Credenciales incorrectas"),
    LAST_OWNER_CANNOT_BE_DEACTIVATED("El ultimo dueño no puede ser desactivado"),
    TENANT_NOT_EXISTS("El inquilino con ID %s no esta registrado"),
    PARKING_NOT_EXISTS("El parking con ID %s no esta registrado");

    private final String template;
    ErrorMessagesModel(String template) {
        this.template = template;
    }

    public String format(Object... values) {
        return String.format(template, values);
    }

    @Override
    public String toString() {
        return template;
    }
}
