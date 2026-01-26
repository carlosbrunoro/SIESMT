package br.gov.mt.seplag.service.album;

import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.entity.Album;
import br.gov.mt.seplag.repository.AlbumRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static br.gov.mt.seplag.core.query.QueryParamUtils.likeContainsIgnoreCase;
import static io.micrometer.common.util.StringUtils.isBlank;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Service
public class AlbumService {

    private static final String RESOURCE_ALBUM = "Album";
    private static final String NOME = "nome";
    private final AlbumRepository repository;

    public AlbumService(final AlbumRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Album create(final Album request) {
        if (request == null || isBlank(request.getNome())) {
            throw DomainException.businessRule("validation.field.required", NOME);
        }

        validateNomeUnico(request);

        return repository.save(request);
    }

    @Transactional
    public Album update(final Long id, final Album request) {
        request.setId(id);
        validateNomeUnico(request);

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

}