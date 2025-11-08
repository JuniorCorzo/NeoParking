package dev.angelcorzo.neoparking.model.parkinglots.dto;

import dev.angelcorzo.neoparking.model.parkinglots.Address;
import dev.angelcorzo.neoparking.model.parkinglots.OperatingHours;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record UpsertParkingLotsDTO(
    UUID id,
    String name,
    Address address,
    UUID tenantId,
    String timezone,
    String currency,
    OperatingHours operatingHours) {}
