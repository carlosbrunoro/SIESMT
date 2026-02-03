package br.gov.mt.seplag.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private String version;
    private String endpoint;
    private Integrations integrations;
    private String name;
    private String description;
    private String env;
    private Scheduler scheduler;
    private RateLimit rateLimit;

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
            private Duration signatureDuration;
        }

        @Getter
        @Setter
        public static class Argus {
            private String endpoint;
        }
    }

    @Getter
    @Setter
    public static class Scheduler {
        private RegionalSync regionalSync;

        @Getter
        @Setter
        public static class RegionalSync {
            private Boolean enabled;
            private String cron;
        }
    }

    @Getter
    @Setter
    public static class RateLimit {
        private Integer requests;
        private Duration duration;
        private Duration cacheTtl;
    }

}

