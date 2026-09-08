package dev.angelcorzo.nivo.domain.usecase.rate;

import dev.angelcorzo.nivo.domain.model.authentication.gateway.AuthenticationContextGateway;
import dev.angelcorzo.nivo.domain.model.parkingtickets.ParkingTicketNotFound;
import dev.angelcorzo.nivo.domain.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.nivo.domain.model.parkingtickets.gateways.ParkingTicketsRepository;
import dev.angelcorzo.nivo.domain.model.rates.valueobject.RateReference;
import dev.angelcorzo.nivo.domain.model.tenants.exceptions.TenantNotExistsException;
import dev.angelcorzo.nivo.domain.model.tenants.gateways.TenantsRepository;
import dev.angelcorzo.nivo.domain.usecase.rate.decorator.RateBaseDecorator;
import dev.angelcorzo.nivo.domain.usecase.rate.decorator.RateComponent;
import dev.angelcorzo.nivo.domain.usecase.rate.decorator.RateWithSpecialPolicyDecorator;
import dev.angelcorzo.nivo.domain.usecase.rate.dtos.PriceDetailed;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CalculateRateUseCase {
  private final TenantsRepository tenantsRepository;
  private final ParkingTicketsRepository parkingTicketsRepository;
  private final AuthenticationContextGateway authenticationContextGateway;

  public PriceDetailed execute(UUID ticketId) {
    final String tenantName = this.getTenantName();

    final ParkingTickets parkingTicket =
        this.parkingTicketsRepository
            .findById(ticketId)
            .orElseThrow(() -> new ParkingTicketNotFound(ticketId));

    final RateReference rate = parkingTicket.getRate();

    RateComponent rateComponent =
        new RateBaseDecorator(
            tenantName, BigDecimal.valueOf(0.19), rate, parkingTicket.getEntryTime());

    if (rate.hasSpecialPolicy())
      rateComponent = new RateWithSpecialPolicyDecorator(rateComponent, rate.specialPolicy());

    return rateComponent.getItemizedPrices();
  }

  private String getTenantName() {
    final UUID tenantId = this.authenticationContextGateway.getCurrentTenantId();

    return this.tenantsRepository
        .findById(tenantId)
        .orElseThrow(() -> new TenantNotExistsException(tenantId))
        .getCompanyName();
  }
}
