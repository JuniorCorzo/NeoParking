package dev.angelcorzo.neoparking.model.rates;

import dev.angelcorzo.neoparking.model.parkinglots.valueobject.ParkingLotsReference;
import dev.angelcorzo.neoparking.model.rates.enums.VehicleType;
import dev.angelcorzo.neoparking.model.specialpolicies.valueobjects.SpecialPoliciesReference;
import dev.angelcorzo.neoparking.model.tenants.valueobject.TenantReference;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Rates {
  private UUID id;
  private TenantReference tenant;
  private ParkingLotsReference parking;
  private String name;
  private String description;
  private BigDecimal pricePerUnit;
  private ChronoUnit timeUnit;
  private String minChargeTimeMinutes;
  private VehicleType vehicleType;
  private SpecialPoliciesReference specialPolicy;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private OffsetDateTime deletedAt;

  public boolean hasSpecialPolicy() {
    return this.specialPolicy != null;
  }
}
