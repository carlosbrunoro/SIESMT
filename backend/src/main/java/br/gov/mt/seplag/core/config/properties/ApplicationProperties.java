package br.gov.mt.seplag.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private String version;
    private Security security;

    @Getter
    @Setter
    public static class Security {
        private Jwt jwt;

        @Getter
        @Setter
        public static class Jwt {
            private String secret;
        }
    }

}

