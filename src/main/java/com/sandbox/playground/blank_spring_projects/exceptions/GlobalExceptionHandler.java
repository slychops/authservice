package com.sandbox.playground.blank_spring_projects.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RichAuthException.InvalidDataWhileEncoding.class)
	public ResponseEntity<?> handleException(RichAuthException.InvalidDataWhileEncoding exception){
		RichAuthResponseBody<Object> responseBody = new  RichAuthResponseBody<>(exception.getCode(), exception.getDescription());
		ResponseEntity<RichAuthResponseBody<?>> errorResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		return errorResponse;	
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException(){
		RichAuthResponseBody<Object> responseBody = new  RichAuthResponseBody<>(1, "500");
		ResponseEntity<RichAuthResponseBody<?>> errorResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		return errorResponse;
	}
}
