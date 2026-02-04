package br.gov.mt.seplag.repository;

import br.gov.mt.seplag.entity.Album;
import br.gov.mt.seplag.repository.base.BaseRepository;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends BaseRepository<Album, Long> {

    @EntityGraph(attributePaths = {"artistas", "imagens", "imagens.arquivo"})
    @Query("""
        SELECT a
        FROM Album a
        JOIN a.artistas artista
        WHERE (:nomeArtista IS NULL OR LOWER(artista.nome) LIKE :nomeArtista)
        AND (:flagCantores IS NULL
            OR (:flagCantores = TRUE AND EXISTS (
                    SELECT 1 FROM a.artistas ar2
                    WHERE ar2.tipo = br.gov.mt.seplag.enumeration.TipoArtista.CANTOR
               ))
            OR (:flagCantores = FALSE AND NOT EXISTS (
                    SELECT 1 FROM a.artistas ar2
                    WHERE ar2.tipo = br.gov.mt.seplag.enumeration.TipoArtista.CANTOR
               ))
          )
        
        AND (:flagBandas IS NULL
            OR (:flagBandas = TRUE AND EXISTS (
                    SELECT 1 FROM a.artistas ar3
                    WHERE ar3.tipo = br.gov.mt.seplag.enumeration.TipoArtista.BANDA
               ))
            OR (:flagBandas = FALSE AND NOT EXISTS (
                    SELECT 1 FROM a.artistas ar3
                    WHERE ar3.tipo = br.gov.mt.seplag.enumeration.TipoArtista.BANDA
               ))
          )
        """)
    Page<Album> listarPor(final String nomeArtista,
                          final Boolean flagCantores,
                          final Boolean flagBandas,
                          final Pageable pageable
    );

    @NonNull
    @EntityGraph(attributePaths = {"artistas", "imagens", "imagens.arquivo"})
    @Override
    Optional<Album> findById(@NonNull final Long id);

}