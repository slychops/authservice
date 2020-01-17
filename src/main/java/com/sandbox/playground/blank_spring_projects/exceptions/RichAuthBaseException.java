package com.sandbox.playground.blank_spring_projects.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RichAuthBaseException extends Exception {

	
	 private int code;
	 private String description;
	 
//	 public int getCode() {
//		 return code;
//	 }
//	 
//	 public String getDescription() {
//		 return description;
//	 }
//	 
//	 public RichAuthBaseException(int code, String desccription) {
//		 this.code = code;
//		 this.description = description;
//	 }
}
