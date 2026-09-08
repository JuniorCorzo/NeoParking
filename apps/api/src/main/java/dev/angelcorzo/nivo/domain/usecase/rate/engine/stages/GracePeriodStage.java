package dev.angelcorzo.nivo.domain.usecase.rate.engine.stages;

import dev.angelcorzo.nivo.domain.model.parkinglots.ParkingLotPolicy;
import dev.angelcorzo.nivo.domain.usecase.rate.dtos.PriceLine;
import dev.angelcorzo.nivo.domain.usecase.rate.engine.PricingContext;
import dev.angelcorzo.nivo.domain.usecase.rate.engine.PricingStage;
import java.time.Duration;

public class GracePeriodStage implements PricingStage {

  @Override
  public PricingContext apply(PricingContext context) {
    if (context.settled()) {
      return context;
    }

    ParkingLotPolicy policy = context.policy();
    if (policy == null || !policy.hasGracePeriod()) {
      return context;
    }

    Duration graceDuration = Duration.ofMinutes(policy.gracePeriodMinutes());
    if (context.duration().compareTo(graceDuration) <= 0) {
      if (policy.isGraceFree()) {
        return context.toSettled();
      } else {
        PriceLine line = PriceLine.of("Grace period", policy.gracePeriodPrice());
        return context.withSubtotal(policy.gracePeriodPrice(), line).toSettled();
      }
    }

    return context;
  }
}
