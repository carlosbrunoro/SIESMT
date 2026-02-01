package br.gov.mt.seplag.core.config.minio;

import br.gov.mt.seplag.core.config.properties.ApplicationProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Slf4j
@Component
public class MinioBucketInitializer {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioBucketInitializer(final MinioClient minioClient,
                                  final ApplicationProperties applicationProperties) {
        this.minioClient = minioClient;
        this.bucketName = applicationProperties.getIntegrations().getMinio().getBucketName();
    }

    @PostConstruct
    public void init() {
        try {
            final Boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
            );

            if (isFalse(found)) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                );

                log.info("Bucket '" + bucketName + "' criado com sucesso.");
            }
        } catch (final Exception e) {
            log.error("Erro crítico ao inicializar o MinIO", e);
        }
    }

}
