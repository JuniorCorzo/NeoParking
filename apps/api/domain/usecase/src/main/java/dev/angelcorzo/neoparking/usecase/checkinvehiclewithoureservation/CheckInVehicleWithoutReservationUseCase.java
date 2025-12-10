package dev.angelcorzo.neoparking.usecase.checkinvehiclewithoureservation;

import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.model.parkingtickets.gateways.ParkingTicketsRepository;
import dev.angelcorzo.neoparking.model.rates.exceptions.RateNotFoundException;
import dev.angelcorzo.neoparking.model.rates.gateways.RatesRepository;
import dev.angelcorzo.neoparking.model.rates.valueobject.RateReference;
import dev.angelcorzo.neoparking.model.slots.excetions.SlotNotFoundException;
import dev.angelcorzo.neoparking.model.slots.gateways.SlotsRepository;
import dev.angelcorzo.neoparking.model.slots.valueobject.SlotsReference;
import dev.angelcorzo.neoparking.model.tenants.exceptions.TenantNotExistsException;
import dev.angelcorzo.neoparking.model.tenants.gateways.TenantsRepository;
import dev.angelcorzo.neoparking.model.tenants.valueobject.TenantReference;
import dev.angelcorzo.neoparking.model.users.exceptions.UserNotExistsException;
import dev.angelcorzo.neoparking.model.users.gateways.UsersRepository;
import dev.angelcorzo.neoparking.model.users.valueobject.UserReference;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CheckInVehicleWithoutReservationUseCase {
  private final ParkingTicketsRepository parkingTicketsRepository;
  private final TenantsRepository tenantsRepository;
  private final UsersRepository usersRepository;
  private final SlotsRepository slotsRepository;
  private final RatesRepository ratesRepository;

  public ParkingTickets execute(CreatedParkingTicket ticket) {
    this.validate(ticket);

    final ParkingTickets parkingTicket =
        ParkingTickets.builder()
            .slot(SlotsReference.of(this.slotsRepository.getReferenceById(ticket.slotId())))
            .tenant(TenantReference.of(this.tenantsRepository.getReferenceById(ticket.tenantId())))
            .user(UserReference.of(this.usersRepository.getReferenceById(ticket.userId())))
            .rate(RateReference.of(this.ratesRepository.getReferenceById(ticket.rateId())))
            .entryTime(OffsetDateTime.now())
            .licensePlate(ticket.plate())
            .build();

    return this.parkingTicketsRepository.save(parkingTicket);
  }

  private void validate(CreatedParkingTicket ticket) {
    if (!this.slotsRepository.existsById(ticket.slotId()))
      throw new SlotNotFoundException(ticket.slotId());

    if (ticket.userId() != null && !this.usersRepository.existsById(ticket.userId()))
      throw new UserNotExistsException(ticket.userId());

    if (!this.tenantsRepository.existsById(ticket.tenantId()))
      throw new TenantNotExistsException(ticket.tenantId());
    if (!ratesRepository.existsById(ticket.rateId()))
      throw new RateNotFoundException(ticket.rateId());
  }

  @Builder(toBuilder = true)
  public record CreatedParkingTicket(
      UUID slotId, UUID tenantId, UUID userId, UUID rateId, String plate) {}
}
