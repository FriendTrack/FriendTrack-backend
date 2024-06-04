package com.ciklon.friendtracker.common.exception.handler;

import com.ciklon.friendtracker.common.exception.ApiError;
import com.ciklon.friendtracker.common.exception.CustomException;
import com.ciklon.friendtracker.common.exception.ExceptionType;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@Slf4j
@Component
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ApiError> handle(CustomException e) {
        log.error("CustomException occurred: {}", e.getMessage());
        ApiError apiError = new ApiError(getStatus(e.getType()), e.getMessage());
        return new ResponseEntity<>(apiError, getStatus(e.getType()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiError> handle(Exception e) {
        log.error("Exception occurred: {}", e.getMessage());
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ApiError> handle(AuthenticationException e) {
        log.error("AuthenticationCredentialsNotFoundException occurred: {}", e.getMessage());
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "Invalid credentials");
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({JwtException.class})
    public ResponseEntity<ApiError> handle(JwtException e) {
        log.error("JwtException occurred: {}", e.getMessage());
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "Invalid token");
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ServletException.class})
    public ResponseEntity<ApiError> handle(ServletException e) {
        log.error("ServletException occurred: {}", e.getMessage());
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiError> handle(AccessDeniedException e) {
        log.error("AccessDeniedException occurred: {}", e.getMessage());
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    private HttpStatus getStatus(ExceptionType type) {
        return switch (type) {
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case BAD_REQUEST, ILLEGAL -> HttpStatus.BAD_REQUEST;
            case ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case FATAL -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
