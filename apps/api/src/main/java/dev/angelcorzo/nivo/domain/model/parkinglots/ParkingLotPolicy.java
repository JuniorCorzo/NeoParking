package dev.angelcorzo.nivo.domain.model.parkinglots;

import java.math.BigDecimal;
import lombok.Builder;

@Builder(toBuilder = true)
public record ParkingLotPolicy(
    int gracePeriodMinutes,
    BigDecimal gracePeriodPrice,
    BigDecimal ivaRate
) {
  public ParkingLotPolicy {
    gracePeriodPrice = gracePeriodPrice != null ? gracePeriodPrice : BigDecimal.ZERO;
    ivaRate = ivaRate != null ? ivaRate : new BigDecimal("0.19");
  }

  public static ParkingLotPolicy defaults() {
    return new ParkingLotPolicy(0, BigDecimal.ZERO, new BigDecimal("0.19"));
  }

  public boolean hasGracePeriod() {
    return this.gracePeriodMinutes > 0;
  }

  public boolean isGraceFree() {
    return BigDecimal.ZERO.compareTo(this.gracePeriodPrice) == 0;
  }
}
