package dev.angelcorzo.neoparking.model.payments.valueobject.check_out;

import dev.angelcorzo.neoparking.model.payments.enums.PaymentsMethods;
import java.util.UUID;

import lombok.Builder;

@Builder
public record NoSendCheckOut(UUID ticketId, UUID tenantId, PaymentsMethods paymentMethod)
    implements CheckOut {}
