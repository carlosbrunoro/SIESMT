package br.gov.mt.seplag.mapper;

import br.gov.mt.seplag.dto.album.AlbumRequest;
import br.gov.mt.seplag.dto.album.AlbumResponse;
import br.gov.mt.seplag.entity.Album;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    Album fromRequest(final AlbumRequest request);

    AlbumResponse toResponse(final Album entity);

}