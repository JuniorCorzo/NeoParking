package dev.angelcorzo.neoparking.usecase.registertenant;

import dev.angelcorzo.neoparking.model.tenants.Tenants;
import dev.angelcorzo.neoparking.model.tenants.gateways.TenantsRepository;
import dev.angelcorzo.neoparking.model.users.Users;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import dev.angelcorzo.neoparking.model.users.exceptions.EmailAlreadyExistsException;
import dev.angelcorzo.neoparking.model.users.gateways.PasswordEncode;
import dev.angelcorzo.neoparking.model.users.gateways.UsersRepository;
import lombok.RequiredArgsConstructor;

/**
 * Use case for registering a new tenant and an initial owner user.
 *
 * <p>This class handles the business logic for creating a new tenant,
 * associating an owner user with it, and ensuring data integrity.</p>
 *
 * <p><strong>Layer:</strong> Application (Use Case)</p>
 * <p><strong>Responsibility:</strong> To manage the registration process of a new tenant.</p>
 *
 * @author Angel Corzo
 * @since 1.0.0
 * @see Tenants
 * @see Users
 */
@RequiredArgsConstructor
public final class RegisterTenantUseCase {
  private final UsersRepository usersRepository;
  private final TenantsRepository tenantsRepository;
  private final PasswordEncode passwordEncode;

  /**
   * Registers a new tenant and creates an associated owner user.
   *
   * @param user The initial {@link Users} object for the tenant owner.
   * @param tenant The {@link Tenants} object representing the new tenant.
   * @return The newly created {@link Users} object for the tenant owner, now associated with the tenant.
   * @throws EmailAlreadyExistsException if the email of the provided user already exists in the system.
   */
  public Users register(final Users user, final Tenants tenant) {
    this.validateEmailExists(user);

    final Tenants tenantCreated = this.tenantsRepository.save(tenant);

    final String passwordEncrypted = this.passwordEncode.encrypt(user.getPassword());
    user.setTenant(tenantCreated);
    user.setRole(Roles.OWNER);
    user.setPassword(passwordEncrypted);

    final Users userCreated = this.usersRepository.save(user);
    userCreated.setTenant(tenantCreated); // Ensure the returned user has the created tenant set

    return userCreated;
  }

  /**
   * Validates if the email of the provided user already exists in the system.
   *
   * @param user The {@link Users} object whose email is to be validated.
   * @throws EmailAlreadyExistsException if the email already exists.
   */
  private void validateEmailExists(Users user) {
    final String email = user.getEmail();
    if (this.usersRepository.existsByEmail(email)) {
      throw new EmailAlreadyExistsException(email);
    }
  }
}
