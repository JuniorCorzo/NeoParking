package dev.angelcorzo.neoparking.jpa.parkingtickets;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingTicketsRepositoryData extends JpaRepository<ParkingTicketsData, UUID> {}
