package dev.angelcorzo.neoparking.model.authentication.gateway;

import dev.angelcorzo.neoparking.model.users.UserAuthentication;
import java.util.UUID;

public interface AuthenticationContextGateway {
  UserAuthentication getCurrentlyAuthenticatedUser();

  UUID getCurrentTenantId();

  UUID getCurrentUserId();
}
