package br.gov.mt.seplag.core.config.minio;

import br.gov.mt.seplag.core.config.properties.ApplicationProperties;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    private final ApplicationProperties applicationProperties;

    public MinioConfig(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public MinioClient minioClient() {
        final String endpoint = applicationProperties.getIntegrations().getMinio().getEndpoint();
        final String accessKey = applicationProperties.getIntegrations().getMinio().getAccessKey();
        final String secretKey = applicationProperties.getIntegrations().getMinio().getSecretKey();

        return MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build();
    }

}
