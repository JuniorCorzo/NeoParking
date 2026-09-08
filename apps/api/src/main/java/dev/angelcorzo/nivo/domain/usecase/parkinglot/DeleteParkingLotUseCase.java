package dev.angelcorzo.nivo.domain.usecase.parkinglot;

import dev.angelcorzo.nivo.domain.model.parkinglots.ParkingLots;
import dev.angelcorzo.nivo.domain.model.parkinglots.exceptions.ParkingNotExistsException;
import dev.angelcorzo.nivo.domain.model.parkinglots.gateways.ParkingLotsRepository;
import dev.angelcorzo.nivo.domain.model.slots.gateways.SlotsRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteParkingLotUseCase {

  private final ParkingLotsRepository parkingLotsRepository;
  private final SlotsRepository slotsRepository;

  public void execute(UUID parkingId) {
    ParkingLots parkingLot =
        parkingLotsRepository
            .findById(parkingId)
            .orElseThrow(() -> new ParkingNotExistsException(parkingId));

    slotsRepository.softDeleteByParkingLotsId(parkingId);

    parkingLotsRepository.delete(parkingLot);
  }
}
