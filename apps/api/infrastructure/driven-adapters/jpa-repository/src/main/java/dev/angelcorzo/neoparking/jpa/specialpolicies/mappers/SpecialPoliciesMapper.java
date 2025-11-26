package dev.angelcorzo.neoparking.jpa.specialpolicies.mappers;

import dev.angelcorzo.neoparking.jpa.config.MapperStructConfig;
import dev.angelcorzo.neoparking.jpa.mappers.BaseMapper;
import dev.angelcorzo.neoparking.jpa.specialpolicies.SpecialPoliciesData;
import dev.angelcorzo.neoparking.model.specialpolicies.SpecialPolicies;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface SpecialPoliciesMapper extends BaseMapper<SpecialPolicies, SpecialPoliciesData> {}
