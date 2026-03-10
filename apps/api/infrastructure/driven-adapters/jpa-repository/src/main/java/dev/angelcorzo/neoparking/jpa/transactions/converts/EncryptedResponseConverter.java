package dev.angelcorzo.neoparking.jpa.transactions.converts;

import dev.angelcorzo.neoparking.model.commons.encryption.exceptions.EncryptionException;
import dev.angelcorzo.neoparking.model.commons.encryption.gateways.EncryptionGateway;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Converter
@Component
@Slf4j
public class EncryptedResponseConverter implements AttributeConverter<String, Object> {
    private final EncryptionGateway encryptionGateway;

    public EncryptedResponseConverter(EncryptionGateway encryptionGateway) {
        log.info("Initializing {}", getClass().getSimpleName());
        this.encryptionGateway = encryptionGateway;
    }

    @Override
    public Object convertToDatabaseColumn(String attribute) {
        return this.encryptionGateway.decrypt(attribute, Object.class).orElseThrow(EncryptionException::new);
    }

    @Override
    public String convertToEntityAttribute(Object dbData) {
        return this.encryptionGateway.encrypt(dbData).orElseThrow(EncryptionException::new);
    }
}
