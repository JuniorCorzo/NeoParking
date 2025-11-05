package dev.angelcorzo.neoparking.jpa.users;

import dev.angelcorzo.neoparking.jpa.parkinglots.ParkingLotsData;
import dev.angelcorzo.neoparking.jpa.tenants.TenantsData;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents the JPA data entity for a User.
 *
 * <p>This class maps to the "users" table in the database and includes auditing fields
 * for creation, update, and soft deletion.</p>
 *
 * <p><strong>Layer:</strong> Infrastructure (Driven Adapter - JPA)</p>
 * <p><strong>Responsibility:</strong> To persist and retrieve User data from the database.</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 * @see TenantsData
 * @see Roles
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
public class UsersData {
    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The full name of the user.
     */
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /**
     * The email address of the user. Must be unique.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * The hashed password of the user.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The role of the user within the tenant.
     */
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;

    /**
     * The tenant to which this user belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id",  referencedColumnName = "id",foreignKey = @ForeignKey(name = "users_tenant_id_fkey"))
    private TenantsData tenant;

    /**
     * Additional contact information for the user.
     */
    @Column(name = "contact_info")
    private String contactInfo;

    /**
     * The ID of the user who performed the soft deletion.
     */
    @Column(name = "delete_by")
    private String deletedBy;

    /**
     * Timestamp when the user record was created.
     */
    @CreatedDate
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    /**
     * Timestamp when the user record was last updated.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    /**
     * Timestamp when the user record was soft deleted.
     */
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @OneToMany(mappedBy = "parking_lots", fetch = FetchType.LAZY, targetEntity = ParkingLotsData.class)
    private List<ParkingLotsData> parkingLots;
}
