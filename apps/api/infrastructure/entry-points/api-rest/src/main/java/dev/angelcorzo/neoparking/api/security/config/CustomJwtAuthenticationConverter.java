package dev.angelcorzo.neoparking.api.security.config;

import dev.angelcorzo.neoparking.model.authentication.exceptions.TokenInvalidException;
import dev.angelcorzo.neoparking.model.exceptions.TokenErrorMessages;
import dev.angelcorzo.neoparking.usecase.validatetoken.ValidateAccessTokenUseCase;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter
    implements Converter<Jwt, AbstractAuthenticationToken> {

  private final ValidateAccessTokenUseCase validateAccessTokenUseCase;

  @Override
  public AbstractAuthenticationToken convert(Jwt source) {
    log.debug("Converting JWT to Authentication");
    validateAccessTokenUseCase.validate(source.getTokenValue());
    String userId = source.getSubject();
    String tenantId = source.getClaimAsString("tenantId");
    String role = source.getClaimAsString("role");

    if (userId == null || tenantId == null || role == null)
      throw new TokenInvalidException(TokenErrorMessages.INVALID_TOKEN.toString());

    List<SimpleGrantedAuthority> authority =
        Collections.singletonList(new SimpleGrantedAuthority(role));

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userId, source, authority);

    log.debug("Converted JWT to Authentication: {}", authenticationToken);
    return authenticationToken;
  }
}
