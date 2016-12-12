package com.zobristinc.commerce.payments.apruve.valueobjects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.commerce.exception.ECException;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.zobristinc.commerce.integration.apruve.commands.OrderItemsDetail;

public class ZCApruveCreateOrderRequestObject extends ZCApruveRequestObject implements ZCApruveAddItemInterface
{
	private final static String CLASSNAME = ZCApruveCreateOrderRequestObject.class.getName();
	private final static Logger LOGGER = Logger.getLogger(CLASSNAME);
	
	String API_Key = "";
	String merchant_id = "";
	String merchant_order_id = "";
	Integer amount_cents;
	String currency = "";
	Integer tax_cents;
	Integer shipping_cents;
	String expire_at = "";
	ArrayList<OrderItemsDetail> items ;
	String hashinfo = "";
	int storeId;
	String finalize_on_create;
	String invoice_on_create;
	private BigDecimal itemAmount;
	private BigDecimal taxAmount;
	private BigDecimal shippingCharge;
	private BigDecimal shippingDiscount;
	private BigDecimal itemDiscount = BigDecimal.ZERO;

	public String getAPI_Key()
	{
		return API_Key;
	}

	public void setAPI_Key(String aPI_Key)
	{
		API_Key = aPI_Key;
	}

	public String getMerchant_id()
	{
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id)
	{
		this.merchant_id = merchant_id;
	}

	public String getMerchant_order_id()
	{
		return merchant_order_id;
	}

	public void setMerchant_order_id(String merchant_order_id)
	{
		this.merchant_order_id = merchant_order_id;
	}

	public Integer getAmount_cents()
	{
		return amount_cents;
	}

	public void setAmount_cents(Integer amount_cents)
	{
		this.amount_cents = amount_cents;
	}

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
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

	public String getExpire_at()
	{
		return expire_at;
	}

	public void setExpire_at(String expire_at)
	{
		this.expire_at = expire_at;
	}
	
	public String getHashinfo()
	{
		return hashinfo;
	}

	public void setHashinfo(String hashinfo)
	{
		this.hashinfo = hashinfo;
	}

	public int getStoreId()
	{
		return storeId;
	}

	public void setStoreId(int storeId)
	{
		this.storeId = storeId;
	}

	public String getFinalize_on_create()
	{
		return finalize_on_create;
	}

	public void setFinalize_on_create(String finalize_on_create)
	{
		this.finalize_on_create = finalize_on_create;
	}

	public String getInvoice_on_create()
	{
		return invoice_on_create;
	}

	public void setInvoice_on_create(String invoice_on_create)
	{
		this.invoice_on_create = invoice_on_create;
	}

	@Override
	public JSONObject getJSONRequestObj() throws ECException
	{
		final String  METHODNAME = "getJSONRequestObj"; 
		LOGGER.entering(CLASSNAME, METHODNAME);
		JSONObject obj = new JSONObject();
		try 
		{
		
			obj.put("merchant_id", getMerchant_id());
			obj.put("merchant_order_id", getMerchant_order_id());
			obj.put("amount_cents", getAmount_cents());
			obj.put("currency", getCurrency());
			obj.put("tax_cents", getTax_cents());
			obj.put("shipping_cents", getShipping_cents());
			    
			if(getExpire_at() != null)
			{
			  obj.put("expire_at", getExpire_at());
			}
			else
			{
			   obj.put("expire_at", "");
			}
			    
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
			           studentJSON.put("amount_cents", orderItemDetail.getAmount_cents());
			        }
			        else{
			           studentJSON.put("amount_cents", 0);
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
			obj.put("order_items", jArray);
			obj.put("finalize_on_create", finalize_on_create);
			obj.put("invoice_on_create", invoice_on_create);
			
	     }
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASSNAME, METHODNAME, "Encountered exception ["+e.toString()+"]", e);
			e.printStackTrace();
		}
		LOGGER.info("create order json object : "+obj);
		LOGGER.exiting(CLASSNAME, METHODNAME);
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
