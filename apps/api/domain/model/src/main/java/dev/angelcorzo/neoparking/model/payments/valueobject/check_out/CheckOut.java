package dev.angelcorzo.neoparking.model.payments.valueobject.check_out;

import dev.angelcorzo.neoparking.model.payments.enums.PaymentsMethods;

import java.util.UUID;

public sealed interface CheckOut permits EmailCheckOut, SMSCheckOut, NoSendCheckOut {
  UUID ticketId();

  UUID tenantId();

  PaymentsMethods paymentMethod();
}
