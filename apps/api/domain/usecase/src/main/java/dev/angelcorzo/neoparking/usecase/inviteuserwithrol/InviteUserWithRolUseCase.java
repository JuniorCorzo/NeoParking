package dev.angelcorzo.neoparking.usecase.inviteuserwithrol;

import dev.angelcorzo.neoparking.model.tenants.Tenants;
import dev.angelcorzo.neoparking.model.tenants.exceptions.TenantNotExistsException;
import dev.angelcorzo.neoparking.model.tenants.gateways.TenantsRepository;
import dev.angelcorzo.neoparking.model.userinvitations.UserInvitationStatus;
import dev.angelcorzo.neoparking.model.userinvitations.UserInvitations;
import dev.angelcorzo.neoparking.model.userinvitations.gateways.UserInvitationsRepository;
import dev.angelcorzo.neoparking.model.users.Users;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import dev.angelcorzo.neoparking.model.users.exceptions.UserAlreadyExistsInTenantException;
import dev.angelcorzo.neoparking.model.users.exceptions.UserNotExistsException;
import dev.angelcorzo.neoparking.model.users.gateways.UsersRepository;
import dev.angelcorzo.neoparking.usecase.acceptinvitation.exceptions.InvalidRoleException;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InviteUserWithRolUseCase {
  private static final int EXPIRATION_DAYS = 3;
  private final UsersRepository usersRepository;
  private final UserInvitationsRepository userInvitationsRepository;
  private final TenantsRepository tenantsRepository;

  public UserInvitations registerInvitation(final InviteUserWithRole inviteUserWithRole) {
    this.validateInvitation(inviteUserWithRole);
    final Tenants tenant =
        this.tenantsRepository
            .findById(inviteUserWithRole.tenantId())
            .orElseThrow(() -> new TenantNotExistsException(inviteUserWithRole.tenantId()));

    final Users invitedBy =
        this.usersRepository
            .findById(inviteUserWithRole.inviteBy())
            .orElseThrow(() -> new UserNotExistsException(inviteUserWithRole.inviteBy()));

    final UserInvitations userInvitations =
        UserInvitations.builder()
            .tenant(tenant)
            .invitedEmail(inviteUserWithRole.email())
            .role(inviteUserWithRole.role())
            .token(UUID.randomUUID())
            .status(UserInvitationStatus.PENDING)
            .invitedBy(invitedBy)
            .createdAt(OffsetDateTime.now())
            .expiredAt(OffsetDateTime.now().plusDays(EXPIRATION_DAYS))
            .build();

    return this.userInvitationsRepository.save(userInvitations);
  }

  private void validateInvitation(final InviteUserWithRole invitation) {
    final UUID inviteBy = invitation.inviteBy();
    final UUID tenantId = invitation.tenantId();
    final String email = invitation.email();
    final Roles rol = invitation.role();

    if (this.usersRepository.existsByEmailAndTenantId(email, tenantId)) {
      throw new UserAlreadyExistsInTenantException(email);
    }

    this.validateRolePermitted(rol);

    if (!this.usersRepository.existsById(inviteBy)) {
      throw new UserNotExistsException(inviteBy);
    }
  }

  private void validateRolePermitted(Roles rol) {
    final Set<Roles> rolesNotPermitted = Set.of(Roles.OWNER, Roles.SUPERADMIN);
    if (rolesNotPermitted.contains(rol)) throw new InvalidRoleException(rol);
  }

  @Builder(toBuilder = true)
  public record InviteUserWithRole(String email, Roles role, UUID tenantId, UUID inviteBy) {}
}
