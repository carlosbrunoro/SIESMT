package br.gov.mt.seplag.core.exception;

public class EmailException extends RuntimeException {

    public EmailException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmailException(final String message) {
        super(message);
    }

}
