package dev.angelcorzo.neoparking.usecase.registertenant;

import dev.angelcorzo.neoparking.model.tenants.Tenants;
import dev.angelcorzo.neoparking.model.tenants.gateways.TenantsRepository;
import dev.angelcorzo.neoparking.model.users.Users;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import dev.angelcorzo.neoparking.model.users.exceptions.EmailAlreadyExistsException;
import dev.angelcorzo.neoparking.model.users.gateways.UsersRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public final class RegisterTenantUseCase {
    private final UsersRepository usersRepository;
    private final TenantsRepository tenantsRepository;

    public RegisterTenantResponse register(final Users user, final Tenants tenant) {
        this.validateEmailExists(user);

        final Tenants tenantCreated = this.tenantsRepository.save(tenant);

        user.setTenant(tenantCreated);
        user.setRole(Roles.OWNER);
        final Users userCreated = this.usersRepository.save(user);

        return RegisterTenantResponse.builder()
                .tenant(tenantCreated)
                .user(userCreated)
                .build();
    }

    private void validateEmailExists(Users user) {
        final String email = user.getEmail();
        if (this.usersRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    @Builder(toBuilder = true)
    @Getter
    public static class  RegisterTenantResponse {
        private Users user;
        private Tenants tenant;
    }
}
