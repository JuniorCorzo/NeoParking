package dev.angelcorzo.neoparking.usecase.validatetoken;

import dev.angelcorzo.neoparking.model.authentication.gateway.AuthenticationGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidateAccessTokenUseCase {
  private final AuthenticationGateway authenticationGateway;

  public void validate(String accessToken) {
     this.authenticationGateway.validateToken(accessToken);
  }
}
