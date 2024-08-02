package com.backend.effectivemobile.exception;

public class IncorrectPasswordsException extends RuntimeException{
    public IncorrectPasswordsException(String message) {
        super(message);
    }
}
