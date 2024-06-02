package com.ai.xstack.kolesnyk.handler;

import com.ai.xstack.kolesnyk.exception.EntityNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class NotFoundExceptionHandler {

    @ExceptionHandler(EntityNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> entityNotFound(EntityNotFound e) {
        return Map.of("message", e.getMessage());
    }

}
