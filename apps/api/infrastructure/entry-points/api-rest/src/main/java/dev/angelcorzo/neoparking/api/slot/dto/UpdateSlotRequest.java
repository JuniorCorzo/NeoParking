package dev.angelcorzo.neoparking.api.slot.dto;

import dev.angelcorzo.neoparking.model.slots.enums.SlotStatus;
import dev.angelcorzo.neoparking.model.slots.enums.SlotType;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record UpdateSlotRequest(
    @NotNull UUID id,
    @NotEmpty String slotNumber,
    @NotEmpty SlotType type,
    @NotEmpty SlotStatus status) {}
