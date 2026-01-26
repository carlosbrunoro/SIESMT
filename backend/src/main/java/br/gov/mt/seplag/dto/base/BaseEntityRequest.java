package br.gov.mt.seplag.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BaseEntityRequest", description = "Modelo padrão para representar o id de um determinado registro.")
public class BaseEntityRequest {

    @NotNull
    private Long id;

}

