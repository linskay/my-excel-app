package com.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class ExcelProcessingException extends ResponseStatusException {

    private final String message;
    private final HttpStatus status;

    public ExcelProcessingException(String message, HttpStatus status) {
        super(status, message);
        this.message = message;
        this.status = status;
    }
}