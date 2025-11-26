package dev.angelcorzo.neoparking.usecase.rateconfiguration;

import dev.angelcorzo.neoparking.model.parkinglots.exceptions.ParkingNotExistsException;
import dev.angelcorzo.neoparking.model.parkinglots.gateways.ParkingLotsRepository;
import dev.angelcorzo.neoparking.model.parkinglots.valueobject.ParkingLotsReference;
import dev.angelcorzo.neoparking.model.rates.Rates;
import dev.angelcorzo.neoparking.model.rates.enums.VehicleType;
import dev.angelcorzo.neoparking.model.rates.gateways.RatesRepository;
import dev.angelcorzo.neoparking.model.specialpolicies.exceptions.SpecialPolicyNotFoundException;
import dev.angelcorzo.neoparking.model.specialpolicies.gateways.SpecialPoliciesRepository;
import dev.angelcorzo.neoparking.model.specialpolicies.valueobjects.SpecialPoliciesReference;
import dev.angelcorzo.neoparking.model.tenants.gateways.TenantsRepository;
import dev.angelcorzo.neoparking.model.tenants.valueobject.TenantReference;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RateConfigurationUseCase {
  private final RatesRepository ratesRepository;
  private final SpecialPoliciesRepository specialPoliciesRepository;
  private final ParkingLotsRepository parkingLotsRepository;
  private final TenantsRepository tenantsRepository;

  public Rates execute(final CreateTariff tariff) {
    this.validate(tariff);

    final Rates rate =
        Rates.builder()
            .tenant(TenantReference.of(this.tenantsRepository.getReferenceById(tariff.tenantId())))
            .parking(
                ParkingLotsReference.of(
                    this.parkingLotsRepository.getReferenceById(tariff.parkingLotId())))
            .name(tariff.name())
            .description(tariff.description())
            .pricePerUnit(tariff.pricePerUnit())
            .timeUnit(tariff.timeUnit())
            .minChargeTimeMinutes(tariff.minChargeTimeMinutes())
            .vehicleType(tariff.vehicleType())
            .specialPolicy(
                SpecialPoliciesReference.of(
                    this.specialPoliciesRepository.getReferenceById(tariff.specialPolicyId())))
            .build();

    return this.ratesRepository.save(rate);
  }

  public void validate(CreateTariff tariff) {
    if (tariff.specialPolicyId() != null
        && !this.specialPoliciesRepository.existsById(tariff.specialPolicyId()))
      throw new SpecialPolicyNotFoundException(tariff.specialPolicyId());

    if (!this.parkingLotsRepository.existsById(tariff.parkingLotId()))
      throw new ParkingNotExistsException(tariff.parkingLotId());
  }

  @Builder(toBuilder = true)
  public record CreateTariff(
      UUID tenantId,
      UUID parkingLotId,
      String name,
      String description,
      BigDecimal pricePerUnit,
      ChronoUnit timeUnit,
      String minChargeTimeMinutes,
      VehicleType vehicleType,
      UUID specialPolicyId) {}
}
