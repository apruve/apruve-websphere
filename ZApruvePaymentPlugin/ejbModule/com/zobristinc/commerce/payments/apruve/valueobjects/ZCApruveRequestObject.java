package com.zobristinc.commerce.payments.apruve.valueobjects;

import java.math.BigDecimal;
import java.util.Map;




import com.ibm.commerce.exception.ECException;
import com.ibm.json.java.JSONObject;
import com.zobristinc.commerce.payments.apruve.util.ZCApruveConstants;

public abstract class ZCApruveRequestObject implements ZCApruveConstants {
	private String method;
	private String ecToken;
	private String currencyCode;
	private BigDecimal amount;
	String apruveInterface ;

	//	private String merchantId = "thenorthface";
	private String orderId;
	private String endPoint = "";
	private String currency;
	
	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getEndPoint() {
		return endPoint;
	}


	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}


	private Map additionalParameters;
	JSONObject jsonObj = null;
	
	public String toString()
	{
		try
		{
			return getJSONObj().toString();
		}
		catch(Exception e )
		{
			return "";
		}
		
	}
	
	
	public String getApruveInterface() {
		return apruveInterface;
	}
	public void setApruveInterface(String apruveInterface) {
		this.apruveInterface = apruveInterface;
	}
	public abstract JSONObject getJSONRequestObj() throws ECException ;
	
	
	public void resetJsonObj()
	{
		jsonObj = null;
	}
	public  JSONObject getJSONObj() throws ECException
	{
		//TODO
		 jsonObj = new JSONObject();
		 jsonObj = getJSONRequestObj();
		/* nvps = new HashMap();
		 nvps.putAll(getApruveExpressNVPs());
		 if ( additionalParameters != null )
			nvps.putAll(additionalParameters);*/
		return jsonObj;

	}
	

	/**
	 * @return Returns the amount.
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * @return Returns the currencyCode.
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode The currencyCode to set.
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	/**
	 * @return Returns the method.
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method The method to set.
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return Returns the token.
	 */
	public String getECToken() {
		return ecToken;
	}
	/**
	 * @param token The token to set.
	 */
	public void setECToken(String token) {
		this.ecToken = token;
	}
//	public void setMerchantId(String merchantId) {
//		this.merchantId = merchantId;
//	}
//	public String getMerchantId() {
//		return merchantId;
//	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setAdditionalParameters(Map additionalParameters) {
		this.additionalParameters = additionalParameters;
	}
	public Map getAdditionalParameters() {
		return additionalParameters;
	}


}
