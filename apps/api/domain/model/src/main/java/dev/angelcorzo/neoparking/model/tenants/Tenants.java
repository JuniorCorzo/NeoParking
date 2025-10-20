package dev.angelcorzo.neoparking.model.tenants;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Represents a Tenant, which is a top-level customer entity in this multi-tenant system.
 *
 * <p>A Tenant typically corresponds to a company or organization that subscribes to the NeoParking service.
 * All other resources, such as Users and Parking Lots, are scoped under a Tenant.</p>
 *
 * <p><strong>Layer:</strong> Domain</p>
 * <p><strong>Responsibility:</strong> To hold the state and invariants of a Tenant.</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
public class Tenants {
    /**
     * The unique identifier for the Tenant.
     * It is a UUID generated at the time of creation.
     */
    private UUID id;
    /**
     * The legal or commercial name of the company representing the Tenant.
     */
    private String companyName;
    /**
     * The timestamp when the Tenant was created in the system.
     */
    private OffsetDateTime createdAt;
    /**
     * The timestamp of the last update to the Tenant's information.
     */
    private OffsetDateTime updatedAt;
    /**
     * The timestamp when the Tenant was soft-deleted. A non-null value indicates the Tenant is considered deleted.
     */
    private OffsetDateTime deletedAt;
}