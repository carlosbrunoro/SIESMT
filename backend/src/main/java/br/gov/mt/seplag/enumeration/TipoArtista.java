package br.gov.mt.seplag.enumeration;

import br.gov.mt.seplag.dto.base.EnumResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum TipoArtista {
    CANTOR("CANTOR", "Cantor"),
    BANDA("BANDA", "Banda");

    private final String codigo;
    private final String descricao;

    public static List<EnumResponse> toResponse() {
        return Arrays.stream(values())
            .map(v -> EnumResponse.builder()
                .codigo(v.getCodigo())
                .descricao(v.getDescricao())
                .build()
            )
            .toList();
    }
}