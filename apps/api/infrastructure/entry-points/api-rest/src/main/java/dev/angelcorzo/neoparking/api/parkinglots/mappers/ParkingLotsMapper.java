package dev.angelcorzo.neoparking.api.parkinglots.mappers;

import dev.angelcorzo.neoparking.api.commons.config.MapperStructConfig;
import dev.angelcorzo.neoparking.api.parkinglots.dto.ParkingLotsResponse;
import dev.angelcorzo.neoparking.api.parkinglots.dto.UpsertParkingLotsRequest;
import dev.angelcorzo.neoparking.model.parkinglots.ParkingLots;
import dev.angelcorzo.neoparking.model.parkinglots.dto.UpsertParkingLotsDTO;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface ParkingLotsMapper {
  UpsertParkingLotsDTO toModel(UpsertParkingLotsRequest dto);

  ParkingLotsResponse toDTO(ParkingLots model);
}
