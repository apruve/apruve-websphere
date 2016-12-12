package com.zobristinc.commerce.integration.apruve.commands;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.foundation.logging.LoggingHelper;
import com.ibm.commerce.server.ECConstants;
import com.ibm.json.java.JSONObject;
import com.zobristinc.commerce.payments.apruve.builder.ZCApruveRequestBuilder;
import com.zobristinc.commerce.payments.apruve.processor.helper.ZCApruveProcessorHelper;
import com.zobristinc.commerce.payments.apruve.util.ZCApruveUtility;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateOrderRequestObject;

public class ZCOrderInformationCmdImpl extends ControllerCommandImpl implements ZCOrderInformationCmd 
{
	private static final String CLASS_NAME = "ZCOrderInformationCmdImpl";
	final Logger LOGGER = LoggingHelper.getLogger(ZCOrderInformationCmdImpl.class);
	
	String API_Key = "";
	String merchant_order_id = "";
	String hashinfo = "";
	int storeId;
	int langId;
	TypedProperty reqProp = getRequestProperties();
	
	ZCApruveCreateOrderRequestObject createOrderRequestObj = new ZCApruveCreateOrderRequestObject(); 
	
	public ZCOrderInformationCmdImpl() {
		super();
	}
	public void performExecute() throws ECException {
		final String  METHODNAME = "performExecute"; 
		LOGGER.entering(CLASS_NAME, METHODNAME);
		try{
			super.performExecute();
			storeId = new Integer(requestProperties.getString("storeId"));
			langId = new Integer(requestProperties.getString("langId"));
			merchant_order_id = requestProperties.getString("merchant_order_id", null);
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(getStoreId());
			API_Key = apruveUtility.getConfProperty("Merchant_API_Key", getStoreId());
			ZCApruveRequestBuilder builder = ZCApruveProcessorHelper.getInstance(storeId).getApruveRequestBuilder(storeId);
			createOrderRequestObj = builder.getApruveCreateOrderTransactionObject(merchant_order_id,storeId,langId);
			TypedProperty rspProp = new TypedProperty();
			 rspProp.put("secureHashMap",getHashMap());
			 rspProp.put("jsonObj",     getJsonObject());
			 rspProp.put(ECConstants.EC_VIEWTASKNAME,"ZCOrderInformationResponseView");// MSVertexValidateResponseView
		  	 setResponseProperties(rspProp);
		}catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHODNAME, "Encountered exception ["+e.toString()+"]", e);
			e.printStackTrace();
		}
		LOGGER.exiting(CLASS_NAME, METHODNAME);
			
		}
	
	public String getHashMap(){
		final String  METHODNAME = "getHashMap";
		LOGGER.entering(CLASS_NAME, METHODNAME);
		MessageDigest digest;
		String hash = "";
		try {
			
			digest = MessageDigest.getInstance("SHA-256");
			//hash = digest.digest(getHashInfo().getBytes());
			hash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(getHashInfo()); 
			
		} 
		catch (NoSuchAlgorithmException e) {
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHODNAME, "Encountered exception ["+e.toString()+"]", e);
			e.printStackTrace();
		}
		LOGGER.info( " sha256 hash string is : " + hash.toString() );
		LOGGER.exiting(CLASS_NAME, METHODNAME);
		return hash.toString();
		
	}
		
	public String getHashInfo(){
		final String  METHODNAME = "getHashInfo";
		LOGGER.entering(CLASS_NAME, METHODNAME);
		hashinfo = createOrderRequestObj.getAPI_Key();
		hashinfo += createOrderRequestObj.getMerchant_id();
		hashinfo += createOrderRequestObj.getMerchant_order_id();
		hashinfo += createOrderRequestObj.getAmount_cents();
		hashinfo += createOrderRequestObj.getCurrency();
		hashinfo += createOrderRequestObj.getTax_cents();
		hashinfo += createOrderRequestObj.getShipping_cents();
		hashinfo += createOrderRequestObj.getExpire_at();
		hashinfo += createOrderRequestObj.getFinalize_on_create();
		hashinfo += createOrderRequestObj.getInvoice_on_create();
		//hashinfo += getJsonObject().getString("order_items");
		
		ArrayList<OrderItemsDetail> oidList = (ArrayList<OrderItemsDetail>) createOrderRequestObj.getItems();
		for(int i = 0;i < oidList.size();i++){
			OrderItemsDetail oid = oidList.get(i);
			if(oid.getTitle() != null){
				hashinfo += oid.getTitle();
			}else{
				hashinfo += "";
			}
			if(oid.getAmount_cents() != null)
				hashinfo += oid.getAmount_cents();
			if(oid.getPrice() != null)
				hashinfo += oid.getPrice()	;
			if(oid.getQuantity() != null)
				hashinfo += oid.getQuantity();
			if(oid.getDescription() != null)
				hashinfo += oid.getDescription();
			if(oid.getVariant() != null)
				hashinfo += oid.getVariant();
			if(oid.getSku() != null)
				hashinfo += oid.getSku();
			if(oid.getVendor() != null)
				hashinfo += oid.getVendor();
		}
		LOGGER.info( " String used for creating secure hash is : " + hashinfo );
		LOGGER.exiting(CLASS_NAME, METHODNAME);
		return hashinfo;
	}
	
	public JSONObject getJsonObject(){
		
		final String  METHODNAME = "getJsonObject";
		LOGGER.entering(CLASS_NAME, METHODNAME);
		
		JSONObject obj = new JSONObject();
		try 
		{
			obj = createOrderRequestObj.getJSONRequestObj();
	    } catch (Exception e) {
	    	LOGGER.logp(Level.SEVERE, CLASS_NAME, METHODNAME, "Encountered exception ["+e.toString()+"]", e);
			e.printStackTrace();
		}
		LOGGER.info( " JSON object for creating apruve order is : " + obj.toString() );
		LOGGER.exiting(CLASS_NAME, METHODNAME);
		return obj;
	   }
	
	public String getMerchant_order_id() {
		return merchant_order_id;
	}
	public void setMerchant_order_id(String merchant_order_id) {
		this.merchant_order_id = merchant_order_id;
	}
	public Integer getStoreId()
	{
		return storeId;
	}
	public void setStoreId(int storeId)
	{
		this.storeId = storeId;
	}
	public int getLangId()
	{
		return langId;
	}
	public void setLangId(int langId)
	{
		this.langId = langId;
	}
	
}
