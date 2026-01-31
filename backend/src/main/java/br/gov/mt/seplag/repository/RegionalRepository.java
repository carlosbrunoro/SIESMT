package br.gov.mt.seplag.repository;

import br.gov.mt.seplag.entity.Regional;
import br.gov.mt.seplag.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionalRepository extends BaseRepository<Regional, Long> {

    @Query("""
            SELECT r
            FROM Regional r
            WHERE (:nome IS NULL OR LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
        """)
    Page<Regional> listarPor(final String nome, final Pageable pageable);

    List<Regional> findAllByAtivoTrue();
}