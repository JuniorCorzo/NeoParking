package dev.angelcorzo.neoparking.usecase.calculaterate.decorator;

import dev.angelcorzo.neoparking.model.specialpolicies.enums.ModifiesTypes;
import dev.angelcorzo.neoparking.model.specialpolicies.valueobjects.SpecialPoliciesReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.temporal.TemporalUnit;

public class RateWithSpecialPolicyDecorator implements RateComponent {
  private final SpecialPoliciesReference specialPoliciesReference;
  protected RateComponent rateComponent;

  public RateWithSpecialPolicyDecorator(
      RateComponent rateComponent, SpecialPoliciesReference specialPoliciesReference) {
    this.rateComponent = rateComponent;
    this.specialPoliciesReference = specialPoliciesReference;
  }

  @Override
  public BigDecimal getPrice() {
    if (this.specialPoliciesReference.modifies() == ModifiesTypes.TIME)
      return this.rateComponent.getPrice();
    final BigDecimal priceModified = this.obtainPrice(this.rateComponent.getPrice());

    return priceModified.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : priceModified;
  }

  @Override
  public Duration getDuration() {
    if (this.specialPoliciesReference.modifies() != ModifiesTypes.TIME)
      return this.rateComponent.getDuration();
    final Duration durationModified = this.obtainDuration(this.rateComponent.getDuration());

    return durationModified.toMillis() < 0L ? Duration.ZERO : durationModified;
  }

  @Override
  public TemporalUnit getTimeUnit() {
    return this.rateComponent.getTimeUnit();
  }

  private BigDecimal obtainPrice(BigDecimal price) {
    final BigDecimal valueToModify = this.specialPoliciesReference.valueToModify();

    return switch (this.specialPoliciesReference.operation()) {
      case SUBTRACT -> price.subtract(valueToModify);
      case SET -> valueToModify;
      case PERCENTAGE -> {
        final BigDecimal discount =
            price.multiply(valueToModify.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));

        yield price.subtract(discount);
      }
    };
  }

  private Duration obtainDuration(Duration duration) {
    final Duration timeToModify =
        Duration.of(
            this.specialPoliciesReference.valueToModify().longValue(),
            this.rateComponent.getTimeUnit());

    return switch (this.specialPoliciesReference.operation()) {
      case SET -> timeToModify;
      case SUBTRACT -> duration.minus(timeToModify);
      case PERCENTAGE -> {
        final long currentTime = duration.toMillis();
        final long modifiedTime = currentTime - (currentTime * timeToModify.toMillis() / 100);
        yield Duration.ofMillis(modifiedTime);
      }
    };
  }
}
