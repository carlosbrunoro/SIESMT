package br.gov.mt.seplag.dto.album;

import br.gov.mt.seplag.dto.base.BaseEntityRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados para criação/atualização de álbum")
public class AlbumRequest {

    @Schema(description = "Título do álbum", example = "Acústico ao Vivo")
    @NotBlank
    private String nome;

    @Schema(description = "Ano de lançamento do álbum", example = "2023")
    @NotNull
    private Integer anoLancamento;

    @Valid
    @Schema(description = "IDs dos artistas associados ao álbum")
    private Set<BaseEntityRequest> artistas;

}