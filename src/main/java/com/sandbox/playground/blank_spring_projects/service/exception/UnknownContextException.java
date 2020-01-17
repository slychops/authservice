package com.sandbox.playground.blank_spring_projects.service.exception;

public class UnknownContextException extends Exception {
    public UnknownContextException() {
        this("Unknown context requested, could not find redirect.");
    }

    public UnknownContextException(String message) {
        super(message);
    }
}
