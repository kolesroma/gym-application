package com.epam.ai.xstack.kolesnyk.handler;

import com.epam.ai.xstack.kolesnyk.exception.BruteForceTryingException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AuthorizationExceptionHandler {

    private static final String WRONG_LOGIN_OR_PASSWORD = "Wrong login or password. You have only 5 attempts to log in";

    @ExceptionHandler(BruteForceTryingException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> bruteForce() {
        return Map.of("message", WRONG_LOGIN_OR_PASSWORD);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> accessDenied(AccessDeniedException e) {
        return Map.of("message", e.getMessage());
    }

}
