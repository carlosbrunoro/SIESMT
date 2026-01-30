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
    private String endpoint;
    private Security security;
    private Integrations integrations;
    private String name;
    private String description;
    private String env;

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

    @Getter
    @Setter
    public static class Integrations {
        private Minio minio;
        private Argus argus;

        @Getter
        @Setter
        public static class Minio {
            private String endpoint;
            private String bucketName;
            private String accessKey;
            private String secretKey;
        }

        @Getter
        @Setter
        public static class Argus {
            private String endpoint;
        }
    }

}

