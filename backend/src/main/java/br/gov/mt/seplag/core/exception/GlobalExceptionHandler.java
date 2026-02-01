package br.gov.mt.seplag.core.exception;

import br.gov.mt.seplag.core.message.MessageService;
import br.gov.mt.seplag.dto.base.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageService messageService;

    public GlobalExceptionHandler(final MessageService messageService) {
        this.messageService = messageService;
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(final DomainException ex, final WebRequest request) {
        final String path = getPathFromRequest(request);
        final HttpStatus status = ex.getHttpStatus();

        final ErrorResponse errorResponse = ErrorResponse.of(
            status.value(),
            messageService.toLocale("error.business.title"),
            messageService.toLocale(ex.getMessage(), ex.getArgs()),
            path,
            ex.getErrorCode()
        );

        log.warn("Domain exception: {}", ex.getMessage());

        return ResponseEntity
            .status(errorResponse.getStatus())
            .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(final MethodArgumentNotValidException ex, final WebRequest request) {
        final String path = getPathFromRequest(request);

        final List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::mapToValidationError)
            .toList();

        final ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            messageService.toLocale("error.badrequest.title"),
            messageService.toLocale("validation.general.message"),
            path,
            "VALIDATION_ERROR"
        );

        errorResponse.setDetails(validationErrors);

        log.warn("Validation errors: {}", validationErrors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex,
                                                                               final WebRequest request) {
        final String path = getPathFromRequest(request);

        final String detailMessage = isNotBlank(ex.getMessage()) && ex.getMessage().contains("Required request body is missing")
            ? "Corpo da requisição é obrigatório"
            : "Corpo da requisição inválido";

        final ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            messageService.toLocale("error.badrequest.title"),
            messageService.toLocale("validation.data.invalid"),
            path,
            "INVALID_REQUEST_BODY"
        );

        errorResponse.setDetails(List.of(
            new ErrorResponse.ValidationError("requestBody", detailMessage, null)
        ));

        log.warn("Invalid request body: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(final RuntimeException ex,
                                                                       final WebRequest request) {
        final String path = getPathFromRequest(request);

        final ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.UNAUTHORIZED.value(),
            messageService.toLocale("error.auth.title"),
            ex.getMessage(),
            path,
            "AUTHENTICATION_FAILED"
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(final Exception ex, final WebRequest request) {
        final String path = getPathFromRequest(request);

        final ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            messageService.toLocale("error.server.title"),
            messageService.toLocale("error.internal.server"),
            path,
            "INTERNAL_ERROR"
        );

        log.error("Internal error occurred at path {}: ", path, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(final WebRequest request) {
        final String path = getPathFromRequest(request);
        final String httpMethod = getHttpMethodFromRequest(request);

        log.debug("Recurso/Endpoint não encontrado: {} {}", httpMethod, path);

        final ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.NOT_FOUND.value(),
            messageService.toLocale("error.notfound.title"),
            messageService.toLocale("error.endpoint.notfound", httpMethod, path),
            path,
            "ENDPOINT_NOT_FOUND"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(final HttpRequestMethodNotSupportedException ex,
                                                                  final WebRequest request) {
        final String path = getPathFromRequest(request);
        final String httpMethod = getHttpMethodFromRequest(request);

        log.debug("Método HTTP não suportado: {} {}", httpMethod, path);

        final ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.METHOD_NOT_ALLOWED.value(),
            messageService.toLocale("error.method.not.allowed.title"),
            messageService.toLocale(
                "error.method.not.allowed",
                httpMethod,
                ex.getSupportedHttpMethods()
            ),
            path,
            "METHOD_NOT_ALLOWED"
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDenied(final WebRequest request) {
        final String path = getPathFromRequest(request);

        final ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.FORBIDDEN.value(),
            messageService.toLocale("error.forbidden.title"),
            messageService.toLocale("auth.access.denied"),
            path,
            "ACCESS_DENIED"
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(final DataIntegrityViolationException ex,
                                                                      final WebRequest request) {
        final String path = getPathFromRequest(request);
        log.error("Data integrity violation at {}: {}", path, ex.getMessage());

        final ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            messageService.toLocale("error.badrequest.title"),
            messageService.toLocale("error.data.integrity"),
            path,
            "DATA_INTEGRITY_VIOLATION"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipartException(final MultipartException ex,
                                                                  final WebRequest request) {
        final String path = getPathFromRequest(request);

        final ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            messageService.toLocale("error.badrequest.title"),
            messageService.toLocale("validation.multipart.required"),
            path,
            "MULTIPART_REQUEST_REQUIRED"
        );

        errorResponse.setDetails(List.of(
            new ErrorResponse.ValidationError(
                "file",
                messageService.toLocale("validation.multipart.file.required"),
                null
            )
        ));

        log.warn("Multipart request expected but not received at {}: {}", path, ex.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
                                                                         final WebRequest request) {
        final String path = getPathFromRequest(request);

        final ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            messageService.toLocale("error.badrequest.title"),
            messageService.toLocale("validation.multipart.required"),
            path,
            "MULTIPART_PART_REQUIRED"
        );

        errorResponse.setDetails(List.of(
            new ErrorResponse.ValidationError(
                ex.getRequestPartName(),
                messageService.toLocale("validation.multipart.file.required"),
                null
            )
        ));

        log.warn("Parte de arquivo ausente na requisição em {}: {}", path, ex.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
                                                                         final WebRequest request) {
        final String path = getPathFromRequest(request);

        final String unsupported = nonNull(ex.getContentType()) ? ex.getContentType().toString() : "desconhecido";
        final String supported = isEmpty(ex.getSupportedMediaTypes())
            ? "nenhum"
            : ex.getSupportedMediaTypes().toString();

        final ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
            messageService.toLocale("error.unsupported.media.type.title"),
            messageService.toLocale("error.unsupported.media.type.detail", unsupported, supported),
            path,
            "UNSUPPORTED_MEDIA_TYPE"
        );

        log.warn("Content-Type não suportado no endpoint {}: {}, suportados: {}", path, unsupported, supported);

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    private String getPathFromRequest(final WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    private String getHttpMethodFromRequest(final WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getMethod();
    }

    private ErrorResponse.ValidationError mapToValidationError(final FieldError fieldError) {
        return new ErrorResponse.ValidationError(
            fieldError.getField(),
            messageService.toLocale(fieldError.getDefaultMessage(), fieldError.getField()),
            fieldError.getRejectedValue()
        );
    }
}