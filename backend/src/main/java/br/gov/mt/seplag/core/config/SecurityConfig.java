package br.gov.mt.seplag.core.config;

import br.gov.mt.seplag.core.config.properties.JwtProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/v3/api-docs/**",
                    "/api/v1/authenticate",
                    "/api/v1/refresh-token",
                    "/actuator/health",
                    "/public/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
            "http://localhost:3000"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setMaxAge(3600L);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public JwtDecoder jwtDecoder(final SecretKey jwtSigningKey) {
        return NimbusJwtDecoder
            .withSecretKey(jwtSigningKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(final SecretKey jwtSigningKey) {
        final OctetSequenceKey jwk = new OctetSequenceKey.Builder(jwtSigningKey)
            .keyID("HS256-key")
            .algorithm(com.nimbusds.jose.JWSAlgorithm.HS256)
            .build();

        final JWKSet jwkSet = new JWKSet(jwk);
        final JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("scope");
        authoritiesConverter.setAuthorityPrefix("");

        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }

    @Bean
    public SecretKey jwtSigningKey(final JwtProperties jwtProperties) {
        final String secret = jwtProperties.getSecret();
        final byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

}