package dev.angelcorzo.neoparking.usecase.modifyuserrole;

import dev.angelcorzo.neoparking.model.tenants.Tenants;
import dev.angelcorzo.neoparking.model.tenants.gateways.TenantsRepository;
import dev.angelcorzo.neoparking.model.users.Users;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import dev.angelcorzo.neoparking.model.users.exceptions.UserNotExistsException;
import dev.angelcorzo.neoparking.model.users.exceptions.UserNotExistsInTenantException;
import dev.angelcorzo.neoparking.model.users.gateways.UsersRepository;
import dev.angelcorzo.neoparking.usecase.acceptinvitation.exceptions.InvalidRoleException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class ModifyUserRoleUseCase {
    private final UsersRepository usersRepository;
    private final TenantsRepository tenantsRepository;

    public Users modifyRole(final ModifyUserRole modifyUserRole) {
        final Users user = this.usersRepository.findByIdAndTenantId(modifyUserRole.userId(), modifyUserRole.tenantId())
                .orElseThrow(UserNotExistsInTenantException::new);


        this.validateRolePermitted(modifyUserRole.newRole());
        user.setRole(modifyUserRole.newRole());

        return this.usersRepository.save(user);
    }

    private void validateRolePermitted(Roles role) {
        Set<Roles> rolesNotPermitted = Set.of(Roles.OWNER, Roles.SUPERADMIN);
        if (rolesNotPermitted.contains(role)) {
            throw new InvalidRoleException(role);
        }
    }

    @Builder(toBuilder = true)
    public record ModifyUserRole(UUID userId, Roles newRole, UUID tenantId) {
    }
}
