package dev.angelcorzo.neoparking.jpa.slot;

import dev.angelcorzo.neoparking.jpa.parkinglots.ParkingLotsData;
import dev.angelcorzo.neoparking.jpa.tenants.TenantsData;
import dev.angelcorzo.neoparking.model.slots.enums.SlotStatus;
import dev.angelcorzo.neoparking.model.slots.enums.SlotType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "slots")
@Entity
@SQLRestriction(value = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE slots SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class SlotsData {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, targetEntity = ParkingLotsData.class)
  @JoinColumn(name = "parking_lot_id", referencedColumnName = "id", nullable = false)
  private ParkingLotsData parking;

  @ManyToOne(fetch = FetchType.LAZY, targetEntity = TenantsData.class)
  @JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false)
  private TenantsData tenant;

  @Column(name = "slot_number", nullable = false)
  private String slotNumber;

  @ColumnDefault(value = "CAR")
  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private SlotType type;

  @ColumnDefault(value = "AVAILABLE")
  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private SlotStatus status;

  @CreationTimestamp
  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  @Column(name = "deleted_at")
  private OffsetDateTime deletedAt;
}
