package com.iplens.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import org.springframework.dao.DuplicateKeyException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<Map<String, Object>> handleDuplicate(IllegalArgumentException ex) {
        return Mono.just(Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<Map<String, Object>> handleDuplicate(DuplicateKeyException ex) {
        return Mono.just(Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public Mono<Map<String, Object>> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return Mono.just(Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Internal Server Error",
                "message", "Unexpected error occurred"
        ));
    }
    
}
