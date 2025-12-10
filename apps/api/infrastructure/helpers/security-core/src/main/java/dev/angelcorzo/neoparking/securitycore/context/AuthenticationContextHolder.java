package dev.angelcorzo.neoparking.securitycore.context;

import dev.angelcorzo.neoparking.model.authentication.gateway.AuthenticationContextGateway;
import dev.angelcorzo.neoparking.model.users.UserAuthentication;
import dev.angelcorzo.neoparking.model.users.exceptions.UserAuthenticationContextInvalidException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@Configuration
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AuthenticationContextHolder implements AuthenticationContextGateway {

  @Override
  public UserAuthentication getCurrentlyAuthenticatedUser() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated())
      throw new UserAuthenticationContextInvalidException();

    final Object principal = authentication.getPrincipal();

    if (principal instanceof UserAuthentication userAuthentication) return userAuthentication;

    throw new UserAuthenticationContextInvalidException();
  }

  @Override
  public UUID getCurrentTenantId() {
    return this.getCurrentlyAuthenticatedUser().tenantId();
  }

  @Override
  public UUID getCurrentUserId() {
    return this.getCurrentlyAuthenticatedUser().userId();
  }
}
