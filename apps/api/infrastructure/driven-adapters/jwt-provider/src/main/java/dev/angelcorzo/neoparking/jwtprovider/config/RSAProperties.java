package dev.angelcorzo.neoparking.jwtprovider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Data
@Configuration
@ConfigurationProperties(prefix = "rsa")
public class RSAProperties {
  private Resource publicKey;
  private Resource privateKey;
  private String keyId;
}
