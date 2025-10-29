package dev.angelcorzo.neoparking.jwtprovider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties( prefix = "jwt")
public class JwtProperties {
	private String issue;
	private long accessTokenExpiration;
	private long refreshTokenExpiration;
	private String secretKey;
}
