package br.gov.mt.seplag.repository;

import br.gov.mt.seplag.entity.ImagemAlbum;
import br.gov.mt.seplag.repository.base.BaseRepository;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImagemAlbumRepository extends BaseRepository<ImagemAlbum, Long> {

    @NonNull
    Optional<ImagemAlbum> findById(@NonNull final Long id);

}