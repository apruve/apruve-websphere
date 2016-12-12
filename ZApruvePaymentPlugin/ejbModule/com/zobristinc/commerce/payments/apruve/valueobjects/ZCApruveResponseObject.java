package com.zobristinc.commerce.payments.apruve.valueobjects;

import org.apache.commons.json.JSONArray;
import org.apache.commons.json.JSONObject;

import com.zobristinc.commerce.payments.apruve.util.ZCApruveConstants;

public class ZCApruveResponseObject implements ZCApruveConstants {
	public static final String TYPE_APRUVE_DIRECT = "Apruve";
	public static final String TYPE_APRUVE_CYBERSOURCE = "Cybersource";
	
	private JSONObject responseParameters = new JSONObject();
	
 	
	public String getValue(String s)
	{
		try{
			//if(responseParameters.get(s) instanceof Integer )
		return responseParameters.get(s).toString();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public Object getObjValue(String s)
	{
		try{
			
		return responseParameters.get(s);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public JSONArray getJSONArrayValue(String s)
	{
		try{
		return  responseParameters.getJSONArray(s);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public ZCApruveResponseObject(JSONObject responseMap)
	{
		this.responseParameters = responseMap;
	}
	

	/**
	 * @param nvpDecoder The nvpDecoder to set.
	 */
	/*public void setNvpDecoder(NVPDecoder nvpDecoder) {
		this.nvpDecoder = nvpDecoder;
	}*/
	/**
	 * @return Returns the nvpDecoder.
	 */
	/*public NVPDecoder getNvpDecoder() {
		return nvpDecoder;
	}*/

	

}
