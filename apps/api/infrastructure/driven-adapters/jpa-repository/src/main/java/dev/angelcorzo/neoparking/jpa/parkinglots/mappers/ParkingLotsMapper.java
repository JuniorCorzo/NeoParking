package dev.angelcorzo.neoparking.jpa.parkinglots.mappers;

import dev.angelcorzo.neoparking.jpa.config.MapperStructConfig;
import dev.angelcorzo.neoparking.jpa.mappers.BaseMapper;
import dev.angelcorzo.neoparking.jpa.parkinglots.ParkingLotsData;
import dev.angelcorzo.neoparking.model.parkinglots.ParkingLots;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface ParkingLotsMapper extends BaseMapper<ParkingLots, ParkingLotsData> {}
