package br.gov.mt.seplag.service.usuario;

import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.entity.Usuario;
import br.gov.mt.seplag.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    private static final String RESOURCE_USUARIO = "Usuário";
    private final UsuarioRepository repository;

    public UsuarioService(final UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(final Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByUsername(final String login) {
        return repository.findByUsername(login);
    }

    private Usuario findById(final Long id) {
        return repository.findById(id).orElseThrow(() ->
            DomainException.notFound("validation.entity.not.found", RESOURCE_USUARIO, id)
        );
    }

}