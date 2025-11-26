package dev.angelcorzo.neoparking.jpa.specialpolicies;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecialRepositoryData extends JpaRepository<SpecialPoliciesData, UUID> {}
