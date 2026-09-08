package dev.angelcorzo.nivo.domain.model.slots.valueobject;

import java.util.UUID;

import dev.angelcorzo.nivo.domain.model.slots.enums.SlotStatus;
import dev.angelcorzo.nivo.domain.model.slots.enums.SlotType;

public record SlotSummary(UUID id, String parkingName, SlotType type, String prefix, String zone,
    String numberSlot, SlotStatus status, boolean hasTicket, boolean hasHistory) {
}
