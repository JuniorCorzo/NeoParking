package dev.angelcorzo.nivo.domain.model.parkinglots;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ParkingLotPolicy Tests")
class ParkingLotPolicyTest {

  @Test
  @DisplayName("defaults() should create policy with zero grace and 0.19 IVA")
  void shouldCreateDefaults() {
    ParkingLotPolicy policy = ParkingLotPolicy.defaults();

    assertThat(policy.gracePeriodMinutes()).isZero();
    assertThat(policy.gracePeriodPrice()).isEqualByComparingTo(BigDecimal.ZERO);
    assertThat(policy.ivaRate()).isEqualByComparingTo(new BigDecimal("0.19"));
    assertThat(policy.hasGracePeriod()).isFalse();
    assertThat(policy.isGraceFree()).isTrue();
  }

  @Test
  @DisplayName("hasGracePeriod() should return true when minutes > 0")
  void shouldDetectGracePeriodPresence() {
    ParkingLotPolicy policy = new ParkingLotPolicy(15, BigDecimal.ZERO, new BigDecimal("0.19"));

    assertThat(policy.hasGracePeriod()).isTrue();
    assertThat(policy.isGraceFree()).isTrue();
  }

  @Test
  @DisplayName("isGraceFree() should return false when gracePeriodPrice > 0")
  void shouldDetectPricedGracePeriod() {
    ParkingLotPolicy policy = new ParkingLotPolicy(15, new BigDecimal("500.00"), new BigDecimal("0.19"));

    assertThat(policy.hasGracePeriod()).isTrue();
    assertThat(policy.isGraceFree()).isFalse();
  }
}
