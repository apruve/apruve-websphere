package com.zobristinc.commerce.payments.apruve.services;

import com.ibm.commerce.payments.plugin.PluginException;

public class ApruveServiceErrorException extends  PluginException {
private ApruveServiceError error;
	
	/**
	 * Class constructor specifying one or more errors
	 * 
	 * @param error the array of errors that caused this exception 
	 * to be thrown 
	 */
	public ApruveServiceErrorException(ApruveServiceError error) {
		super();
		super.setMessageKey("APRUVE_ERROR_" + error.getErrorCode());
		this.error = error;
	}
	
	/**
	 * Returns the array of errors for this exception
	 * 
	 * @return the array of errors that caused this exception
	 */
	public ApruveServiceError getError() {
		return error;
	}
	
	
}
