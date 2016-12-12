package com.zobristinc.commerce.payments.apruve.services;

public class ApruveServiceException extends Exception{
	public static final String WARNING     = "WARNING";
	public static final String TRANSACTION = "TRANSACTION";
	public static final String FATAL       = "FATAL";
	public static final String UNEXPECTED  = "UNEXPECTED";
	
	private String level;
	
	public ApruveServiceException(String msg, String level) {
		super(msg);
		this.level = level;
	}
	
	public ApruveServiceException(String msg, Throwable cause, String level) {
		super(msg, cause);
		this.level = level;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}
	
	public String getLevel() {
		return level;
	}
}
