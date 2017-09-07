package com.goldentwo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final String MESSAGE = "message";
    private static final String CODE = "code";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleNotFoundException(NotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE, ex.getMessage());
        response.put(CODE, HttpStatus.NOT_FOUND.toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleBadRequestException(BadRequestException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE, ex.getMessage());
        response.put(CODE, HttpStatus.BAD_REQUEST.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity handleUnauthorizedUserException(UnauthorizedUserException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE, ex.getMessage());
        response.put(CODE, HttpStatus.UNAUTHORIZED.toString());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BetNotAllowedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity handleUnauthorizedUserException(BetNotAllowedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE, ex.getMessage());
        response.put(CODE, HttpStatus.FORBIDDEN.toString());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
