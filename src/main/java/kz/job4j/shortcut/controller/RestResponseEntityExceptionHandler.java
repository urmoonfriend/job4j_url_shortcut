package kz.job4j.shortcut.controller;

import kz.job4j.shortcut.exceptions.SiteAlreadyExistsException;
import kz.job4j.shortcut.exceptions.SiteNotFoundException;
import kz.job4j.shortcut.exceptions.UsernameAlreadyExistsException;
import kz.job4j.shortcut.model.dto.ResultMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({SiteAlreadyExistsException.class, SiteNotFoundException.class, UsernameAlreadyExistsException.class})
    public ResponseEntity<ResultMessage<String>> handleCustomExceptions(Exception ex) {
        String errorMessage = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(ResultMessage.failure(errorMessage));
    }
}