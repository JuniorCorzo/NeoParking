package dev.angelcorzo.neoparking.jpa.parkinglots;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotsRepositoryData extends JpaRepository<ParkingLotsData, UUID> {
  List<ParkingLotsData> findAllByOwnerId(UUID ownerId);

  List<ParkingLotsData> findAllByTenantId(UUID tenantId);
}
