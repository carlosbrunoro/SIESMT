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
            select a
            from Album a
            left join a.artistas artista
            where (:nomeArtista is null or lower(artista.nome) like :nomeArtista)
            and (:flagCantores is null or (:flagCantores = true and exists (select 1 from a.artistas ar2 where ar2.tipo = br.gov.mt.seplag.enumeration.TipoArtista.CANTOR)) )
            and (:flagBandas is null or (:flagBandas = true and exists (select 1 from a.artistas ar3 where ar3.tipo = br.gov.mt.seplag.enumeration.TipoArtista.BANDA)) )
        """)
    Page<Album> listarPor(final String nomeArtista,
                          final Boolean flagCantores,
                          final Boolean flagBandas,
                          final Pageable pageable);

    @NonNull
    @EntityGraph(attributePaths = {"artistas", "imagens", "imagens.arquivo"})
    @Override
    Optional<Album> findById(@NonNull final Long id);

}