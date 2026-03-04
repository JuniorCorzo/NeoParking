package dev.angelcorzo.neoparking.api.payments.dtos.request.check_out.check_out;

import dev.angelcorzo.neoparking.model.payments.enums.PaymentsMethods;
import lombok.Builder;

import java.util.UUID;

@Builder
public record NoSendCheckOutCommand(UUID ticketId, PaymentsMethods paymentMethod)
    implements CheckOutCommand {}
