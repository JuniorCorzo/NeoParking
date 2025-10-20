package dev.angelcorzo.neoparking.model.parkinglots.gateways;

import dev.angelcorzo.neoparking.model.parkinglots.ParkingLots;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Defines the contract for persistence operations on ParkingLots entities.
 *
 * <p>This interface acts as a Port in the Domain layer, abstracting the data storage
 * mechanism. The implementation will be provided by an adapter in the Infrastructure layer.</p>
 *
 * <p><strong>Layer:</strong> Domain (Gateway)</p>
 * <p><strong>Responsibility:</strong> To declare the necessary methods for managing
 * the lifecycle of {@link ParkingLots} aggregates.</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 */
public interface ParkingLotsRepository {

    /**
     * Finds all parking lots associated with a specific tenant.
     *
     * @param tenantId The unique identifier of the tenant.
     * @return A list of {@link ParkingLots} belonging to the tenant. Can be empty if none are found.
     */
    List<ParkingLots> findByTenantId(UUID tenantId);

    /**
     * Finds all parking lots owned by a specific user.
     *
     * @param ownerId The unique identifier of the owner user.
     * @return A list of {@link ParkingLots} owned by the user. Can be empty if none are found.
     */
    List<ParkingLots> findByOwnerId(UUID ownerId);

    /**
     * Saves (creates or updates) a parking lot entity.
     *
     * @param parkingLots The {@link ParkingLots} entity to save.
     * @return An {@link Optional} containing the saved entity, or empty if the operation fails.
     */
    Optional<ParkingLots> save(ParkingLots parkingLots);

    /**
     * Deletes a parking lot entity.
     * This could be a soft or hard delete depending on the implementation.
     *
     * @param parkingLots The {@link ParkingLots} entity to delete.
     * @return An {@link Optional} containing the deleted entity, or empty if the operation fails.
     */
    Optional<ParkingLots> delete(ParkingLots parkingLots);
}
