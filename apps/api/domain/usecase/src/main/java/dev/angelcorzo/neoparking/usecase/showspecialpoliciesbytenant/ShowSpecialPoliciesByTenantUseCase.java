package dev.angelcorzo.neoparking.usecase.showspecialpoliciesbytenant;

import dev.angelcorzo.neoparking.model.specialpolicies.SpecialPolicies;
import dev.angelcorzo.neoparking.model.specialpolicies.gateways.SpecialPoliciesRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShowSpecialPoliciesByTenantUseCase {
  private final SpecialPoliciesRepository specialPoliciesRepository;

  public List<SpecialPolicies> execute(UUID tenantId) {
    return this.specialPoliciesRepository.findAllByTenantId(tenantId);
  }
}
