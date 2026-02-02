package br.gov.mt.seplag.core.config;

import br.gov.mt.seplag.core.message.MessageService;
import com.github.benmanes.caffeine.cache.Cache;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Cache<String, Bucket> cache;
    private final Bandwidth bandwidth;
    private final MessageService messageService;

    public RateLimitFilter(final Cache<String, Bucket> cache,
                           final Bandwidth bandwidth,
                           final MessageService messageService) {
        this.cache = cache;
        this.bandwidth = bandwidth;
        this.messageService = messageService;
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {

        final String key = isNotBlank(request.getRemoteUser())
            ? request.getRemoteUser()
            : request.getRemoteAddr();

        final Bucket bucket = resolveBucket(key);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
            "{\"error\": \"" +
                messageService.toLocale("error.rate.limit.exceeded") +
                "\"}"
        );
    }

    private Bucket resolveBucket(final String key) {
        return cache.get(key, k ->
            Bucket.builder()
                .addLimit(bandwidth)
                .build()
        );
    }

}

