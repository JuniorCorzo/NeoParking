package dev.angelcorzo.neoparking.model.slots.valueobject;

import dev.angelcorzo.neoparking.model.slots.Slots;
import dev.angelcorzo.neoparking.model.slots.enums.SlotStatus;
import dev.angelcorzo.neoparking.model.slots.enums.SlotType;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record SlotsReference(UUID id, String slotNumber, SlotType type, SlotStatus status) {
  public static SlotsReference of(Slots slot) {
    return new SlotsReference(slot.getId(), slot.getSlotNumber(), slot.getType(), slot.getStatus());
  }
}
