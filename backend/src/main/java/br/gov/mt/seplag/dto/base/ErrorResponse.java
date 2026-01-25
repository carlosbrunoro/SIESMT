package br.gov.mt.seplag.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String errorCode;
    private List<ValidationError> details;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
        private Object rejectedValue;
    }

    public static ErrorResponse of(final int status,
                                   final String error,
                                   final String message,
                                   final String path) {
        return new ErrorResponse(
            LocalDateTime.now(), status, error, message, path, null, null
        );
    }

    public static ErrorResponse of(final int status,
                                   final String error,
                                   final String message,
                                   final String path,
                                   final String errorCode) {
        return new ErrorResponse(
            LocalDateTime.now(), status, error, message, path, errorCode, null
        );
    }
}