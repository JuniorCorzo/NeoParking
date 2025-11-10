package dev.angelcorzo.neoparking.model.parkinglots.valueobject;

import dev.angelcorzo.neoparking.model.parkinglots.Address;
import dev.angelcorzo.neoparking.model.parkinglots.OperatingHours;
import dev.angelcorzo.neoparking.model.parkinglots.ParkingLots;
import java.util.UUID;

public record ParkingLotsReference(
    UUID id,
    String name,
    Address address,
    Integer totalSpots,
    String timezone,
    String currency,
    OperatingHours operatingHours) {
  public static ParkingLotsReference of(ParkingLots parkingLots) {
    return new ParkingLotsReference(
        parkingLots.getId(),
        parkingLots.getName(),
        parkingLots.getAddress(),
        parkingLots.getTotalSpots(),
        parkingLots.getTimezone(),
        parkingLots.getCurrency(),
        parkingLots.getOperatingHours());
  }
}
