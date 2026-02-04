package br.gov.mt.seplag.repository;

import br.gov.mt.seplag.entity.Artista;
import br.gov.mt.seplag.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistaRepository extends BaseRepository<Artista, Long> {

    @Query("""
            SELECT a
            FROM Artista a
            WHERE (:nome IS NULL OR lower(a.nome) LIKE :nome)
        """)
    Page<Artista> findAll(final String nome, final Pageable pageable);

}