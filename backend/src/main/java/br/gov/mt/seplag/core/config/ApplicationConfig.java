package br.gov.mt.seplag.core.config;

import br.gov.mt.seplag.core.config.properties.ApplicationProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
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

    @Bean
    public OpenAPI customOpenAPI(final ApplicationProperties applicationProperties) {
        return new OpenAPI()
            .addServersItem(
                new Server().url(applicationProperties.getEndpoint())
            )
            .components(
                new Components()
                    .addSecuritySchemes(
                        "bearer-jwt",
                        new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .in(SecurityScheme.In.HEADER)
                            .name("Authorization")
                    )
            )
            .addSecurityItem(
                new SecurityRequirement().addList("bearer-jwt")
            )
            .info(
                new Info()
                    .title(applicationProperties.getName())
                    .version(
                        applicationProperties.getEnv() + " - " +
                            "(" + applicationProperties.getVersion() + ")"
                    )
                    .description(applicationProperties.getDescription())
            );
    }

}
