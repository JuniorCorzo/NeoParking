package dev.angelcorzo.neoparking.usecase.updateparking;

import dev.angelcorzo.neoparking.model.parkinglots.ParkingLots;
import dev.angelcorzo.neoparking.model.parkinglots.gateways.ParkingLotsRepository;
import dev.angelcorzo.neoparking.model.parkinglots.gateways.ParkingNotExistsException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateParkingLotsUseCase {
  private final ParkingLotsRepository parkingLotsRepository;

  public ParkingLots update(ParkingLots parking) {
    if (this.parkingLotsRepository.existsById(parking.getId()))
      throw new ParkingNotExistsException(parking.getId());

    return this.parkingLotsRepository.save(parking);
  }
}
