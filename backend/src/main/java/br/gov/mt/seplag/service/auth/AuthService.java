package br.gov.mt.seplag.service.auth;

import br.gov.mt.seplag.core.message.MessageService;
import br.gov.mt.seplag.core.security.service.JwtService;
import br.gov.mt.seplag.core.security.service.UserDetailsServiceImpl;
import br.gov.mt.seplag.dto.auth.request.AuthRequest;
import br.gov.mt.seplag.dto.auth.request.RefreshTokenRequest;
import br.gov.mt.seplag.dto.auth.response.AuthResponse;
import br.gov.mt.seplag.dto.base.RefreshTokenResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(final JwtService jwtService,
                       final UserDetailsServiceImpl userDetailsService,
                       final MessageService messageService,
                       final PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(final AuthRequest request) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        if (isFalse(passwordEncoder.matches(request.getPassword(), userDetails.getPassword()))) {
            throw new BadCredentialsException(messageService.toLocale("auth.invalid.credentials"));
        }

        final var authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String authorities = getAuthoritiesAsString(userDetails);
        final Jwt accessToken = jwtService.generateAccessToken(request.getUsername(), authorities);
        final String refreshToken = jwtService.generateRefreshToken(request.getUsername());

        return new AuthResponse(
            accessToken.getTokenValue(),
            refreshToken,
            "Bearer",
            accessToken.getExpiresAt()
        );
    }

    public RefreshTokenResponse refreshToken(final RefreshTokenRequest request) {
        final String username = jwtService.validateAndGetUsernameFromRefreshToken(request.getRefreshToken());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String authorities = getAuthoritiesAsString(userDetails);
        final Jwt newAccessToken = jwtService.generateAccessToken(username, authorities);

        return new RefreshTokenResponse(newAccessToken.getTokenValue(), "Bearer", newAccessToken.getExpiresAt());
    }

    private String getAuthoritiesAsString(final UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    }

}