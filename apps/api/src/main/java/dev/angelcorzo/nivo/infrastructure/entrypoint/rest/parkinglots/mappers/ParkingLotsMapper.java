package dev.angelcorzo.nivo.infrastructure.entrypoint.rest.parkinglots.mappers;

import dev.angelcorzo.nivo.infrastructure.entrypoint.rest.commons.config.MapperStructConfig;
import dev.angelcorzo.nivo.infrastructure.entrypoint.rest.parkinglots.dto.ParkingLotListItemResponse;
import dev.angelcorzo.nivo.infrastructure.entrypoint.rest.parkinglots.dto.ParkingLotsResponse;
import dev.angelcorzo.nivo.infrastructure.entrypoint.rest.parkinglots.dto.UpsertParkingLotsRequest;
import dev.angelcorzo.nivo.domain.model.parkinglots.ParkingLotListItem;
import dev.angelcorzo.nivo.domain.model.parkinglots.ParkingLots;
import dev.angelcorzo.nivo.domain.model.parkinglots.dto.UpsertParkingLotsDTO;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface ParkingLotsMapper {
  UpsertParkingLotsDTO toModel(UpsertParkingLotsRequest dto);

  ParkingLotsResponse toDTO(ParkingLots model);

  ParkingLotListItemResponse toListItemResponse(ParkingLotListItem model);
}
