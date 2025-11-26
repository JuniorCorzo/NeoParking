package dev.angelcorzo.neoparking.usecase.calculaterate.decorator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.TemporalUnit;

public interface RateComponent {
	BigDecimal getPrice();
	Duration getDuration();
	TemporalUnit getTimeUnit();
}
