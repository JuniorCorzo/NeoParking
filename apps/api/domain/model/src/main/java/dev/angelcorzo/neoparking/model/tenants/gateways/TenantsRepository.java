package dev.angelcorzo.neoparking.model.tenants.gateways;

import dev.angelcorzo.neoparking.model.tenants.Tenants;

import java.util.Optional;
import java.util.UUID;

public interface TenantsRepository {
    Optional<Tenants> findById(UUID id);
    Tenants save(Tenants tenants);
    void deleteById(UUID id);
}
