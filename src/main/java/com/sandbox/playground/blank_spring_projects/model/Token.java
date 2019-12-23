package com.sandbox.playground.blank_spring_projects.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {
    private String token_type;
    private String access_token;
}
