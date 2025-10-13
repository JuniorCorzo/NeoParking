package dev.angelcorzo.neoparking.model.exceptions;

public enum ErrorMessagesModel {
    INVITATION_NOT_FOUND("Invitación no encontrada"),
    INVITATION_EXPIRED("La invitación expiro"),
    INVITATION_ALREADY_ACCEPTED("La invitación fue aceptada el %s"),
    USER_NOT_EXIST_EMAIL("El usuario con email %s no existe"),;

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
