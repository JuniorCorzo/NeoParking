package dev.angelcorzo.neoparking.jpa.parkingtickets;

import dev.angelcorzo.neoparking.jpa.helper.AdapterOperations;
import dev.angelcorzo.neoparking.jpa.parkingtickets.mappers.ParkingTicketMapper;
import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.model.parkingtickets.gateways.ParkingTicketsRepository;
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
      ParkingTicketsRepositoryData repository,
      ParkingTicketMapper mapper) {
    super(repository, mapper);
  }
}
