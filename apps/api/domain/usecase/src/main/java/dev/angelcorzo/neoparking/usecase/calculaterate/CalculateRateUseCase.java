package dev.angelcorzo.neoparking.usecase.calculaterate;

import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTicketNotFound;
import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.model.parkingtickets.gateways.ParkingTicketsRepository;
import dev.angelcorzo.neoparking.model.rates.valueobject.RateReference;
import dev.angelcorzo.neoparking.usecase.calculaterate.decorator.RateBaseDecorator;
import dev.angelcorzo.neoparking.usecase.calculaterate.decorator.RateComponent;
import dev.angelcorzo.neoparking.usecase.calculaterate.decorator.RateWithSpecialPolicyDecorator;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CalculateRateUseCase {
  private final ParkingTicketsRepository parkingTicketsRepository;

  public BigDecimal execute(UUID ticketId) {
    final ParkingTickets parkingTicket =
        this.parkingTicketsRepository
            .findById(ticketId)
            .orElseThrow(() -> new ParkingTicketNotFound(ticketId));

    final RateReference rate = parkingTicket.getRate();

    RateComponent calculateRate =
        new RateBaseDecorator(rate.pricePerUnit(), rate.timeUnit(), parkingTicket.getEntryTime());
    if (rate.hasSpecialPolicy())
      calculateRate = new RateWithSpecialPolicyDecorator(calculateRate, rate.specialPolicy());

    return calculateRate.getPrice();
  }
}
