package org.irmalerrr.employeeservice.advice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler  {
//todo - DONE - коменты убираем

    /**
     * Обрабатывает ошибки валидации (например, @Valid).
     * Собирает все ошибки полей в одно сообщение.
     *
     * @param e исключение валидации
     * @return ResponseEntity с сообщениями об ошибках и статусом 400
     */
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body); // todo - DONE - можно использовать билдер) ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Обрабатывает ситуацию, когда запрашиваемый ресурс не найден.
     *
     * @param e исключение NoSuchElementException и его потомки
     * @return ResponseEntity с сообщением об ошибке и статусом 404
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CustomErrorMessage> handleNoSuchElementException(NoSuchElementException e) {
        CustomErrorMessage body = new CustomErrorMessage()
                .setMessage(e.getMessage())
                .setCode(HttpStatus.NOT_FOUND.value())
                .setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Обрабатывает ошибки парсинга JSON (некорректный формат запроса).
     *
     * @param e исключение HttpMessageNotReadableException
     * @return ResponseEntity с сообщением об ошибке и статусом 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorMessage> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {

        CustomErrorMessage body = new CustomErrorMessage()
                .setMessage(e.getMessage())
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Обрабатывает ошибки несоответствия типов параметров запроса.
     * Например, когда ожидается Long, а передана строка.
     *
     * @param e исключение MethodArgumentTypeMismatchException
     * @return ResponseEntity с сообщением об ошибке и статусом 400
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorMessage> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {

        CustomErrorMessage body = new CustomErrorMessage()
                .setMessage(e.getMessage())
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Обрабатывает все остальные ResponseStatusException.
     *
     * @param e исключение ResponseStatusException
     * @param request веб-запрос
     * @return ResponseEntity с сообщением об ошибке и соответствующим статусом
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CustomErrorMessage> handleResponseStatusException(
            ResponseStatusException e, WebRequest request) {

        CustomErrorMessage body = new CustomErrorMessage()
                .setMessage(e.getMessage())
                .setCode(e.getStatusCode().value())
                .setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(e.getStatusCode()).body(body);
    }

    /**
     * Класс DTO для кастомного ответа с ошибкой
     */
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
