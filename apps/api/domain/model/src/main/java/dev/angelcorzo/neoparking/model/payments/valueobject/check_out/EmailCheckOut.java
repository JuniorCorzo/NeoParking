package dev.angelcorzo.neoparking.model.payments.valueobject.check_out;

import dev.angelcorzo.neoparking.model.payments.enums.PaymentsMethods;
import lombok.Builder;

import java.util.UUID;

@Builder
public record EmailCheckOut(
		UUID ticketId,
		UUID tenantId,
		PaymentsMethods paymentMethod,
		String email
		) implements CheckOut {}
