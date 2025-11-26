package dev.angelcorzo.neoparking.jpa.parkingtickets.mappers;

import dev.angelcorzo.neoparking.jpa.config.MapperStructConfig;
import dev.angelcorzo.neoparking.jpa.mappers.BaseMapper;
import dev.angelcorzo.neoparking.jpa.parkingtickets.ParkingTicketsData;
import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface ParkingTicketMapper extends BaseMapper<ParkingTickets, ParkingTicketsData> {}
