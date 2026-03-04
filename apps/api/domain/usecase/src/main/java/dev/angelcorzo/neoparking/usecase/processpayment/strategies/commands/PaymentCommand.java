package dev.angelcorzo.neoparking.usecase.processpayment.strategies.commands;

import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.model.payments.valueobject.check_out.CheckOut;
import dev.angelcorzo.neoparking.usecase.calculaterate.dtos.PriceDetailed;

public record PaymentCommand(ParkingTickets ticket, PriceDetailed amounts, CheckOut checkOut) {
  public static PaymentCommand of(ParkingTickets ticket, PriceDetailed amounts, CheckOut checkOut) {
    return new PaymentCommand(ticket, amounts, checkOut);
  }
}
