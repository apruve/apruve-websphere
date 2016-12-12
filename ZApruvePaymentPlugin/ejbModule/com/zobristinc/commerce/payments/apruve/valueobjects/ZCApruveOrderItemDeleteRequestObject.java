package com.zobristinc.commerce.payments.apruve.valueobjects;

import com.ibm.commerce.exception.ECException;
import com.ibm.json.java.JSONObject;

public class ZCApruveOrderItemDeleteRequestObject extends ZCApruveRequestObject
{
private String endPoint = "";
	

	public String getEndPoint() {
		return endPoint;
	}


	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
		super.setEndPoint(endPoint);
	}


	@Override
	public JSONObject getJSONRequestObj() throws ECException {
		// TODO Auto-generated method stub
		return null;
	}
}
