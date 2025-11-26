package dev.angelcorzo.neoparking.api.rates.dto;

import dev.angelcorzo.neoparking.model.rates.enums.VehicleType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Builder(toBuilder = true)
public record UpdateRate(
    @org.hibernate.validator.constraints.UUID UUID id,
    @NotEmpty String name,
    @NotEmpty String description,
    @Min(value = 0) BigDecimal pricePerUnit,
    @NotNull ChronoUnit timeUnit,
    @NotNull String minChargeTimeMinutes,
    @NotNull VehicleType vehicleType) {}
