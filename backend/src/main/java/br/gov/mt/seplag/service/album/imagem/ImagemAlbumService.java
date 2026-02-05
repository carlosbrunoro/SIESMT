package br.gov.mt.seplag.service.album.imagem;

import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.entity.Album;
import br.gov.mt.seplag.entity.Arquivo;
import br.gov.mt.seplag.entity.ImagemAlbum;
import br.gov.mt.seplag.repository.ImagemAlbumRepository;
import br.gov.mt.seplag.service.arquivo.ArquivoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Service
public class ImagemAlbumService {

    private final ImagemAlbumRepository repository;
    private final ArquivoService arquivoService;

    private static final Set<String> IMAGE_CONTENT_TYPES = Set.of(
        "image/jpeg",
        "image/png",
        "image/webp"
    );

    public ImagemAlbumService(final ImagemAlbumRepository repository,
                              final ArquivoService arquivoService) {
        this.repository = repository;
        this.arquivoService = arquivoService;
    }

    @Transactional
    public void salvarCapas(final Album album, final MultipartFile[] files) {
        validarImagens(files);

        for (final MultipartFile file : files) {
            final Arquivo arquivo = arquivoService.uploadArquivo(file);

            try {
                final ImagemAlbum imagem = ImagemAlbum
                    .builder()
                    .album(album)
                    .arquivo(arquivo)
                    .build();

                repository.save(imagem);
            } catch (final Exception ex) {
                if (nonNull(arquivo)) {
                    arquivoService.deletarArquivo(arquivo);
                }

                throw ex;
            }
        }
    }

    private void validarImagens(final MultipartFile[] files) {
        if (isNull(files) || files.length == 0) {
            throw DomainException.validation("business.file.empty");
        }

        for (final MultipartFile imagem : files) {
            validarContentType(imagem);
        }
    }

    private void validarContentType(final MultipartFile file) {
        if (isNull(file.getContentType()) || isFalse(IMAGE_CONTENT_TYPES.contains(file.getContentType()))) {
            throw DomainException.validation("business.file.not.image", file.getOriginalFilename());
        }
    }

}