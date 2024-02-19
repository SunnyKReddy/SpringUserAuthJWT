package com.learnleadgrow.springauth.userauthservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleException(LoginSessionsLimitException loginSessionsLimitException) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(loginSessionsLimitException.getMessage());
        exceptionMessage.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleException(RuntimeException runtimeException) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setMessage(runtimeException.getMessage());
        return new ResponseEntity(exceptionMessage, HttpStatus.NOT_ACCEPTABLE);
    }
}
