package dev.angelcorzo.nivo.domain.usecase.rate.decorator;

import dev.angelcorzo.nivo.domain.model.rates.enums.TimeUnitsRate;
import dev.angelcorzo.nivo.domain.model.rates.valueobject.RateReference;
import dev.angelcorzo.nivo.domain.usecase.rate.dtos.PriceDetailed;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.LinkedList;

public interface RateComponent {
  RateReference getRates();

  BigDecimal getPrice();

  Duration getDuration();

  TimeUnitsRate getTimeUnit();

  PriceDetailed getItemizedPrices();
}
