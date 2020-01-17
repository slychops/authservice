package com.sandbox.playground.blank_spring_projects.model;

import lombok.Getter;

@Getter
public final class ErrorToken extends Token {
    private final String noToken = "NO TOKEN RECEIVED";
    private String responseStatus;
    private Exception exception;

    public ErrorToken() {
    }

    public ErrorToken(String statusCode) {
        this.responseStatus = statusCode;
    }

    public ErrorToken(String statusCode, Exception e) {
        this.exception = e;
        this.responseStatus = statusCode;
    }
}
