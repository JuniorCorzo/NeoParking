package dev.angelcorzo.neoparking.usecase.checkoutvehicle;

import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.model.parkingtickets.enums.ParkingTicketStatus;
import dev.angelcorzo.neoparking.model.parkingtickets.gateways.ParkingTicketsRepository;
import dev.angelcorzo.neoparking.model.payments.Payments;
import dev.angelcorzo.neoparking.model.payments.exceptions.PaymentError;
import dev.angelcorzo.neoparking.model.payments.exceptions.ProcessPaymentException;
import dev.angelcorzo.neoparking.model.payments.valueobject.check_out.CheckOut;
import dev.angelcorzo.neoparking.usecase.calculaterate.CalculateRateUseCase;
import dev.angelcorzo.neoparking.usecase.calculaterate.dtos.PriceDetailed;
import dev.angelcorzo.neoparking.usecase.processpayment.ProcessPaymentUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CheckOutVehicleUseCase {
  private final ProcessPaymentUseCase processPayment;
  private final CalculateRateUseCase calculateRateUseCase;
  private final ParkingTicketsRepository parkingTicketsRepository;

  public Payments execute(CheckOut command) {
    final UUID ticketId = command.ticketId();
    final ParkingTickets ticket =
        this.parkingTicketsRepository
            .findById(ticketId)
            .orElseThrow(
                () -> new ProcessPaymentException(new PaymentError.TicketNotFound(ticketId)));

    if (ticket.getStatus() == ParkingTicketStatus.CLOSED) {
      throw new ProcessPaymentException(new PaymentError.Duplicate(ticketId));
    }

    final PriceDetailed amounts = this.calculateRateUseCase.execute(command.ticketId());
    return this.processPayment.execute(ticket, amounts, command);
  }
}
