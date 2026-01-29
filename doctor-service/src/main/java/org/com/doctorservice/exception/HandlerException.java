package org.com.doctorservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class HandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmptyModelException.class)
    public ResponseEntity<Map<String, String>> handleEmptyDoctorException(EmptyModelException ex) {
        Map<String, String> errors = new HashMap<>();
        log.warn("Empty entity received{}", ex.getMessage());

        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmptyComponentException.class)
    public ResponseEntity<Map<String, String>> handleEmptyComponentException(EmptyComponentException ex) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Empty component received{}", ex.getMessage());
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDoctorNotFoundException(DoctorNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Doctor not found{}", ex.getMessage());
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Email already exists{}", ex.getMessage());
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmptyScheduleException.class)
    public ResponseEntity<Map<String, String>> handleEmptyScheduleException(EmptyScheduleException ex) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Empty schedule received{}", ex.getMessage());
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleScheduleNotFoundException(ScheduleNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Schedule not found{}", ex.getMessage());
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}