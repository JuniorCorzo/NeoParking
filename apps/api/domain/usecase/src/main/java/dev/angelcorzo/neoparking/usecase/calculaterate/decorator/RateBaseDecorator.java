package dev.angelcorzo.neoparking.usecase.calculaterate.decorator;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;

@AllArgsConstructor
public abstract class BasicRate implements RateComponent {
  protected RateComponent rateComponent;

  @Override
  public BigDecimal getPrice() {
    return this.rateComponent.getPrice();
  }

  @Override
  public Duration getDuration() {
    return this.rateComponent.getDuration();
  }
}
