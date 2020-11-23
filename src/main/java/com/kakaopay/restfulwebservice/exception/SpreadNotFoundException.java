package com.kakaopay.restfulwebservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SpreadNotFoundException extends RuntimeException {
    public SpreadNotFoundException(ExceptionStatus message) {
        super(message.getDescription());
    }
}
