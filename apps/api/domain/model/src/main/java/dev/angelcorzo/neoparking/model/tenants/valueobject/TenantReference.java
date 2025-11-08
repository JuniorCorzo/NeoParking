package dev.angelcorzo.neoparking.model.tenants.valueobject;

import java.util.UUID;

import dev.angelcorzo.neoparking.model.tenants.Tenants;
import lombok.Builder;

@Builder
public record TenantReference(UUID id, String companyName) {
	public static TenantReference of(Tenants tenant) {
		return new TenantReference(tenant.getId(), tenant.getCompanyName());
	}
}
