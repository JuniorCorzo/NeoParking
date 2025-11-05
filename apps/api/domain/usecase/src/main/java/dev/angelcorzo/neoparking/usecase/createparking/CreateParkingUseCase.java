package dev.angelcorzo.neoparking.usecase.createparking;

import dev.angelcorzo.neoparking.model.parkinglots.ParkingLots;
import dev.angelcorzo.neoparking.model.parkinglots.gateways.ParkingLotsRepository;
import dev.angelcorzo.neoparking.model.users.exceptions.UserNotExistsInTenantException;
import dev.angelcorzo.neoparking.model.users.gateways.UsersRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateParkingUseCase {
  private final ParkingLotsRepository parkingLotsRepository;
  private final UsersRepository usersRepository;

  public ParkingLots created(ParkingLots parking) {
    if (this.usersRepository.existsByIdAndTenantId(parking.getOwnerId(), parking.getTenantId()))
      throw new UserNotExistsInTenantException();

    return this.parkingLotsRepository.save(parking);
  }
}
