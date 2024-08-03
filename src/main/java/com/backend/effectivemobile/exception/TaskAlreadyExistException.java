package com.backend.effectivemobile.exception;

public class TaskAlreadyExistException extends RuntimeException{
    public TaskAlreadyExistException(String message) {
        super(message);
    }
}
