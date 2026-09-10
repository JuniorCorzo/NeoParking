package dev.angelcorzo.nivo.infrastructure.entrypoint.rest.rates.dto;

import dev.angelcorzo.nivo.infrastructure.entrypoint.rest.specialpolicies.dto.SpecialPoliciesInfo;
import dev.angelcorzo.nivo.domain.model.rates.enums.TimeUnitsRate;
import dev.angelcorzo.nivo.domain.model.rates.enums.VehicleType;
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
    TimeUnitsRate timeUnit,
    Integer minChargeTimeMinutes,
    VehicleType vehicleType,
    SpecialPoliciesInfo specialPolicy) {}
