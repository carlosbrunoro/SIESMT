package br.gov.mt.seplag.controller;

import br.gov.mt.seplag.dto.auth.request.AuthRequest;
import br.gov.mt.seplag.dto.auth.request.RefreshTokenRequest;
import br.gov.mt.seplag.dto.auth.response.AuthResponse;
import br.gov.mt.seplag.dto.base.RefreshTokenResponse;
import br.gov.mt.seplag.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @Operation(
        summary = "Autenticar usuário",
        description = "Realiza a autenticação do usuário e retorna um access token JWT com expiração de 5 minutos e um refresh token."
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid final AuthRequest request) {
        final AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Renovar access token",
        description = "Gera um novo access token JWT a partir de um refresh token válido."
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Valid final RefreshTokenRequest request) {
        final var response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

}