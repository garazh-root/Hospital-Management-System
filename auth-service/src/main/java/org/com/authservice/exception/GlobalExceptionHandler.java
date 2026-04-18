package org.com.authservice.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((fieldError) ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handler(UsernameNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Username not found {}", ex.getMessage());

        errors.put("username", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handler(EmailAlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Email already exists {}", ex.getMessage());

        errors.put("email", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, String>> handler(JwtException je) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Jwt exception {}", je.getMessage());

        errors.put("jwt", je.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}