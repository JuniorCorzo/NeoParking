package dev.angelcorzo.neoparking.jpa.parkinglots;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.angelcorzo.neoparking.jpa.tenants.TenantsData;
import dev.angelcorzo.neoparking.jpa.users.UsersData;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "parking_lots")
@Entity
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE parking_lots SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
public class ParkingLotsData {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Embedded
  @AttributeOverride(name = "zipCode", column = @Column(name = "zip_code"))
  private AddressType address;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "total_spots", nullable = false)
  private Long totalSpots;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
  @JsonBackReference("parking-lot-owner")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UsersData owner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false)
  @JsonBackReference("parking-lot-tenant")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private TenantsData tenant;

  @ColumnDefault("UTC-5")
  @Column(name = "timezone")
  private String timezone;

  @ColumnDefault("COP")
  @Column(name = "currency")
  private String currency;

  @Column(name = "operating_hours")
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "openTime", column = @Column(name = "open_time")),
    @AttributeOverride(name = "closeTime", column = @Column(name = "close_time"))
  })
  private OperatingHoursType operatingHours;

  @CreationTimestamp
  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  @Column(name = "deleted_at")
  private OffsetDateTime deletedAt;
}
