package dev.angelcorzo.neoparking.model.parkingtickets.gateways;

import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.model.parkingtickets.enums.ParkingTicketStatus;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ParkingTicketsRepository {
  Optional<ParkingTickets> findById(UUID id);

  Optional<ParkingTickets> findByTenantIdAndId(UUID tenantId, UUID id);

  ParkingTickets getReferenceById(UUID id);

  ParkingTickets save(ParkingTickets parkingTickets);

  ParkingTickets prepareCheckout(UUID ticketId, BigDecimal amountToCharge);

  ParkingTickets changeStatus(UUID ticketId, ParkingTicketStatus status);

  ParkingTickets closeTicket(UUID ticketId, BigDecimal amountPaid);
}
