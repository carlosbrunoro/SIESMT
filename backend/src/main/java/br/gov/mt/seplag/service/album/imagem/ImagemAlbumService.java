package br.gov.mt.seplag.service.album.imagem;

import br.gov.mt.seplag.entity.Album;
import br.gov.mt.seplag.entity.Arquivo;
import br.gov.mt.seplag.entity.ImagemAlbum;
import br.gov.mt.seplag.repository.ImagemAlbumRepository;
import br.gov.mt.seplag.service.arquivo.ArquivoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.nonNull;

@Service
public class ImagemAlbumService {

    private final ImagemAlbumRepository repository;
    private final ArquivoService arquivoService;

    public ImagemAlbumService(final ImagemAlbumRepository repository,
                              final ArquivoService arquivoService) {
        this.repository = repository;
        this.arquivoService = arquivoService;
    }

    @Transactional
    public void salvarCapas(final Album album, final MultipartFile[] files) {
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

}