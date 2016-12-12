package com.zobristinc.commerce.payments.apruve.valueobjects;



import com.ibm.commerce.exception.ECException;
import com.ibm.json.java.JSONObject;

public class ZCApruveOrderUpdateRequestObject extends ZCApruveRequestObject{
private String endPoint = "";
private Integer tax_cents;
private Integer shipping_cents;
private String expire_at = "";


	public String getExpire_at()
	{
		return expire_at;
	}
	
	
	public void setExpire_at(String expire_at)
	{
		this.expire_at = expire_at;
	}


	public Integer getTax_cents()
	{
		return tax_cents;
	}
	
	
	public void setTax_cents(Integer tax_cents)
	{
		this.tax_cents = tax_cents;
	}
	
	
	public Integer getShipping_cents()
	{
		return shipping_cents;
	}
	
	
	public void setShipping_cents(Integer shipping_cents)
	{
		this.shipping_cents = shipping_cents;
	}

	
	public String getEndPoint() {
		return endPoint;
	}


	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
		super.setEndPoint(endPoint);
	}


	@Override
	public JSONObject getJSONRequestObj() throws ECException {
		//set values here
		//create a json object here and put values in it
		JSONObject obj = new JSONObject();
		JSONObject updateOrderObj = new JSONObject();
		try {
		
			if(getAmount() != null)
				obj.put("amount_cents", getAmount().intValue());//(int) Double.parseDouble(odb.getTotalProductPrice())*100
		      //obj.put("currency", getCurrency());
		      if(getTax_cents() != null)
		    	  obj.put("tax_cents", getTax_cents());
		      if(getShipping_cents() != null)
		    	  obj.put("shipping_cents", getShipping_cents());
		      if(getExpire_at() != null && !getExpire_at().equals(""))
		    	  obj.put("expire_at", getExpire_at());
		      updateOrderObj.put("order", obj);
	     }
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("updateOrderObj is : "+updateOrderObj);
		return updateOrderObj;
	}
}
