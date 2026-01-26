package br.gov.mt.seplag.dto.album;

import br.gov.mt.seplag.enumeration.TipoArtista;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta representando um álbum")
public class AlbumResponse {

    @Schema(description = "ID do álbum", example = "1")
    private Long id;

    @Schema(description = "Título do álbum", example = "Acústico ao Vivo")
    private String nome;

    @Schema(description = "Ano de lançamento do álbum", example = "2023")
    private Integer anoLancamento;

    @Schema(description = "Artistas associados ao álbum")
    private Set<ArtistaResponse> artistas;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Artista associado")
    public static class ArtistaResponse {
        @Schema(description = "ID do artista", example = "1")
        private Long id;

        @Schema(description = "Nome do artista", example = "Fulano")
        private String nome;

        @Schema(description = "Tipo do artista (CANTOR/BANDA)", example = "CANTOR")
        private TipoArtista tipo;
    }
}