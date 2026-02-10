package dev.angelcorzo.neoparking.api.rates.dto;

import dev.angelcorzo.neoparking.api.parkinglots.dto.ParkingLotsInfo;
import dev.angelcorzo.neoparking.api.specialpolicies.dto.SpecialPoliciesInfo;
import dev.angelcorzo.neoparking.api.tenants.dto.TenantInfo;
import dev.angelcorzo.neoparking.model.rates.enums.TimeUnitsRate;
import dev.angelcorzo.neoparking.model.rates.enums.VehicleType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record RatesDTO(
    UUID id,
    TenantInfo tenant,
    ParkingLotsInfo parking,
    String name,
    String description,
    BigDecimal pricePerUnit,
    TimeUnitsRate timeUnit,
    String minChargeTimeMinutes,
    VehicleType vehicleType,
    SpecialPoliciesInfo specialPolicy,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    OffsetDateTime deletedAt) {}
