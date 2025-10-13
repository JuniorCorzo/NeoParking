package dev.angelcorzo.neoparking.model.parkinglots.gateways;

import dev.angelcorzo.neoparking.model.parkinglots.ParkingLots;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParkingLotsRepository {
    List<ParkingLots> findByTenantId(UUID tenantId);
    List<ParkingLots> findByOwnerId(UUID ownerId);
    Optional<ParkingLots> save(ParkingLots parkingLots);
    Optional<ParkingLots> delete(ParkingLots parkingLots);
}
