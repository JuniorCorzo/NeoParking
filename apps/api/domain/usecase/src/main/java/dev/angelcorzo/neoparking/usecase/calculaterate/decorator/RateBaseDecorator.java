package dev.angelcorzo.neoparking.usecase.calculaterate.decorator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class RateBaseDecorator implements RateComponent {
  private BigDecimal pricePerUnit;
  private ChronoUnit timeUnit;
  private OffsetDateTime entryTime;

  @Override
  public BigDecimal getPrice() {
        return this.pricePerUnit.multiply(BigDecimal.valueOf(this.getDuration().get(timeUnit)));
  }

  @Override
  public Duration getDuration() {
    return Duration.between(entryTime, OffsetDateTime.now());
  }

  @Override
  public TemporalUnit getTimeUnit() {
    return this.timeUnit;
  }
}
