package dev.angelcorzo.neoparking.usecase.calculaterate.decorator;

import dev.angelcorzo.neoparking.usecase.calculaterate.dtos.ItemPriceDTO;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.LinkedList;

public interface RateComponent {
  BigDecimal getPrice();

  Duration getDuration();

  TemporalUnit getTimeUnit();

  LinkedList<ItemPriceDTO> getItemizedPrices();
}
