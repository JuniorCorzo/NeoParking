package dev.angelcorzo.neoparking.usecase.createparking;

import dev.angelcorzo.neoparking.model.parkinglots.ParkingLots;
import dev.angelcorzo.neoparking.model.parkinglots.dto.UpsertParkingLotsDTO;
import dev.angelcorzo.neoparking.model.parkinglots.gateways.ParkingLotsRepository;
import dev.angelcorzo.neoparking.model.tenants.Tenants;
import dev.angelcorzo.neoparking.model.tenants.gateways.TenantsRepository;
import dev.angelcorzo.neoparking.model.tenants.valueobject.TenantReference;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import dev.angelcorzo.neoparking.model.users.exceptions.UserNotExistsInTenantException;
import dev.angelcorzo.neoparking.model.users.gateways.UsersRepository;
import dev.angelcorzo.neoparking.model.users.valueobject.UserReference;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateParkingUseCase {
  private final ParkingLotsRepository parkingLotsRepository;
  private final UsersRepository usersRepository;
  private final TenantsRepository tenantsRepository;

  public ParkingLots created(UpsertParkingLotsDTO parking) {
    UUID ownerId =
        this.tenantsRepository
            .findById(parking.tenantId())
            .map(Tenants::getUsers)
            .map(users -> users.stream().filter(user -> user.role() == Roles.OWNER).toList())
            .map(users -> users.getFirst().id())
            .orElseThrow(UserNotExistsInTenantException::new);

    final ParkingLots parkingLots =
        ParkingLots.builder()
            .name(parking.name())
            .address(parking.address())
            .owner(UserReference.of(usersRepository.getReferenceById(ownerId)))
            .tenant(TenantReference.of(this.tenantsRepository.getReferenceById(parking.tenantId())))
            .timezone(parking.timezone())
            .currency(parking.currency())
            .operatingHours(parking.operatingHours())
            .totalSpots(0)
            .build();

    return this.parkingLotsRepository.save(parkingLots);
  }
}
