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
            select a
            from Artista a
            where (:nome is null or lower(a.nome) like :nome)
        """)
    Page<Artista> findAll(final String nome, final Pageable pageable);

}