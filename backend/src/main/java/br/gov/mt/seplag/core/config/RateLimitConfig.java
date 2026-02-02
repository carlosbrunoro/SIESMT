package br.gov.mt.seplag.core.config;

import br.gov.mt.seplag.core.config.properties.ApplicationProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Cache<String, Bucket> rateLimitCache(final ApplicationProperties properties) {
        return Caffeine.newBuilder()
            .expireAfterAccess(properties.getRateLimit().getCacheTtl())
            .build();
    }

    @Bean
    public Bandwidth rateLimitBandwidth(final ApplicationProperties applicationProperties) {
        final var rateLimit = applicationProperties.getRateLimit();

        return Bandwidth.simple(
            rateLimit.getRequests(),
            rateLimit.getDuration());
    }

}