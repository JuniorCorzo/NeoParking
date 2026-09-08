package dev.angelcorzo.nivo.infrastructure.entrypoint.rest.rates.dto;

import dev.angelcorzo.nivo.infrastructure.entrypoint.rest.parkinglots.dto.ParkingLotsInfo;
import dev.angelcorzo.nivo.infrastructure.entrypoint.rest.specialpolicies.dto.SpecialPoliciesInfo;
import dev.angelcorzo.nivo.infrastructure.entrypoint.rest.tenants.dto.TenantInfo;
import dev.angelcorzo.nivo.domain.model.rates.enums.TimeUnitsRate;
import dev.angelcorzo.nivo.domain.model.rates.enums.VehicleType;
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
