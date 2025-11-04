package dev.angelcorzo.neoparking.jpa.tenants;

import dev.angelcorzo.neoparking.jpa.users.UsersData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents the JPA data entity for a Tenant.
 *
 * <p>This class maps to the "tenants" table in the database and includes auditing fields
 * for creation, update, and soft deletion. It also defines the relationship with {@link UsersData}.</p>
 *
 * <p><strong>Layer:</strong> Infrastructure (Driven Adapter - JPA)</p>
 * <p><strong>Responsibility:</strong> To persist and retrieve Tenant data from the database.</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 * @see UsersData
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tenants")
@SQLRestriction(value = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE tenants SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class TenantsData {
  /**
   * Unique identifier for the tenant.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  /**
   * The legal or commercial name of the company representing the tenant.
   */
  @Column(name = "company_name", nullable = false)
  private String companyName;

  /**
   * Timestamp when the tenant record was created.
   */
  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  /**
   * Timestamp when the tenant record was last updated.
   */
  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  /**
   * Timestamp when the tenant record was soft deleted.
   */
  @Column(name = "deleted_at")
  private OffsetDateTime deletedAt;

  /**
   * List of users associated with this tenant.
   * This is a one-to-many relationship, mapped by the "tenant" field in {@link UsersData}.
   */
  @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, targetEntity = UsersData.class)
  private List<UsersData> users;
}
