package dev.angelcorzo.nivo.domain.model.slots;

import dev.angelcorzo.nivo.domain.model.parkinglots.valueobject.ParkingLotsReference;
import dev.angelcorzo.nivo.domain.model.slots.enums.SlotStatus;
import dev.angelcorzo.nivo.domain.model.slots.enums.SlotType;
import dev.angelcorzo.nivo.domain.model.tenants.valueobject.TenantReference;
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
  private String zone;
  private String prefix;
  private SlotType type;
  private SlotStatus status;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private OffsetDateTime deletedAt;
}
