package br.gov.mt.seplag.service.artista;

import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.entity.Artista;
import br.gov.mt.seplag.repository.ArtistaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Service
public class ArtistaService {

    private static final String RESOURCE_ARTISTA = "Artista";
    private final ArtistaRepository repository;

    public ArtistaService(final ArtistaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Artista buscarPorId(final Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public void existsById(final Long id) {
        if (isFalse(repository.existsById(id))) {
            throw DomainException.notFound("validation.entity.not.found", RESOURCE_ARTISTA, id);
        }
    }

    private Artista findById(final Long id) {
        return repository.findById(id).orElseThrow(() ->
            DomainException.notFound("validation.entity.not.found", RESOURCE_ARTISTA, id)
        );
    }

}