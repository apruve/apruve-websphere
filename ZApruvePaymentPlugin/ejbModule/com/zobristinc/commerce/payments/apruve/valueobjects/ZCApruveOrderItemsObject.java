package com.zobristinc.commerce.payments.apruve.valueobjects;

public class ZCApruveOrderItemsObject
{
	String description;
	String title;
	String sku;
	Integer totalAmount_cents;
	Integer quantity;
	String vendor;
	String variant;
	Integer price;
	String order_id;
	String productURL;
	String planCode;
	String currency;
	String orderItemId;
	String productImageURL;
	String merchantNotes;
	
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getSku()
	{
		return sku;
	}
	public void setSku(String sku)
	{
		this.sku = sku;
	}
	public Integer getQuantity()
	{
		return quantity;
	}
	public Integer getTotalAmount_cents()
	{
		return totalAmount_cents;
	}
	public void setTotalAmount_cents(Integer totalAmount_cents)
	{
		this.totalAmount_cents = totalAmount_cents;
	}
	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}
	public String getVendor()
	{
		return vendor;
	}
	public void setVendor(String vendor)
	{
		this.vendor = vendor;
	}
	public String getVariant()
	{
		return variant;
	}
	public void setVariant(String variant)
	{
		this.variant = variant;
	}
	public Integer getPrice()
	{
		return price;
	}
	public void setPrice(Integer price)
	{
		this.price = price;
	}
	public String getOrder_id()
	{
		return order_id;
	}
	public void setOrder_id(String order_id)
	{
		this.order_id = order_id;
	}
	public String getProductURL()
	{
		return productURL;
	}
	public void setProductURL(String productURL)
	{
		this.productURL = productURL;
	}
	public String getPlanCode()
	{
		return planCode;
	}
	public void setPlanCode(String planCode)
	{
		this.planCode = planCode;
	}
	public String getCurrency()
	{
		return currency;
	}
	public void setCurrency(String currency)
	{
		this.currency = currency;
	}
	public String getOrderItemId()
	{
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId)
	{
		this.orderItemId = orderItemId;
	}
	public String getProductImageURL()
	{
		return productImageURL;
	}
	public void setProductImageURL(String productImageURL)
	{
		this.productImageURL = productImageURL;
	}
	public String getMerchantNotes()
	{
		return merchantNotes;
	}
	public void setMerchantNotes(String merchantNotes)
	{
		this.merchantNotes = merchantNotes;
	}
	
	
}
