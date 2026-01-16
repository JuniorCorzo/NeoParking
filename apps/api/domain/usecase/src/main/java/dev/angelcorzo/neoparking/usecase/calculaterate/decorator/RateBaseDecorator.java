package dev.angelcorzo.neoparking.usecase.calculaterate.decorator;

import dev.angelcorzo.neoparking.usecase.calculaterate.dtos.ItemPriceDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.LinkedList;

public class RateBaseDecorator implements RateComponent {
  private final BigDecimal total;
  private final ChronoUnit timeUnit;
  private final Duration duration;
  private final OffsetDateTime entryTime;

  private final LinkedList<ItemPriceDTO> itemizedPrices = new LinkedList<>();

  public RateBaseDecorator(
      String name,
      ChronoUnit timeUnit,
      Duration minDuration,
      OffsetDateTime entryTime,
      BigDecimal pricePerUnit) {
    this.timeUnit = timeUnit;
    this.entryTime = entryTime;
    this.duration = Duration.between(entryTime, OffsetDateTime.now());
    this.total = this.calculateTotal(pricePerUnit, minDuration);

    this.itemizedPrices.add(ItemPriceDTO.of(name, total));
  }

  @Override
  public BigDecimal getPrice() {
    return this.total;
  }

  @Override
  public Duration getDuration() {
    return Duration.between(entryTime, OffsetDateTime.now());
  }

  @Override
  public TemporalUnit getTimeUnit() {
    return this.timeUnit;
  }

  @Override
  public LinkedList<ItemPriceDTO> getItemizedPrices() {
    return this.itemizedPrices;
  }

  private BigDecimal calculateTotal(BigDecimal pricePerUnit, Duration minDuration) {
    if (this.duration.get(ChronoUnit.MINUTES) < minDuration.toMinutes())
      return pricePerUnit.multiply(BigDecimal.valueOf(minDuration.get(this.timeUnit)));

    return pricePerUnit
        .multiply(BigDecimal.valueOf(this.duration.get(this.timeUnit)))
        .setScale(2, RoundingMode.CEILING);
  }
}
