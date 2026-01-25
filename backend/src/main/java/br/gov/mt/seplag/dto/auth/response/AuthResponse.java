package br.gov.mt.seplag.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta de autenticação com tokens JWT")
public class AuthResponse {
    @Schema(description = "Access token JWT")
    private String accessToken;

    @Schema(description = "Refresh token JWT")
    private String refreshToken;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String tokenType;

    @Schema(description = "Data/hora de expiração do access token")
    private Instant expiresAt;
}