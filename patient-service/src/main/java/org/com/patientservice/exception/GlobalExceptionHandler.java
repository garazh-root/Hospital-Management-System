package org.com.patientservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmptyEntityException.class)
    public ResponseEntity<Map<String, String>> handler(EmptyEntityException exception) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Empty entity received {}", exception.getMessage());
        errors.put("message", exception.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmptyComponentException.class)
    public ResponseEntity<Map<String, String>> handler(EmptyComponentException exception) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Empty component received {}", exception.getMessage());
        errors.put("message", exception.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handler(EmailAlreadyExistsException exception) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Email already exists {}", exception.getMessage());
        errors.put("message", exception.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, String>> handler(PatientNotFoundException exception) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Patient not found {}", exception.getMessage());
        errors.put("message", exception.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }

    public ResponseEntity<Map<String, String>> handler(NotMatchingRoleExcpetion exception) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Not matching role excpetion {}", exception.getMessage());
        errors.put("message", exception.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }

}