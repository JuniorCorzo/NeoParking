package dev.angelcorzo.neoparking.jpa.users.userinvitation;

import dev.angelcorzo.neoparking.jpa.tenants.TenantsData;
import dev.angelcorzo.neoparking.jpa.users.UsersData;
import dev.angelcorzo.neoparking.model.userinvitations.UserInvitationStatus;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user_invitations")
@EntityListeners((AuditingEntityListener.class))
@SQLDelete(sql = "UPDATE user_invitations SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
public class UserInvitationsData {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, targetEntity = TenantsData.class)
  @JoinColumn(
      name = "tenant_id",
      foreignKey = @ForeignKey(name = "fk_user_invitations_tenant_id_fkey"),
      nullable = false)
  private TenantsData tenant;

  @Column(name = "invited_email", nullable = false)
  private String invitedEmail;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private Roles role;
  @Column(name = "deleted_at")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID token;
  private UserInvitationStatus status;
  private UsersData invitedBy;
  private OffsetDateTime createdAt;
  private OffsetDateTime acceptedAt;
  private OffsetDateTime expiredAt;
}
