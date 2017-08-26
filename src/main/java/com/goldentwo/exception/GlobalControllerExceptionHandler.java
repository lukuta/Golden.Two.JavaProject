package com.goldentwo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleNotFoundException(NotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("code", HttpStatus.NOT_FOUND.toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleBadRequestException(BadRequestException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("code", HttpStatus.BAD_REQUEST.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
