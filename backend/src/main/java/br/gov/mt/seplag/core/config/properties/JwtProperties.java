package br.gov.mt.seplag.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String issuer;
    private Duration accessTokenExpiration;
    private Duration refreshTokenExpiration;
    private String secret;

}

