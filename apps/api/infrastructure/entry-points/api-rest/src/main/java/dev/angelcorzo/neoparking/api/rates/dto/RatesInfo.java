package dev.angelcorzo.neoparking.api.rates.dto;

import dev.angelcorzo.neoparking.api.specialpolicies.dto.SpecialPoliciesInfo;
import dev.angelcorzo.neoparking.model.rates.enums.VehicleType;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record RatesInfo(
    UUID id,
    String name,
    String description,
    BigDecimal pricePerUnit,
    ChronoUnit timeUnit,
    String minChargeTimeMinutes,
    VehicleType vehicleType,
    SpecialPoliciesInfo specialPolicy) {}
