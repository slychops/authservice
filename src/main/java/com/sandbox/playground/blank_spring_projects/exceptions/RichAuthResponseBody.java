package com.sandbox.playground.blank_spring_projects.exceptions;


public class RichAuthResponseBody<T> {

	private T body;
	private int status;
	private String code;
	
	public RichAuthResponseBody(T body, int status, String code) {
		this.body = body;
		this.status = status;
		this .code  = code;
	}
	
	public RichAuthResponseBody(int status, String code) {
		this.body = null;
		this.status = status;
		this .code  = code;
	}
}
