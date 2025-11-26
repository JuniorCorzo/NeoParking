package dev.angelcorzo.neoparking.usecase.updaterate;

import dev.angelcorzo.neoparking.model.rates.Rates;
import dev.angelcorzo.neoparking.model.rates.enums.VehicleType;
import dev.angelcorzo.neoparking.model.rates.exceptions.RateNotFoundException;
import dev.angelcorzo.neoparking.model.rates.gateways.RatesRepository;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateRateUseCase {
  private final RatesRepository ratesRepository;

  public Rates execute(UpdateRate updateRate) {
    final Rates rate =
        this.ratesRepository
            .findById(updateRate.id())
            .orElseThrow(() -> new RateNotFoundException(updateRate.id()))
            .toBuilder()
            .name(updateRate.name())
            .description(updateRate.description())
            .pricePerUnit(updateRate.pricePerUnit())
            .timeUnit(updateRate.timeUnit())
            .minChargeTimeMinutes(updateRate.minChargeTimeMinutes())
            .vehicleType(updateRate.vehicleType())
            .build();

    return this.ratesRepository.save(rate);
  }

  public record UpdateRate(
      UUID id,
      String name,
      String description,
      BigDecimal pricePerUnit,
      ChronoUnit timeUnit,
      String minChargeTimeMinutes,
      VehicleType vehicleType) {}
}
