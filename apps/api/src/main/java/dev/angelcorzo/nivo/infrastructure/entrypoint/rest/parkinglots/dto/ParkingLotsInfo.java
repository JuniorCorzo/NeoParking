package dev.angelcorzo.nivo.infrastructure.entrypoint.rest.parkinglots.dto;


import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record ParkingLotsInfo(
    UUID id,
    String name,
    AddressDTO address,
    CoordinatesDTO coordinates,
    String timezone,
    String currency,
    OperatingHoursDTO operatingHours) {}
