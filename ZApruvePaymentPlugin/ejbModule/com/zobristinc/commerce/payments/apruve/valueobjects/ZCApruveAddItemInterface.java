package com.zobristinc.commerce.payments.apruve.valueobjects;

import java.math.BigDecimal;
import java.util.List;

import com.zobristinc.commerce.integration.apruve.commands.OrderItemsDetail;

public interface ZCApruveAddItemInterface {
	public void addItem(OrderItemsDetail itemObject);
	public void setItemAmount(BigDecimal itemAmount) ;
	public void setTaxAmount(BigDecimal taxAmount) ;
	public void setShippingCharge(BigDecimal shippingChargeAmount) ;
	public void setShippingDiscount(BigDecimal shippingDiscountAmount) ;
	public void setItemDiscount(BigDecimal itemDiscount);
	public void clearItems();
	public List getItems() ;
}
