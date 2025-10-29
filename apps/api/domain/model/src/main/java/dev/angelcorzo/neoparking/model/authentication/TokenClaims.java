package dev.angelcorzo.neoparking.model.authentication;

import dev.angelcorzo.neoparking.model.users.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Represents the claims extracted from a parsed JWT (JSON Web Token).
 * <p>
 * This class is a domain model that holds the validated and structured information
 * from a token. It is used throughout the application, particularly in authorization
 * logic, to identify the user, their tenant, role, and other relevant security attributes.
 * </p>
 * <p>
 * An instance of this class is typically created by a use case after a token has been
 * successfully parsed and its signature and claims have been verified.
 * </p>
 *
 * @author Angel Corzo
 * @version 1.0
 * @since 2025-10-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TokenClaims {

    /**
     * The unique identifier of the token itself (JWT ID or 'jti').
     */
    private UUID id;

    /**
     * The unique identifier of the user (subject or 'sub').
     */
    private UUID userId;

    /**
     * The unique identifier of the tenant to which the user belongs.
     */
    private UUID tenantId;

    /**
     * The full name of the user.
     */
    private String fullName;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The role of the user within the tenant.
     */
    private Roles role;

    /**
     * A list of parking lot IDs that the user has access to.
     * This can be used for fine-grained authorization.
     */
    private List<UUID> parkingLotIds;
}
