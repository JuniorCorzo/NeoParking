package dev.angelcorzo.neoparking.api.rates.mappers;

import dev.angelcorzo.neoparking.api.commons.config.MapperStructConfig;
import dev.angelcorzo.neoparking.api.rates.dto.CreateRate;
import dev.angelcorzo.neoparking.api.rates.dto.RatesDTO;
import dev.angelcorzo.neoparking.api.rates.dto.UpdateRate;
import dev.angelcorzo.neoparking.model.rates.Rates;
import dev.angelcorzo.neoparking.usecase.rateconfiguration.RateConfigurationUseCase;
import dev.angelcorzo.neoparking.usecase.updaterate.UpdateRateUseCase;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface RatesMapper {
  RateConfigurationUseCase.CreateTariff toModel(CreateRate dto);
  UpdateRateUseCase.UpdateRate toModel(UpdateRate dto);

  RatesDTO toDTO(Rates model);
}
