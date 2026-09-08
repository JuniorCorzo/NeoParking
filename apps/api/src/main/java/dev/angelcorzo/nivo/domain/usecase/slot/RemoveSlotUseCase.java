package dev.angelcorzo.nivo.domain.usecase.slot;

import dev.angelcorzo.nivo.domain.model.parkinglots.gateways.ParkingLotsRepository;
import dev.angelcorzo.nivo.domain.model.slots.gateways.SlotsRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RemoveSlotUseCase {
  private final SlotsRepository slotsRepository;
  private final ParkingLotsRepository parkingLotsRepository;

  public void execute(UUID id) {
    this.slotsRepository.deleteById(id);
  }
}
