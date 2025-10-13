package dev.angelcorzo.neoparking.model.parkinglots;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class ParkingLots {
    private UUID id;
    private String name;
    private Address address;
    private Integer totalSpots;
    private UUID ownerId;
    private UUID tenantId;
    private String timezone;
    private String currency;
    private OperatingHours operatingHours;
}