package com.example.btportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateCompanyException extends RuntimeException {
    public DuplicateCompanyException(String message) {
        super(message);
    }
}