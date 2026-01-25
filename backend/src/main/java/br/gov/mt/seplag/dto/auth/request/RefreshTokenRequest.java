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
@Schema(description = "Dados para renovação do token de acesso")
public class RefreshTokenRequest {
    @Schema(
        description = "Refresh token JWT válido",
        example = "eyJhbGciOiJIUzI1NiJ9..."
    )
    @NotBlank
    private String refreshToken;

}