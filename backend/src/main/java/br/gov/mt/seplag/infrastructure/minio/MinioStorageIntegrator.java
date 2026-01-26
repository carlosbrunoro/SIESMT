package br.gov.mt.seplag.infrastructure.minio;

import br.gov.mt.seplag.core.config.properties.ApplicationProperties;
import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.service.arquivo.StorageIntegrator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Component
public class MinioStorageIntegrator implements StorageIntegrator {

    private final S3Client s3Client;
    private final ApplicationProperties applicationProperties;

    protected MinioStorageIntegrator(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;

        final String endpoint = applicationProperties.getIntegrations().getMinio().getEndpoint();
        final String accessKey = applicationProperties.getIntegrations().getMinio().getAccessKey();
        final String secretKey = applicationProperties.getIntegrations().getMinio().getSecretKey();

        s3Client = S3Client
            .builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.US_EAST_1)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .build();
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

            throw DomainException.businessRule("infra.file.upload.failed", file.getOriginalFilename());
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

}
