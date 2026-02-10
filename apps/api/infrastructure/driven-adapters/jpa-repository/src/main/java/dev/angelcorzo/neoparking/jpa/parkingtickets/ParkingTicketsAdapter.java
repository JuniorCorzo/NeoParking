package dev.angelcorzo.neoparking.jpa.parkingtickets;

import dev.angelcorzo.neoparking.jpa.helper.AdapterOperations;
import dev.angelcorzo.neoparking.jpa.parkingtickets.mappers.ParkingTicketMapper;
import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.model.parkingtickets.enums.ParkingTicketStatus;
import dev.angelcorzo.neoparking.model.parkingtickets.gateways.ParkingTicketsRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ParkingTicketsAdapter
    extends AdapterOperations<
        ParkingTickets, ParkingTicketsData, UUID, ParkingTicketsRepositoryData>
    implements ParkingTicketsRepository {

  /**
   * Constructor for AdapterOperations.
   *
   * @param repository The JPA repository instance.
   * @param mapper The mapper for converting between domain and data entities.
   */
  protected ParkingTicketsAdapter(
      ParkingTicketsRepositoryData repository, ParkingTicketMapper mapper) {
    super(repository, mapper);
  }

  @Override
  public Optional<ParkingTickets> findByTenantIdAndId(UUID tenantId, UUID id) {

    return super.repository.findByTenant_IdAndId(tenantId, id).map(super::toEntity);
  }

  @Override
  public ParkingTickets getReferenceById(UUID id) {
    return super.mapper.toEntity(super.repository.getReferenceById(id));
  }

  @Override
  public ParkingTickets prepareCheckout(UUID ticketId, BigDecimal amountToCharge) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'prepareCheckout'");
  }

  @Override
  public ParkingTickets changeStatus(UUID ticketId, ParkingTicketStatus status) {
    super.repository.changeStatus(ticketId, status);

    return super.repository.findById(ticketId).map(super::toEntity).orElse(null);
  }

  @Override
  public ParkingTickets closeTicket(UUID ticketId, BigDecimal amountPaid) {
    super.repository.closeTicket(ticketId, amountPaid);

    return super.repository.findById(ticketId).map(super::toEntity).orElse(null);
  }
}
