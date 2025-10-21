package dev.angelcorzo.neoparking.api.tenants.enums;

public enum TenantsMessages {
    TENANT_CREATED_SUCCESSFULLY("El usuario fue creado con éxito"),
    ;

    private final String template;
    TenantsMessages(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
    @Override
    public String toString() {
        return template;
    }
}
