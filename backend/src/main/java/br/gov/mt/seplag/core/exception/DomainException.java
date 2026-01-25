package br.gov.mt.seplag.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Object[] args;

    public DomainException(final String messageCode,
                           final String errorCode,
                           final HttpStatus httpStatus,
                           final Object... args) {
        super(messageCode);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    public static DomainException validation(final String messageCode, final Object... args) {
        return new DomainException(messageCode, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST, args);
    }

    public static DomainException businessRule(final String messageCode, final Object... args) {
        return new DomainException(messageCode, "BUSINESS_RULE_VIOLATION", HttpStatus.UNPROCESSABLE_ENTITY, args);
    }

    public static DomainException notFound(final String messageCode, final Object... args) {
        return new DomainException(messageCode, "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND, args);
    }

    public static DomainException conflict(final String messageCode, final Object... args) {
        return new DomainException(messageCode, "CONFLICT", HttpStatus.CONFLICT, args);
    }

    public static DomainException unauthorized(final String messageCode, final Object... args) {
        return new DomainException(messageCode, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED, args);
    }

    public static DomainException persistenceFailure(final String messageCode, final Object... args) {
        return new DomainException(messageCode, "PERSISTENCE_FAILURE", HttpStatus.INTERNAL_SERVER_ERROR, args);
    }

}