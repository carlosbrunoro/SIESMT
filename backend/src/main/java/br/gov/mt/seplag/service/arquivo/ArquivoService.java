package br.gov.mt.seplag.service.arquivo;

import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.dto.arquivo.ArquivoDownloadResponse;
import br.gov.mt.seplag.entity.Arquivo;
import br.gov.mt.seplag.repository.ArquivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArquivoService {
    private static final String RESOURCE_ARQUIVO = "Arquivo";
    private final StorageIntegrator storageIntegrator;
    private final ArquivoRepository arquivoRepository;

    public ArquivoService(final StorageIntegrator storageIntegrator,
                          final ArquivoRepository arquivoRepository) {
        this.storageIntegrator = storageIntegrator;
        this.arquivoRepository = arquivoRepository;
    }

    @Transactional
    public Arquivo uploadArquivo(final MultipartFile file) {
        final String key = storageIntegrator.upload(file);

        try {
            final Arquivo arquivo = Arquivo
                .builder()
                .nomeOriginal(file.getOriginalFilename())
                .storageKey(key)
                .tipoMime(file.getContentType())
                .tamanho(file.getSize())
                .build();

            return arquivoRepository.save(arquivo);
        } catch (final Exception ex) {
            storageIntegrator.delete(key);
            throw DomainException.persistenceFailure("business.file.save.failed", file.getOriginalFilename());
        }
    }

    @Transactional(readOnly = true)
    public Arquivo buscarPorId(final Long id) {
        return findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deletarArquivo(final Arquivo arquivo) {
        final String r2StorageKey = arquivo.getStorageKey();

        arquivoRepository.delete(arquivo);
        storageIntegrator.delete(r2StorageKey);
    }

    @Transactional(readOnly = true)
    public ArquivoDownloadResponse downloadArquivo(final Long id) {
        final Arquivo arquivo = findById(id);

        final byte[] dados = storageIntegrator.download(arquivo.getStorageKey());
        return new ArquivoDownloadResponse(dados, arquivo.getNomeOriginal(), arquivo.getTipoMime());
    }

    private Arquivo findById(final Long id) {
        return arquivoRepository.findById(id).orElseThrow(() ->
            DomainException.notFound("validation.entity.not.found", RESOURCE_ARQUIVO, id)
        );
    }

}
