package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.ErrorApi;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
/**
 * Global exception handler that manages various exceptions
 * across the application.
 * It uses @ControllerAdvice to provide consistent error
 * responses for different exceptions.
 */
@NoArgsConstructor
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Handles generic exceptions (HTTP 500).
     *
     * @param exception The thrown exception.
     * @return ResponseEntity with error details and HTTP status 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApi> handleError(Exception exception) {
        ErrorApi error = buildError(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    /**
     * Handles HTTP client errors (HTTP 400).
     *
     * @param exception The thrown HttpClientErrorException.
     * @return ResponseEntity with error details and HTTP status 400.
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorApi> handleError(HttpClientErrorException exception) {
        ErrorApi error = buildError(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    /**
     * Handles validation errors (HTTP 400).
     *
     * @param exception The thrown MethodArgumentNotValidException.
     * @return ResponseEntity with error details and HTTP status 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> handleError(MethodArgumentNotValidException exception) {
        ErrorApi error = buildError(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    /**
     * Handles ResponseStatusException with custom HTTP status.
     *
     * @param exception The thrown ResponseStatusException.
     * @return ResponseEntity with error details and appropriate HTTP status.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorApi> handleError(ResponseStatusException exception) {
        ErrorApi error = buildError(exception.getReason(), HttpStatus.valueOf(exception.getStatusCode().value()));
        return ResponseEntity.status(exception.getStatusCode()).body(error);
    }
    /**
     * Handles EntityNotFoundException (HTTP 404).
     *
     * @param exception The thrown EntityNotFoundException.
     * @return ResponseEntity with error details and HTTP status 404.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorApi> handleError(EntityNotFoundException exception) {
        ErrorApi error = buildError(exception.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    /**
     * Builds an ErrorApi object with the provided message and status.
     *
     * @param message The error message.
     * @param status The HTTP status.
     * @return An ErrorApi object with the error details.
     */
    private ErrorApi buildError(String message, HttpStatus status) {
        return ErrorApi.builder()
                .timestamp(String.valueOf(Timestamp.from(ZonedDateTime.now().toInstant())))
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(message)
                .build();
    }

}
