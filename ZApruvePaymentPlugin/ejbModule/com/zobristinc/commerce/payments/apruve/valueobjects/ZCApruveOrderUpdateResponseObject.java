package com.zobristinc.commerce.payments.apruve.valueobjects;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.json.JSONArray;
import org.apache.commons.json.JSONObject;

public class ZCApruveOrderUpdateResponseObject extends ZCApruveResponseObject {
	public ZCApruveOrderUpdateResponseObject(JSONObject responseJson) {
		super(responseJson);
		// TODO Auto-generated constructor stub
	}
	
	public Map getApruveOrderItemsMap(){
		
		HashMap orderItemsMap = new HashMap();
		try{
			System.out.println("Order items array list in update order response is : "+getJSONArrayValue("order_items"));
			JSONArray jsonarrayObj = getJSONArrayValue("order_items");
			for(int i=0 ;i<jsonarrayObj.length() ;i++){
				JSONObject jsonObj = new JSONObject(jsonarrayObj.get(i));
				ZCApruveOrderItemsObject apruveOrderItems = new ZCApruveOrderItemsObject();
				if(jsonObj.get("id") != null)
					apruveOrderItems.setOrderItemId(jsonObj.getString("id"));
				if(jsonObj.get("order_id") != null)
					apruveOrderItems.setOrder_id(jsonObj.getString("order_id"));
				if(jsonObj.get("sku") != null)
					apruveOrderItems.setSku(jsonObj.getString("sku"));
				if(jsonObj.get("title") != null)
					apruveOrderItems.setTitle(jsonObj.getString("title"));
				if(jsonObj.get("description") != null)
					apruveOrderItems.setDescription(jsonObj.getString("description"));
				if(jsonObj.get("price_ea_cents") != null)
					apruveOrderItems.setPrice(Integer.valueOf(jsonObj.getString("price_ea_cents")));
				if(jsonObj.get("price_total_cents") != null)
					apruveOrderItems.setTotalAmount_cents(Integer.valueOf(jsonObj.getString("price_total_cents")));
				if(jsonObj.get("currency") != null)
					apruveOrderItems.setCurrency(jsonObj.getString("currency"));
				if(jsonObj.get("quantity") != null)
					apruveOrderItems.setQuantity(Integer.valueOf(jsonObj.getString("quantity")));
				if(jsonObj.get("vendor") != null)
					apruveOrderItems.setVendor(jsonObj.getString("vendor"));
				if(jsonObj.get("variant_info") != null)
					apruveOrderItems.setVariant(jsonObj.getString("variant_info"));
				if(jsonObj.get("merchant_notes") != null)
					apruveOrderItems.setMerchantNotes(jsonObj.getString("merchant_notes"));
				if(jsonObj.get("plan_code") != null)
					apruveOrderItems.setPlanCode(jsonObj.getString("plan_code"));
				if(jsonObj.get("product_url") != null)
					apruveOrderItems.setProductURL(jsonObj.getString("product_url"));
				if(jsonObj.get("product_image_url") != null)
					apruveOrderItems.setProductImageURL(jsonObj.getString("product_image_url"));
				if(jsonObj.get("sku") != null)
					orderItemsMap.put(jsonObj.getString("sku"), apruveOrderItems);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return orderItemsMap;
	}
}
