package dev.angelcorzo.neoparking.model.users.gateways;

import dev.angelcorzo.neoparking.model.users.Users;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository {
    Optional<Users> findById(UUID id);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByIdAndTenantId(UUID id, UUID tenantId);
    Long countActiveOwnersByTenantId(UUID tenantId);
    Boolean existsById(UUID id);
    Boolean existsByEmail(String email);
    Boolean existsByEmailAndTenantId(String email, UUID tenantId);
    Users save(Users users);
    Users assignTenant(UUID userId, UUID tenantId);
    void deleteById(UUID id);
}
