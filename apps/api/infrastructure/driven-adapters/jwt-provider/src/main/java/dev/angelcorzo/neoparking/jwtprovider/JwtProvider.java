package dev.angelcorzo.neoparking.jwtprovider;

import dev.angelcorzo.neoparking.jwtprovider.config.JwtProperties;
import dev.angelcorzo.neoparking.jwtprovider.config.RSAProperties;
import dev.angelcorzo.neoparking.model.authentication.exceptions.ExpiredTokenException;
import dev.angelcorzo.neoparking.model.authentication.exceptions.MalformedTokenException;
import dev.angelcorzo.neoparking.model.authentication.exceptions.TokenInvalidException;
import dev.angelcorzo.neoparking.model.authentication.gateway.AuthenticationGateway;
import dev.angelcorzo.neoparking.model.exceptions.TokenErrorMessages;
import dev.angelcorzo.neoparking.model.users.gateways.UsersRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtProvider implements AuthenticationGateway {
  private final JwtProperties jwtProperties;
  private final RSAProperties rsaProperties;
  private final RSAPrivateKey privateKey;
  private final RSAPublicKey publicKey;

  @Override
  public String generateAccessToken(Map<String, Object> claims) {
    Instant expiredTime = Instant.now().plusSeconds(jwtProperties.getAccessTokenExpiration());

    return Jwts.builder()
        .header()
        .type("JWT")
        .keyId(rsaProperties.getKeyId())
        .and()
        .issuer(jwtProperties.getIssue())
        .expiration(Date.from(expiredTime))
        .notBefore(Date.from(Instant.now()))
        .issuedAt(new Date())
        .claims(claims)
        .signWith(privateKey, Jwts.SIG.RS256)
        .compact();
  }

  @Override
  public String generateRefreshToken(Map<String, Object> claims) {
    final Date expiredTime =
        Date.from(Instant.now().plusSeconds(jwtProperties.getRefreshTokenExpiration()));

    return Jwts.builder()
        .header()
        .keyId(rsaProperties.getKeyId())
        .and()
        .issuer(jwtProperties.getIssue())
        .issuedAt(new Date())
        .expiration(expiredTime)
        .notBefore(new Date())
        .claims(claims)
        .encryptWith(publicKey, Jwts.KEY.RSA_OAEP_256, Jwts.ENC.A256GCM)
        .compact();
  }

  @Override
  public Map<String, String> extractTokenClaims(String token) {
    final int tokenParts = token.split("\\.").length;

    return switch (tokenParts) {
      case 3 -> this.extractClaims(token);
      case 5 -> this.extractEncryptedClaims(token);
      default -> throw new MalformedTokenException();
    };
  }

  @Override
  public Optional<String> extractClaim(String token, String claimName) {
    return Optional.of(this.extractTokenClaims(token).get(claimName));
  }

  @Override
  public Optional<String> extractEmail(String token) {
    return Optional.of(this.extractTokenClaims(token).get("email"));
  }

  @Override
  public void validateToken(String accessToken) {
    try {
      final Map<String, String> claims = this.extractTokenClaims(accessToken);

      Optional.ofNullable(claims.get("sub"))
          .orElseThrow(
              () -> new TokenInvalidException(TokenErrorMessages.INVALID_TOKEN.toString()));

      Optional.ofNullable(claims.get("tenantId"))
          .orElseThrow(
              () -> new TokenInvalidException(TokenErrorMessages.INVALID_TOKEN.toString()));

      Optional.ofNullable(claims.get("role"))
          .orElseThrow(
              () -> new TokenInvalidException(TokenErrorMessages.INVALID_TOKEN.toString()));

    } catch (ExpiredJwtException e) {
      log.warn("Token expired: {}", e.getMessage());
      throw new ExpiredTokenException();
    } catch (MalformedJwtException e) {
      log.warn("Malformed token: {}", e.getMessage());
      throw new MalformedTokenException(e);
    } catch (UnsupportedJwtException | SecurityException | IllegalArgumentException e) {
      log.warn("Invalid token: {}", e.getMessage());
      throw new TokenInvalidException(TokenErrorMessages.INVALID_TOKEN.toString(), e);
    }
  }

  private Map<String, String> extractClaims(String token) {
    return Jwts.parser()
        .verifyWith(publicKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
  }

  private Map<String, String> extractEncryptedClaims(String token) {
    return Jwts.parser()
        .decryptWith(privateKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
  }
}
