package com.zobristinc.commerce.integration.apruve.commands;

public class OrderItemsDetail {
	
	String description;
	String title;
	String sku;
	Integer amount_cents;
	Integer quantity;
	String vendor;
	String variant;
	Integer price;
	String orderItemType;
	
	
	public String getOrderItemType()
	{
		return orderItemType;
	}
	public void setOrderItemType(String orderItemType)
	{
		this.orderItemType = orderItemType;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getAmount_cents() {
		return amount_cents;
	}
	public void setAmount_cents(Integer amount_cents) {
		this.amount_cents = amount_cents;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
