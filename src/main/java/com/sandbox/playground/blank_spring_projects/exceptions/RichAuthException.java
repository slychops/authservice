package com.sandbox.playground.blank_spring_projects.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

public class RichAuthException {

	/*
	 * When you don't understand what to throw, throw me!!
	 * */
	
	static public class SomethingWentWrong extends RichAuthBaseException{
		
		public SomethingWentWrong() {
			super(500, "Internal Server error");
			
		}

		private static final long serialVersionUID = 12123123123123131L;
		
	} 
	
	static public  class InvalidDataWhileEncoding extends RichAuthBaseException {
		
		public InvalidDataWhileEncoding() {
			super(401,"Error occured while encoding string for Base64");
		}
	}
}
