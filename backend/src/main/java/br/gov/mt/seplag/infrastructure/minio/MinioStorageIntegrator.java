package br.gov.mt.seplag.infrastructure.minio;

import br.gov.mt.seplag.core.config.properties.ApplicationProperties;
import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.service.arquivo.StorageIntegrator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Component
public class MinioStorageIntegrator implements StorageIntegrator {

    private final S3Client s3Client;
    private final ApplicationProperties applicationProperties;
    private final S3Presigner s3Presigner;

    public MinioStorageIntegrator(final ApplicationProperties applicationProperties,
                                  final S3Client s3Client,
                                  final S3Presigner s3Presigner) {
        this.applicationProperties = applicationProperties;
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    @Override
    public String upload(final MultipartFile file) {
        try {
            final String sanitizedFilename = isNotBlank(file.getOriginalFilename())
                ? file.getOriginalFilename().replaceAll("\\s+", "_")
                : "file";

            final String key = UUID.randomUUID() + "_" + sanitizedFilename;
            final String bucketName = applicationProperties.getIntegrations().getMinio().getBucketName();

            final PutObjectRequest request = PutObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

            return key;
        } catch (final IOException | S3Exception e) {
            log.error("Falha ao enviar arquivo '{}' para o MinIO", file.getOriginalFilename(), e);

            throw DomainException.persistenceFailure("infra.file.upload.failed", file.getOriginalFilename());
        }
    }

    @Override
    public byte[] download(final String key) {
        try {
            final String bucketName = applicationProperties.getIntegrations().getMinio().getBucketName();

            return s3Client.getObjectAsBytes(b -> b
                .bucket(bucketName)
                .key(key)
            ).asByteArray();

        } catch (final S3Exception e) {
            log.error("Falha ao baixar arquivo '{}' do MinIO", key, e);
            throw DomainException.businessRule("infra.file.download.failed", key);
        }
    }

    @Override
    public void delete(final String key) {
        try {
            final String bucketName = applicationProperties.getIntegrations().getMinio().getBucketName();

            s3Client.deleteObject(b -> b
                .bucket(bucketName)
                .key(key)
            );
        } catch (final S3Exception e) {
            log.error("Falha ao deletar arquivo '{}' do MinIO", key, e);
            throw DomainException.businessRule("infra.file.delete.failed", key);
        }
    }

    @Override
    public String gerarLinkDownloadPreAssinado(final String key) {
        final var minio = applicationProperties.getIntegrations().getMinio();

        final String bucketName = minio.getBucketName();
        final Duration signatureDuration = minio.getSignatureDuration();

        if (signatureDuration.isNegative() || signatureDuration.isZero()) {
            throw DomainException.validation("infra.file.invalid.signature.duration");
        }

        final GetObjectPresignRequest presignRequest = GetObjectPresignRequest
            .builder()
            .signatureDuration(signatureDuration)
            .getObjectRequest(req -> req
                .bucket(bucketName)
                .key(key)
            )
            .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

}
