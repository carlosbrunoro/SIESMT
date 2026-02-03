package br.gov.mt.seplag.service.album;

import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.entity.Album;
import br.gov.mt.seplag.entity.Artista;
import br.gov.mt.seplag.event.AlbumCriadoEvent;
import br.gov.mt.seplag.repository.AlbumRepository;
import br.gov.mt.seplag.service.album.imagem.ImagemAlbumService;
import br.gov.mt.seplag.service.artista.ArtistaService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static br.gov.mt.seplag.core.query.QueryParamUtils.likeContainsIgnoreCase;
import static io.micrometer.common.util.StringUtils.isBlank;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Service
public class AlbumService {

    private static final String RESOURCE_ALBUM = "Album";
    private static final String NOME = "nome";
    private final AlbumRepository repository;
    private final ArtistaService artistaService;
    private final ImagemAlbumService imagemAlbumService;
    private final ApplicationEventPublisher publisher;

    public AlbumService(final AlbumRepository repository,
                        final ArtistaService artistaService,
                        final ImagemAlbumService imagemAlbumService,
                        final ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.artistaService = artistaService;
        this.imagemAlbumService = imagemAlbumService;
        this.publisher = publisher;
    }

    @Transactional
    public Album create(final Album request) {
        if (isNull(request) || isBlank(request.getNome())) {
            throw DomainException.businessRule("validation.field.required", NOME);
        }

        validateNomeUnico(request);
        validaExistenciaArtistas(request);

        final Album album = repository.save(request);
        publisher.publishEvent(new AlbumCriadoEvent(album.getId()));

        return album;
    }

    @Transactional
    public Album update(final Long id, final Album request) {
        request.setId(id);
        validateNomeUnico(request);
        validaExistenciaArtistas(request);

        final Album album = findById(id);
        album.setNome(request.getNome());
        album.setAnoLancamento(request.getAnoLancamento());
        album.setArtistas(request.getArtistas());

        return repository.save(album);
    }

    @Transactional(readOnly = true)
    public Album buscarPorId(final Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Album> listarPor(final String nomeArtista,
                                 final Boolean flagCantores,
                                 final Boolean flagBandas,
                                 final Pageable pageable) {
        final String nome = likeContainsIgnoreCase(nomeArtista);

        return repository.listarPor(nome, flagCantores, flagBandas, pageable);
    }

    @Transactional
    public void adicionarCapasAlbum(final Long idAlbum, final MultipartFile[] files) {
        final Album album = findById(idAlbum);

        imagemAlbumService.salvarCapas(album, files);
    }

    private Album findById(final Long id) {
        return repository.findById(id).orElseThrow(() ->
            DomainException.notFound("validation.entity.not.found", RESOURCE_ALBUM, id)
        );
    }

    private void validateNomeUnico(final Album entity) {
        final Map<String, Object> filtros = Map.of(NOME, entity.getNome());

        if (isTrue(repository.existsBy(entity.getId(), filtros))) {
            throw DomainException.businessRule("business.album.nome.exists", entity.getNome());
        }
    }

    private void validaExistenciaArtistas(final Album request) {
        if (isNotEmpty(request.getArtistas())) {
            for (final Artista artista : request.getArtistas()) {
                artistaService.existsById(artista.getId());
            }
        }
    }

}