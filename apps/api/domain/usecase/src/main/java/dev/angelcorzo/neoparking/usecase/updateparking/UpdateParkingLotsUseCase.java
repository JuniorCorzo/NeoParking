package dev.angelcorzo.neoparking.usecase.updateparking;

import dev.angelcorzo.neoparking.model.parkinglots.ParkingLots;
import dev.angelcorzo.neoparking.model.parkinglots.dto.UpsertParkingLotsDTO;
import dev.angelcorzo.neoparking.model.parkinglots.exceptions.ParkingNotExistsException;
import dev.angelcorzo.neoparking.model.parkinglots.gateways.ParkingLotsRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateParkingLotsUseCase {
  private final ParkingLotsRepository parkingLotsRepository;

  public ParkingLots update(final UpsertParkingLotsDTO parking) {
    return this.parkingLotsRepository
        .findById(parking.id())
        .orElseThrow(() -> new ParkingNotExistsException(parking.id()))
        .toBuilder()
        .name(parking.name())
        .operatingHours(parking.operatingHours())
        .currency(parking.currency())
        .address(parking.address())
        .build();
  }
}
