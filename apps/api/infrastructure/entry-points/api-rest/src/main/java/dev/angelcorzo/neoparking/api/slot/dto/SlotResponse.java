package dev.angelcorzo.neoparking.api.slot.dto;

import dev.angelcorzo.neoparking.api.parkinglots.dto.ParkingLotsInfo;
import dev.angelcorzo.neoparking.api.tenants.dto.TenantInfo;
import dev.angelcorzo.neoparking.model.slots.enums.SlotStatus;
import dev.angelcorzo.neoparking.model.slots.enums.SlotType;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record SlotResponse(
    UUID id,
    TenantInfo tenant,
    ParkingLotsInfo parking,
    String slotNumber,
    SlotType type,
    SlotStatus status,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    OffsetDateTime deletedAt) {}
