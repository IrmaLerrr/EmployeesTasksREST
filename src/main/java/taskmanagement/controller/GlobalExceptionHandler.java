package taskmanagement.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorMessage> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        CustomErrorMessage body = new CustomErrorMessage()
                .setMessage(errorMessage)
                .setCode(e.getStatusCode().value())
                .setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CustomErrorMessage> handleResponseStatusException(
            ResponseStatusException e, WebRequest request) {

        CustomErrorMessage body = new CustomErrorMessage()
                .setMessage(e.getMessage())
                .setCode(e.getStatusCode().value())
                .setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(body, e.getStatusCode());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CustomErrorMessage> handleNoSuchElementException(NoSuchElementException e) {
        CustomErrorMessage body = new CustomErrorMessage()
                .setMessage(e.getMessage())
                .setCode(HttpStatus.NOT_FOUND.value())
                .setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class CustomErrorMessage {
        @JsonProperty("message")
        private String message;

        @JsonProperty("code")
        private Integer code;

        @JsonProperty("timestamp")
        private LocalDateTime timestamp;
    }
}
