package br.gov.mt.seplag.service.regional;

import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.entity.Regional;
import br.gov.mt.seplag.repository.RegionalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static br.gov.mt.seplag.core.query.QueryParamUtils.likeContainsIgnoreCase;

@Service
public class RegionalService {

    private static final String RESOURCE_REGIONAL = "Regional";
    private final RegionalRepository repository;

    public RegionalService(final RegionalRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Regional buscarPorId(final Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Regional> listarPor(final String nomeRegional,
                                    final Pageable pageable) {
        final String nome = likeContainsIgnoreCase(nomeRegional);
        return repository.listarPor(nome, pageable);
    }

    @Transactional
    public List<Regional> findAllByAtivoTrue() {
        return repository.findAllByAtivoTrue();
    }

    @Transactional
    public Regional save(final Regional regional) {
        return repository.save(regional);
    }

    private Regional findById(final Long id) {
        return repository.findById(id).orElseThrow(() ->
            DomainException.notFound("validation.entity.not.found", RESOURCE_REGIONAL, id)
        );
    }

}