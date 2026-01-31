package br.gov.mt.seplag.mapper;

import br.gov.mt.seplag.entity.Regional;
import br.gov.mt.seplag.infrastructure.integracao.regional.RegionalIntegracaoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegionalMapper {

    RegionalIntegracaoResponse toResponse(final Regional entity);

}