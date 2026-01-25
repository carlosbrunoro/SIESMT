package br.gov.mt.seplag.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados para autenticação do usuário")
public class AuthRequest {
    @Schema(
        description = "Nome de usuário utilizado para autenticação",
        example = "admin"
    )
    @NotBlank
    private String username;

    @Schema(
        description = "Senha do usuário",
        example = "senha123"
    )
    @NotBlank
    private String password;
}
