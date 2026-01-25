package br.gov.mt.seplag.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class ApplicationConfig {

    @Value("${cache.expiration.minutes:10}")
    private int tempoDeExpiracaoMinutos;

    @Bean
    public Clock clock() {
        final ZoneId zone = ZoneId.of("America/Sao_Paulo");
        log.info("Configurando Clock com timezone: {}", zone);
        return Clock.system(zone);
    }

    @Bean
    public CaffeineCacheManager cacheManager() {
        final CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(tempoDeExpiracaoMinutos, TimeUnit.MINUTES)
            .recordStats());

        log.info("CacheManager configurado: {}", cacheManager.getClass().getName());
        log.info("Expiração: {} minutos", tempoDeExpiracaoMinutos);

        return cacheManager;
    }

}
