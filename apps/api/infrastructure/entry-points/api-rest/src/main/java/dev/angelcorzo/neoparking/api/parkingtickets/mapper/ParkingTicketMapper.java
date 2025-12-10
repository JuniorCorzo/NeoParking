package dev.angelcorzo.neoparking.api.parkingtickets.mapper;

import dev.angelcorzo.neoparking.api.commons.config.MapperStructConfig;
import dev.angelcorzo.neoparking.api.parkingtickets.dto.CreateTicket;
import dev.angelcorzo.neoparking.api.parkingtickets.dto.ParkingTicketsDTO;
import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.usecase.checkinvehiclewithoureservation.CheckInVehicleWithoutReservationUseCase;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface ParkingTicketMapper {
  CheckInVehicleWithoutReservationUseCase.CreatedParkingTicket toModel(CreateTicket dto);

  ParkingTicketsDTO toDto(ParkingTickets model);
}
