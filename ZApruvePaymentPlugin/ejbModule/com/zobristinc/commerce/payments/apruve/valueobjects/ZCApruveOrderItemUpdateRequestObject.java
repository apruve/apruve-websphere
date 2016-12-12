package com.zobristinc.commerce.payments.apruve.valueobjects;

import com.ibm.commerce.exception.ECException;
import com.ibm.json.java.JSONObject;

public class ZCApruveOrderItemUpdateRequestObject extends ZCApruveRequestObject
{
	private String endPoint = "";
	private Integer tax_cents;
	private Integer price_ea_cents;
	private Integer quantity;
	private Integer price_total_cents;
	private String expire_at = "";
	private String title = "";
	private String merchant_notes = "";
	private String description = "";
	private String sku = "";
	private String variant_info = "";
	private String vendor = "";
	private String product_url = "";
	
	
	
	public String getEndPoint()
	{
		return endPoint;
	}



	public void setEndPoint(String endPoint)
	{
		this.endPoint = endPoint;
	}



	public Integer getTax_cents()
	{
		return tax_cents;
	}



	public void setTax_cents(Integer tax_cents)
	{
		this.tax_cents = tax_cents;
	}



	public Integer getPrice_ea_cents()
	{
		return price_ea_cents;
	}



	public void setPrice_ea_cents(Integer price_ea_cents)
	{
		this.price_ea_cents = price_ea_cents;
	}



	public Integer getQuantity()
	{
		return quantity;
	}



	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}



	public Integer getPrice_total_cents()
	{
		return price_total_cents;
	}



	public void setPrice_total_cents(Integer price_total_cents)
	{
		this.price_total_cents = price_total_cents;
	}



	public String getExpire_at()
	{
		return expire_at;
	}



	public void setExpire_at(String expire_at)
	{
		this.expire_at = expire_at;
	}



	public String getTitle()
	{
		return title;
	}



	public void setTitle(String title)
	{
		this.title = title;
	}



	public String getMerchant_notes()
	{
		return merchant_notes;
	}



	public void setMerchant_notes(String merchant_notes)
	{
		this.merchant_notes = merchant_notes;
	}



	public String getDescription()
	{
		return description;
	}



	public void setDescription(String description)
	{
		this.description = description;
	}



	public String getSku()
	{
		return sku;
	}



	public void setSku(String sku)
	{
		this.sku = sku;
	}



	public String getVariant_info()
	{
		return variant_info;
	}



	public void setVariant_info(String variant_info)
	{
		this.variant_info = variant_info;
	}



	public String getVendor()
	{
		return vendor;
	}



	public void setVendor(String vendor)
	{
		this.vendor = vendor;
	}



	public String getProduct_url()
	{
		return product_url;
	}



	public void setProduct_url(String product_url)
	{
		this.product_url = product_url;
	}



	@Override
	public JSONObject getJSONRequestObj() throws ECException {
		//set values here
		//create a json object here and put values in it
		JSONObject obj = new JSONObject();
		JSONObject updateOrderItemObj = new JSONObject();
		try {
		
			if(getPrice_ea_cents() != null)
				obj.put("price_ea_cents", getPrice_ea_cents());//(int) Double.parseDouble(odb.getTotalProductPrice())*100
		    if(getQuantity() != null)
		    	obj.put("quantity", getQuantity());
		      if(getPrice_total_cents() != null)
		    	  obj.put("price_total_cents", getPrice_total_cents());
		      if(getCurrency() != null && !"".equals(getCurrency()))
		    	  obj.put("currency", getCurrency());
		      if(getTitle() != null && !"".equals(getTitle()))
		    	  obj.put("title", getTitle());
		      if(getMerchant_notes() != null && !"".equals(getMerchant_notes()))
		    	  obj.put("merchant_notes", getMerchant_notes());
		      if(getDescription() != null && !"".equals(getDescription()))
		    	  obj.put("description", getDescription());
		      if(getSku() != null && !"".equals(getSku()))
		    	  obj.put("sku", getSku());
		      if(getVariant_info() != null && !"".equals(getVariant_info()))
		    	  obj.put("variant_info", getVariant_info());
		      if(getVendor() != null && !"".equals(getVendor()))
		    	  obj.put("vendor", getVendor());
		      if(getProduct_url() != null && !"".equals(getProduct_url()))
		    	  obj.put("product_url", getProduct_url());
		      //updateOrderItemObj.put("order", obj);
	     }
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("updateOrderItemObj is : "+obj);
		return obj;
	}
}
