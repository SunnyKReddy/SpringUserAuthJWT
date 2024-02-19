package com.learnleadgrow.springauth.userauthservice.exception;

public class LoginSessionsLimitException extends RuntimeException{
    public LoginSessionsLimitException(String message) {
        super(message);
    }

}
