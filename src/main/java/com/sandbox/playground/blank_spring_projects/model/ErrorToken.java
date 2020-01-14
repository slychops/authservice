package com.sandbox.playground.blank_spring_projects.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public final class ErrorToken extends Token {
    private final String noToken = "NO TOKEN RECEIVED";
    private HttpStatus responseStatus;

    public ErrorToken() {
        this.responseStatus = null;
    }

    public ErrorToken(HttpStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
