package dev.angelcorzo.neoparking.model.parkingtickets.gateways;

import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import java.util.Optional;
import java.util.UUID;

public interface ParkingTicketsRepository {
	Optional<ParkingTickets> findById(UUID id);
}
