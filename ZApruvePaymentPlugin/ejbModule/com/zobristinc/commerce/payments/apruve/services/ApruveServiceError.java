package com.zobristinc.commerce.payments.apruve.services;

public class ApruveServiceError {
	private String errorCode;
	private String shortMsg;
	
	public ApruveServiceError(String errorCode, String shortMsg) {
		this.errorCode = errorCode;
		this.shortMsg = shortMsg;
 	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public String getShortMsg() {
		return shortMsg;
	}
	
	public String toString() {
		return errorCode + " - " + shortMsg ;
	}

}
