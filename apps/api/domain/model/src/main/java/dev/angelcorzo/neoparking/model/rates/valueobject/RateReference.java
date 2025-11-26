package dev.angelcorzo.neoparking.model.rates.valueobject;

import dev.angelcorzo.neoparking.model.rates.Rates;
import dev.angelcorzo.neoparking.model.rates.enums.VehicleType;
import dev.angelcorzo.neoparking.model.specialpolicies.valueobjects.SpecialPoliciesReference;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Builder(toBuilder = true)
public record RateReference(
    UUID id,
    String name,
    String description,
    BigDecimal pricePerUnit,
    ChronoUnit timeUnit,
    String minChargeTimeMinutes,
    VehicleType vehicleType,
    SpecialPoliciesReference specialPolicy) {
	public static RateReference of(Rates rate) {
    return RateReference.builder()
		    .id(rate.getId())
		    .name(rate.getName())
		    .description(rate.getDescription())
		    .pricePerUnit(rate.getPricePerUnit())
		    .timeUnit(rate.getTimeUnit())
		    .minChargeTimeMinutes(rate.getMinChargeTimeMinutes())
		    .vehicleType(rate.getVehicleType())
		    .specialPolicy(rate.getSpecialPolicy())
		    .build();
	}

	public boolean hasSpecialPolicy() {
		return this.specialPolicy != null;
	}
}
