package br.gov.mt.seplag.core.security.service;

import br.gov.mt.seplag.core.exception.DomainException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Service
public class JwtService {
    private static final String ISSUER = "spring-security-jwt";
    private static final Duration EXPIRY_TIME = Duration.ofMinutes(50);
    private static final Duration REFRESH_TIME = Duration.ofHours(4);
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final Clock clock;

    public JwtService(final JwtEncoder encoder,
                      final JwtDecoder decoder,
                      final Clock clock) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.clock = clock;
    }

    public Jwt generateAccessToken(final String username, final String authorities) {
        final Instant now = Instant.now(clock);

        final JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(ISSUER)
            .issuedAt(now)
            .expiresAt(now.plus(EXPIRY_TIME))
            .subject(username)
            .claim("scope", authorities)
            .build();

        final JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
    }

    public String generateRefreshToken(final String username) {
        final Instant now = Instant.now(clock);

        final JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(ISSUER)
            .issuedAt(now)
            .expiresAt(now.plus(REFRESH_TIME))
            .subject(username)
            .claim("type", "refresh")
            .build();

        final JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String validateAndGetUsernameFromRefreshToken(final String refreshToken) throws DomainException {
        try {
            final var jwt = decoder.decode(refreshToken);

            if (isFalse("refresh".equals(jwt.getClaims().get("type")))) {
                throw DomainException.unauthorized("auth.refresh.token.invalid");
            }

            return jwt.getSubject();

        } catch (final DomainException e) {
            // Re-lança DomainException para manter o errorCode específico
            throw e;
        } catch (final Exception e) {
            // Captura exceptions genéricas e converte para DomainException
            throw DomainException.unauthorized("auth.refresh.token.expired");
        }
    }
}