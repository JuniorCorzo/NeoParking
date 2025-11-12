package dev.angelcorzo.neoparking.model.slots;

import dev.angelcorzo.neoparking.model.parkinglots.valueobject.ParkingLotsReference;
import dev.angelcorzo.neoparking.model.slots.enums.SlotStatus;
import dev.angelcorzo.neoparking.model.slots.enums.SlotType;
import dev.angelcorzo.neoparking.model.tenants.valueobject.TenantReference;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Slots {
  private UUID id;
  private ParkingLotsReference parking;
  private TenantReference tenant;
  private String slotNumber;
  private SlotType type;
  private SlotStatus status;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private OffsetDateTime deletedAt;
}
