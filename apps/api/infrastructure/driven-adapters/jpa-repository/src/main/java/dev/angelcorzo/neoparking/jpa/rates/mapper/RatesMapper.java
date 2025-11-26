package dev.angelcorzo.neoparking.jpa.rates.mapper;

import dev.angelcorzo.neoparking.jpa.config.MapperStructConfig;
import dev.angelcorzo.neoparking.jpa.mappers.BaseMapper;
import dev.angelcorzo.neoparking.jpa.rates.RateData;
import dev.angelcorzo.neoparking.model.rates.Rates;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface RatesMapper extends BaseMapper<Rates, RateData> {}
