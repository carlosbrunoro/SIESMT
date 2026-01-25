package br.gov.mt.seplag.repository;

import br.gov.mt.seplag.entity.Usuario;
import br.gov.mt.seplag.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {

    @Query("""
            SELECT u
            FROM Usuario u
            WHERE u.username = :username
        """)
    Optional<Usuario> findByUsername(final String username);

}