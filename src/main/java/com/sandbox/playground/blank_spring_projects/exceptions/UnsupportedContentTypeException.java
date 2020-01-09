package com.sandbox.playground.blank_spring_projects.exceptions;

public class UnsupportedContentTypeException extends Exception {
    public UnsupportedContentTypeException() {
    }

    public UnsupportedContentTypeException(String message) {
        super(message);
    }
}
