package dev.angelcorzo.neoparking.jpa.parkinglots;

import dev.angelcorzo.neoparking.jpa.tenants.TenantsData;
import dev.angelcorzo.neoparking.jpa.users.UsersData;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
  private UsersData owner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false)
  private TenantsData tenant;

  @ColumnDefault("UTC-5")
  @Column(name = "timezone")
  private String timezone;

  @ColumnDefault("COP")
  @Column(name = "currency")
  private String currency;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "openTime", column = @Column(name = "open_time")),
    @AttributeOverride(name = "closeTime", column = @Column(name = "close_time"))
  })
  private OperationHorusType operationHorus;

  @CreatedDate
  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  @Column(name = "deleted_at")
  private OffsetDateTime deletedAt;
}
