package dev.angelcorzo.nivo.domain.usecase.rate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.angelcorzo.nivo.domain.model.authentication.gateway.AuthenticationContextGateway;
import dev.angelcorzo.nivo.domain.model.parkinglots.ParkingLotPolicy;
import dev.angelcorzo.nivo.domain.model.parkinglots.ParkingLots;
import dev.angelcorzo.nivo.domain.model.parkinglots.gateways.ParkingLotsRepository;
import dev.angelcorzo.nivo.domain.model.parkingtickets.ParkingTicketNotFound;
import dev.angelcorzo.nivo.domain.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.nivo.domain.model.parkingtickets.gateways.ParkingTicketsRepository;
import dev.angelcorzo.nivo.domain.model.rates.enums.TimeUnitsRate;
import dev.angelcorzo.nivo.domain.model.rates.enums.VehicleType;
import dev.angelcorzo.nivo.domain.model.rates.valueobject.RateReference;
import dev.angelcorzo.nivo.domain.model.tenants.Tenants;
import dev.angelcorzo.nivo.domain.usecase.rate.dtos.PriceDetailed;
import dev.angelcorzo.nivo.domain.usecase.rate.engine.PricingEngine;
import dev.angelcorzo.nivo.domain.usecase.rate.engine.stages.*;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CalculateRateUseCase Tests")
class CalculateRateUseCaseTest {

  private ParkingTicketsRepository ticketsRepository;
  private ParkingLotsRepository parkingLotsRepository;
  private AuthenticationContextGateway authGateway;
  private Clock fixedClock;
  private CalculateRateUseCase calculateRateUseCase;

  @BeforeEach
  void setUp() {
    ticketsRepository = mock(ParkingTicketsRepository.class);
    parkingLotsRepository = mock(ParkingLotsRepository.class);
    authGateway = mock(AuthenticationContextGateway.class);
    fixedClock = Clock.fixed(Instant.parse("2026-09-07T12:00:00Z"), ZoneOffset.UTC);

    PricingEngine engine = new PricingEngine(List.of(
        new GracePeriodStage(),
        new BaseRateStage(),
        new SpecialPolicyStage(),
        new SubscriberStage(),
        new StampsStage(),
        new DayCapStage()
    ));

    calculateRateUseCase = new CalculateRateUseCase(
        ticketsRepository,
        parkingLotsRepository,
        authGateway,
        engine,
        fixedClock
    );
  }

  @Test
  @DisplayName("Should return 0 total when stay is within free grace window")
  void shouldReturnZeroForFreeGrace() {
    UUID ticketId = UUID.randomUUID();
    UUID parkingId = UUID.randomUUID();

    ParkingLotPolicy policy = new ParkingLotPolicy(15, BigDecimal.ZERO, new BigDecimal("0.19"));
    ParkingLots parking = ParkingLots.builder().id(parkingId).policy(policy).build();

    RateReference rate = RateReference.builder()
        .pricePerUnit(BigDecimal.valueOf(5000))
        .timeUnit(TimeUnitsRate.HOURS)
        .minChargeTimeMinutes(0)
        .vehicleType(VehicleType.CAR)
        .build();

    ParkingTickets ticket = ParkingTickets.builder()
        .id(ticketId)
        .rate(rate)
        .entryTime(OffsetDateTime.parse("2026-09-07T11:50:00Z")) // 10 min stay <= 15 min grace
        .build();

    when(ticketsRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
    when(parkingLotsRepository.findById(parkingId)).thenReturn(Optional.of(parking));
    when(authGateway.getCurrentTenant()).thenReturn(Tenants.builder().companyName("Central Parking").build());

    PriceDetailed price = calculateRateUseCase.execute(ticketId, parkingId);

    assertThat(price.getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    assertThat(price.getBreakpoint()).isEmpty();
  }

  @Test
  @DisplayName("Should return flat price when stay is within priced grace window")
  void shouldReturnFlatPriceForPricedGrace() {
    UUID ticketId = UUID.randomUUID();
    UUID parkingId = UUID.randomUUID();

    ParkingLotPolicy policy = new ParkingLotPolicy(15, new BigDecimal("500.00"), new BigDecimal("0.19"));
    ParkingLots parking = ParkingLots.builder().id(parkingId).policy(policy).build();

    RateReference rate = RateReference.builder()
        .pricePerUnit(BigDecimal.valueOf(5000))
        .timeUnit(TimeUnitsRate.HOURS)
        .minChargeTimeMinutes(0)
        .vehicleType(VehicleType.CAR)
        .build();

    ParkingTickets ticket = ParkingTickets.builder()
        .id(ticketId)
        .rate(rate)
        .entryTime(OffsetDateTime.parse("2026-09-07T11:50:00Z")) // 10 min stay <= 15 min grace
        .build();

    when(ticketsRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
    when(parkingLotsRepository.findById(parkingId)).thenReturn(Optional.of(parking));
    when(authGateway.getCurrentTenant()).thenReturn(Tenants.builder().companyName("Central Parking").build());

    PriceDetailed price = calculateRateUseCase.execute(ticketId, parkingId);

    assertThat(price.getSubtotal()).isEqualByComparingTo(new BigDecimal("500.00"));
    assertThat(price.getTotal()).isEqualByComparingTo(new BigDecimal("595.00"));
  }

  @Test
  @DisplayName("Should throw ParkingTicketNotFound when ticket does not exist")
  void shouldThrowWhenTicketNotFound() {
    UUID ticketId = UUID.randomUUID();
    UUID parkingId = UUID.randomUUID();

    when(ticketsRepository.findById(ticketId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> calculateRateUseCase.execute(ticketId, parkingId))
        .isInstanceOf(ParkingTicketNotFound.class);
  }

  @Test
  @DisplayName("Should calculate rate with execute(UUID ticketId) single parameter")
  void shouldCalculateRateWithSingleParameter() {
    UUID ticketId = UUID.randomUUID();

    RateReference rate = RateReference.builder()
        .pricePerUnit(BigDecimal.valueOf(5000))
        .timeUnit(TimeUnitsRate.HOURS)
        .minChargeTimeMinutes(0)
        .vehicleType(VehicleType.CAR)
        .build();

    ParkingTickets ticket = ParkingTickets.builder()
        .id(ticketId)
        .rate(rate)
        .entryTime(OffsetDateTime.parse("2026-09-07T10:00:00Z")) // 2 hours stay
        .build();

    when(ticketsRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
    when(authGateway.getCurrentTenant()).thenReturn(Tenants.builder().companyName("Central Parking").build());

    PriceDetailed price = calculateRateUseCase.execute(ticketId);

    assertThat(price).isNotNull();
    assertThat(price.getSubtotal()).isEqualByComparingTo(new BigDecimal("10000.00"));
    assertThat(price.getTotal()).isEqualByComparingTo(new BigDecimal("11900.00"));
  }
}
