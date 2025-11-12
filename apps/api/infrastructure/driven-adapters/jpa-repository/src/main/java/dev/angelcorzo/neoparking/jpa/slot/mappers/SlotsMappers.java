package dev.angelcorzo.neoparking.jpa.slot.mappers;

import dev.angelcorzo.neoparking.jpa.config.MapperStructConfig;
import dev.angelcorzo.neoparking.jpa.mappers.BaseMapper;
import dev.angelcorzo.neoparking.jpa.slot.SlotsData;
import dev.angelcorzo.neoparking.model.slots.Slots;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface SlotsMappers extends BaseMapper<Slots, SlotsData> {}
