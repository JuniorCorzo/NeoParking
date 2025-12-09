package dev.angelcorzo.neoparking.api.slot.dto;

import dev.angelcorzo.neoparking.model.slots.enums.SlotStatus;
import dev.angelcorzo.neoparking.model.slots.enums.SlotType;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record SlotInfo(UUID id, String slotNumber, SlotType type, SlotStatus status) {}
