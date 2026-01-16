package dev.angelcorzo.neoparking.usecase.calculaterate;

import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTicketNotFound;
import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.model.parkingtickets.gateways.ParkingTicketsRepository;
import dev.angelcorzo.neoparking.model.rates.valueobject.RateReference;
import dev.angelcorzo.neoparking.usecase.calculaterate.decorator.RateBaseDecorator;
import dev.angelcorzo.neoparking.usecase.calculaterate.decorator.RateComponent;
import dev.angelcorzo.neoparking.usecase.calculaterate.decorator.RateWithSpecialPolicyDecorator;
import dev.angelcorzo.neoparking.usecase.calculaterate.dtos.ItemPriceDTO;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CalculateRateUseCase {
  private final ParkingTicketsRepository parkingTicketsRepository;
  private RateComponent rateComponent;

  public LinkedList<ItemPriceDTO> execute(UUID ticketId) {
    final ParkingTickets parkingTicket =
        this.parkingTicketsRepository
            .findById(ticketId)
            .orElseThrow(() -> new ParkingTicketNotFound(ticketId));

    final RateReference rate = parkingTicket.getRate();

    this.rateComponent =
        new RateBaseDecorator(
            rate.name(),
            rate.timeUnit(),
            Duration.ofMinutes(Long.parseLong(rate.minChargeTimeMinutes())),
            parkingTicket.getEntryTime(),
            rate.pricePerUnit());

    if (rate.hasSpecialPolicy())
      this.rateComponent =
          new RateWithSpecialPolicyDecorator(this.rateComponent, rate.specialPolicy());

    this.addTotalItemizedPrice(this.rateComponent.getPrice());

    return this.rateComponent.getItemizedPrices();
  }

  private void addTotalItemizedPrice(BigDecimal total) {
    this.rateComponent.getItemizedPrices().add(ItemPriceDTO.of("Total", total));
  }
}
