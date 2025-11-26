package dev.angelcorzo.neoparking.api.specialpolicies.mapper;

import dev.angelcorzo.neoparking.api.commons.config.MapperStructConfig;
import dev.angelcorzo.neoparking.api.specialpolicies.dto.CreateSpecialPolicies;
import dev.angelcorzo.neoparking.api.specialpolicies.dto.SpecialPoliciesDTO;
import dev.angelcorzo.neoparking.model.specialpolicies.SpecialPolicies;
import dev.angelcorzo.neoparking.usecase.createspecialpolicy.CreateSpecialPolicyUseCase;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface SpecialPoliciesMapper {
  CreateSpecialPolicyUseCase.CreateSpecialPolicyCommand toModel(CreateSpecialPolicies dto);
  SpecialPoliciesDTO toDto(SpecialPolicies model);

}
