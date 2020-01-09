package com.sandbox.playground.blank_spring_projects.exceptions;

public class InvalidValuesPassedForBase64EncodingException extends RuntimeException{

    public InvalidValuesPassedForBase64EncodingException(String message){
        super(message);
    }
}
