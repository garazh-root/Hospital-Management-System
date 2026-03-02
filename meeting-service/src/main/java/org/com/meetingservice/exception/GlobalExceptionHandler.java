package org.com.meetingservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleServiceUnavailableException(ServiceUnavailableException ex) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Service unavailable, {}", ex.getMessage());
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ScheduleNotAvailableException.class)
    public ResponseEntity<Map<String, String>> handleScheduleNotAvailableException(ScheduleNotAvailableException ex) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Schedule not available, {}", ex.getMessage());
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MeetingNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMeetingNotFoundException(MeetingNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Meeting not found, {}", ex.getMessage());
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<Map<String, String>> handleInvalidStatusException(InvalidStatusException ex) {
        Map<String, String> errors = new HashMap<>();

        log.warn("Invalid status, {}", ex.getMessage());
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}