package com.sandbox.playground.blank_spring_projects.service.exception;

public class InsufficientResourceException extends Exception{

    public InsufficientResourceException(String message) {
        super(message);
    }

    public InsufficientResourceException() {
    }
}
