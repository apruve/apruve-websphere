package com.zobristinc.commerce.payments.apruve.valueobjects;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ibm.commerce.exception.ECException;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.zobristinc.commerce.integration.apruve.commands.OrderItemsDetail;

public class ZCApruveOrderCreateInvoiceRequestObject extends ZCApruveRequestObject implements ZCApruveAddItemInterface{
	private String endPoint = "";
	private boolean issueOnCreate;
	private Integer shipping_cents;
	private Integer tax_cents;
	private String merchant_notes = "";
	private String merchant_invoice_id = "";
	private String due_at = "";

	ArrayList<OrderItemsDetail> items ;
	private BigDecimal itemAmount;
	private BigDecimal taxAmount;
	private BigDecimal shippingCharge;
	private BigDecimal shippingDiscount;
	private BigDecimal itemDiscount = BigDecimal.ZERO;
	
	public boolean getIssueOnCreate() {
		return issueOnCreate;
	}

	public void setIssueOnCreate(boolean issueOnCreate) {
		this.issueOnCreate = issueOnCreate;
	}
	
	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	
	public int getShipping_cents()
	{
		return shipping_cents;
	}

	public void setShipping_cents(int shipping_cents)
	{
		this.shipping_cents = shipping_cents;
	}

	public int getTax_cents()
	{
		return tax_cents;
	}

	public void setTax_cents(int tax_cents)
	{
		this.tax_cents = tax_cents;
	}

	public String getMerchant_notes()
	{
		return merchant_notes;
	}

	public void setMerchant_notes(String merchant_notes)
	{
		this.merchant_notes = merchant_notes;
	}

	public String getMerchant_invoice_id()
	{
		return merchant_invoice_id;
	}

	public void setMerchant_invoice_id(String merchant_invoice_id)
	{
		this.merchant_invoice_id = merchant_invoice_id;
	}

	public String getDue_at()
	{
		return due_at;
	}

	public void setDue_at(String due_at)
	{
		this.due_at = due_at;
	}

	@Override
	public JSONObject getJSONRequestObj() throws ECException {
		//set values here
		//create a json object here and put values in it
		JSONObject obj = new JSONObject();
		try {
		
			if(getAmount() != null)
				obj.put("amount_cents", getAmount());//(int) Double.parseDouble(odb.getTotalProductPrice())*100
	      if(getCurrency() != null && !"".equals(getCurrency()))
	    	  obj.put("currency", getCurrency());
	      obj.put("tax_cents", getTax_cents());
	      obj.put("shipping_cents", getShipping_cents());
	      if(getMerchant_notes() != null && !"".equals(getMerchant_notes()))
	    	  obj.put("merchant_notes", getMerchant_notes());
	      if(getMerchant_invoice_id() != null && !"".equals(getMerchant_invoice_id()))
	    	  obj.put("merchant_invoice_id", getMerchant_invoice_id());
	      if(getDue_at() != null && !"".equals(getDue_at()))
	    	  obj.put("due_at", getDue_at());
	      
	      JSONArray jArray = new JSONArray();
	      ArrayList<OrderItemsDetail> oidList = items;
			OrderItemsDetail orderItemDetail = null;
			if(oidList != null)
			{
				System.out.println("order items list size is : "+oidList.size());
				for(int i = 0;i < oidList.size();i++)
				{
					orderItemDetail = oidList.get(i);
				    JSONObject studentJSON = new JSONObject();
				    if(orderItemDetail.getDescription() != null)
				    {
				    	studentJSON.put("description", orderItemDetail.getDescription());
				    }
			        else
			        {
			           studentJSON.put("description", "");
			        }
			        if(orderItemDetail.getTitle() != null)
			        {
				       studentJSON.put("title", orderItemDetail.getTitle());
			        }
			        else{
			          studentJSON.put("title", "");
			        }
			        if(orderItemDetail.getSku() != null){
			           studentJSON.put("sku", orderItemDetail.getSku());
			        }
			        else{
			           studentJSON.put("sku", "");
			        }
			        if(orderItemDetail.getAmount_cents() != null){
			           studentJSON.put("price_total_cents", orderItemDetail.getAmount_cents());
			        }
			        else{
			           studentJSON.put("price_total_cents", 0);
			        }
			        if(orderItemDetail.getQuantity() != null){
				       studentJSON.put("quantity", orderItemDetail.getQuantity());
			        }
			        else{
			           studentJSON.put("quantity", 0);
			        }
			        if(orderItemDetail.getPrice() != null){
				       studentJSON.put("price_ea_cents", orderItemDetail.getPrice());
			        }
			        else{
			           studentJSON.put("price_ea_cents", 0);
			        }
			        if(orderItemDetail.getVendor() != null){
				       studentJSON.put("vendor", orderItemDetail.getVendor());
			        }
			        else{
			            studentJSON.put("vendor", "");
			        }
			        if(orderItemDetail.getVendor() != null){
					       studentJSON.put("variant_info", orderItemDetail.getVariant());
				        }
				        else{
				            studentJSON.put("variant_info", "");
				        }
			        jArray.add(studentJSON);
				}
			}
			System.out.println("jArray size is : "+jArray.size());
			obj.put("invoice_items", jArray);
	      obj.put("issue_on_create", "true");
	     }
		catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public void addItem(OrderItemsDetail itemObject)
	{
		getItems().add(itemObject);
		
	}

	/**
	 * @return Returns the itemAmount.
	 */
	public BigDecimal getItemAmount() {
		return itemAmount;
	}

	/**
	 * @param itemAmount
	 *            The itemAmount to set.
	 */
	public void setItemAmount(BigDecimal itemAmount) {
		this.itemAmount = itemAmount;
	}

	/**
	 * @return Returns the items.
	 */
	public List getItems() {
		if (items == null)
			items = new ArrayList();
		return items;
	}

	/**
	 * @param items
	 *            The items to set.
	 */
	public void setItems(List items) {
		this.items = (ArrayList<OrderItemsDetail>) items;
	}

	/**
	 * @return Returns the shippingCharge.
	 */
	public BigDecimal getShippingCharge() {
		return shippingCharge;
	}

	/**
	 * @param shippingCharge
	 *            The shippingCharge to set.
	 */
	public void setShippingCharge(BigDecimal shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	/**
	 * @return Returns the shippingDiscount.
	 */
	public BigDecimal getShippingDiscount() {
		return shippingDiscount;
	}

	/**
	 * @param shippingDiscount
	 *            The shippingDiscount to set.
	 */
	public void setShippingDiscount(BigDecimal shippingDiscount) {
		this.shippingDiscount = shippingDiscount;
	}

	/**
	 * @return Returns the taxAmount.
	 */
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	/**
	 * @param taxAmount
	 *            The taxAmount to set.
	 */
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	@Override
	public void setItemDiscount(BigDecimal itemDiscount)
	{
		this.itemDiscount = itemDiscount;
		
	}

	@Override
	public void clearItems()
	{
		getItems().clear();
		
	}	
}
