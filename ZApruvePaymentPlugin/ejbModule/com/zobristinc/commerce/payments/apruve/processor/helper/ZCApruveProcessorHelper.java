package com.zobristinc.commerce.payments.apruve.processor.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.ibm.commerce.common.objects.LanguageAccessBean;
import com.ibm.commerce.common.objects.StoreAccessBean;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.registry.StoreRegistry;
import com.zobristinc.commerce.payments.apruve.builder.ZCApruveRequestBuilder;
import com.zobristinc.commerce.payments.apruve.processor.ZCApruveProcessor;
import com.zobristinc.commerce.payments.apruve.responseProcessor.ZCApruveResponseHandler;
import com.zobristinc.commerce.payments.apruve.services.ApruveServiceException;
import com.zobristinc.commerce.payments.apruve.util.ZCApruveUtility;

public class ZCApruveProcessorHelper {
	public static String CLASS_NAME = "ZCApruveProcessorHelper";
	public static ResourceBundle  defaultApruveResourceBundle = null;
	final static Logger LOGGER =  LoggingHelper.getLogger(ZCApruveProcessorHelper.class);
	
	
	static
	{
		try
		{
			defaultApruveResourceBundle = ResourceBundle.getBundle("apruveConfig");	
		}
		catch(Exception e )
		{
			
		}
		
	}
	public static Map apruveObjects = new HashMap();
	
	public ZCApruveProcessorHelper()
	{
		
	}
	
	public static ZCApruveProcessorHelper getInstance(Integer storeId) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException
	{

		String METHOD_NAME = "getInstance";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		String directory = null;
		ResourceBundle resourceBundle = defaultApruveResourceBundle;
		try
		{
			if ( apruveObjects.containsKey("ResourceBundle_InternalUse_" + storeId))
			{
				if ( apruveObjects.get("ResourceBundle_InternalUse_" + storeId) != null )
					resourceBundle = (ResourceBundle)apruveObjects.get("ResourceBundle_InternalUse_" + storeId);
			}
			else
			{
				StoreAccessBean  storeBean = StoreRegistry.singleton().getStore(storeId);
				directory = storeBean.getDirectory();
				
				resourceBundle = ResourceBundle.getBundle(directory + ".apruveConfig");
				apruveObjects.put("ResourceBundle_InternalUse_" + storeId, resourceBundle);
				
			}
			 
			
		}
		catch(Exception e )
		{
			LOGGER.fine(" apruve.config file doesn't exist in store identifier folder: " + e.getMessage() );
			// do not break
		}
		String apruveHelperName = null;
		if ( resourceBundle.containsKey("ApruveProcessorHelper" + "_" + storeId ))
			apruveHelperName = resourceBundle.getString("ApruveProcessorHelper" + "_" + storeId);
		else if ( resourceBundle.containsKey("ApruveProcessorHelper"  ))
			apruveHelperName = resourceBundle.getString("ApruveProcessorHelper");
		else
			apruveHelperName = ZCApruveProcessorHelper.class.getName();
		
		 if ( apruveObjects.containsKey(apruveHelperName + "_" + storeId))
		{
			LOGGER.finest(" Retrning class for " + apruveHelperName );
			return (ZCApruveProcessorHelper) apruveObjects.get(apruveHelperName + "_" + storeId);
		}
		LOGGER.fine(" Creating class for " + apruveHelperName );
		Class apruveHelperClass =  Class.forName(apruveHelperName);
		Constructor apruveHelperConstructor = apruveHelperClass.getConstructor();
		ZCApruveProcessorHelper helper =  (ZCApruveProcessorHelper)apruveHelperConstructor.newInstance();
	
		apruveObjects.put(apruveHelperName + "_" + storeId,helper);
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return helper;

	}
	
 

	
	public ResourceBundle getApruveResourceBundle(Integer storeId)
	{
		String METHOD_NAME = "getApruveResourceBundle";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		if ( apruveObjects.containsKey("ResourceBundle_InternalUse_" + storeId))
		{
			if ( apruveObjects.get("ResourceBundle_InternalUse_" + storeId) != null )
				return (ResourceBundle)apruveObjects.get("ResourceBundle_InternalUse_" + storeId);
		}
		String directory = null;
		try{
			StoreAccessBean  storeBean = StoreRegistry.singleton().getStore(storeId);
			directory = storeBean.getDirectory();
			
			ResourceBundle  storeResourceBundle = ResourceBundle.getBundle(directory + ".apruveConfig");
			apruveObjects.put("ResourceBundle_InternalUse_" + storeId, storeResourceBundle);
			if ( storeResourceBundle == null )
				return defaultApruveResourceBundle;
			else
				return storeResourceBundle;
			
			}
			catch(Exception e){
				apruveObjects.put("ResourceBundle_InternalUse_" + storeId,null);
			}
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);		
		return defaultApruveResourceBundle;
	}
	
	public ResourceBundle getApruveDetailsBundle(Integer storeId, Integer langId)
	{
		String METHOD_NAME = "getApruveDetailsBundle";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		if ( apruveObjects.containsKey("ResourceBundle_InternalUse_detail_" + storeId))
		{
			if ( apruveObjects.get("ResourceBundle_InternalUse_detail_" + storeId) != null )
				return (ResourceBundle)apruveObjects.get("ResourceBundle_InternalUse_detail_" + storeId);
		}
		String directory = null;
		LanguageAccessBean langBean = null;
		Locale lcl = null;
		try
		{
			String sourceType = ZCApruveProcessorHelper.getInstance(storeId).getResourceString("apruveConstantTextSource", storeId);
			System.out.println("sourceType is : "+sourceType);
			langBean = new LanguageAccessBean();
			langBean.setInitKey_languageId(langId.toString());
			langBean.refreshCopyHelper();
			StoreAccessBean  storeBean = StoreRegistry.singleton().getStore(storeId);
			directory = storeBean.getDirectory();
			lcl = new Locale(langBean.getLanguage());
			//ResourceBundle  storeResourceBundle = ResourceBundle.getBundle(directory + ".ApruveLineDetails"+"_"+langBean.getLocaleName());
			ResourceBundle  storeResourceBundle = ResourceBundle.getBundle(directory + "."+sourceType, lcl);
			apruveObjects.put("ResourceBundle_InternalUse_detail_" + storeId, storeResourceBundle);
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			return storeResourceBundle;
		}
		catch(MissingResourceException me)
		{
			try 
			{
				langBean = new LanguageAccessBean();
				langBean.setInitKey_languageId(langId.toString());
				langBean.refreshCopyHelper();
				lcl = new Locale(langBean.getLanguage());
				ResourceBundle  storeResourceBundle = ResourceBundle.getBundle("ApruveLineDetails", lcl);
				apruveObjects.put("ResourceBundle_InternalUse_detail_" + storeId, storeResourceBundle);
				LOGGER.exiting(CLASS_NAME, METHOD_NAME);
				return storeResourceBundle;
			} 
			catch (Exception e) 
			{
				
			} 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return ResourceBundle.getBundle("ApruveLineDetails");
	}
	
	public String getApruveDetailValue(String key, Integer storeId, Integer langId)
	{
		String METHOD_NAME = "getApruveDetailValue";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		String value = null;
		try
		{
			ResourceBundle bundle = getApruveDetailsBundle(storeId, langId);
			if(bundle.containsKey(key))
				value = bundle.getString(key);
		}
		catch (Exception e) 
		{
		} 
		if(value == null)
			value = key;

		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return value;
	}
	
	public  String getResourceString(String key, Integer storeId) {
		String METHOD_NAME = "getResourceString";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		String identifier = null;
	try{
		StoreAccessBean  storeBean = StoreRegistry.singleton().getStore(storeId);
		identifier = storeBean.getIdentifier();
		}
		catch(Exception e){
			// do not break
		}
		
 		String identifierKey = key + "_" + identifier;
		String storeKey = key + "_" + storeId.toString();
			if (identifier != null && getApruveResourceBundle(storeId).containsKey(identifierKey)) {
				return getApruveResourceBundle(storeId).getString(identifierKey);
			}	
			else if ( getApruveResourceBundle(storeId).containsKey(storeKey)) {
			return getApruveResourceBundle(storeId).getString(storeKey);
			}
			else if (getApruveResourceBundle(storeId).containsKey(key)) {
				return  getApruveResourceBundle(storeId).getString(key);
			}

		return "";
	}
	
	public  ZCApruveProcessor getApruveProcessor(Integer storeId) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, ApruveServiceException
	{
		String METHOD_NAME = "getApruveProcessor";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		String apruveProcessorName = getResourceString("ApruveProcessor", storeId);
		
		 if ( apruveObjects.containsKey(apruveProcessorName + "_" + storeId))
		{
			LOGGER.finest(" Retrning class for " + apruveProcessorName );
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			return (ZCApruveProcessor) apruveObjects.get(apruveProcessorName + "_" + storeId);
		}
		LOGGER.fine(" Creating class for " + apruveProcessorName );
		Class apruveProcessorClass =  Class.forName(apruveProcessorName);
		Method apruveMethod = apruveProcessorClass.getMethod("getInstance", Integer.class);
		ZCApruveProcessor processor = (ZCApruveProcessor) apruveMethod.invoke(null, storeId);//ZCPaypalDirectProcessor.getInstance(storeId);//
		apruveObjects.put(apruveProcessorName + "_" + storeId,processor);

		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return processor;
	}
	
	public  ZCApruveUtility getApruveUtility(Integer storeId) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		String METHOD_NAME = "getApruveUtility";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		String apruveUtility = getResourceString("ApruveUtility", storeId);
		if ( apruveObjects.containsKey(apruveUtility + "_" + storeId ))
		{
			LOGGER.finest(" Retrning class for " + apruveUtility );
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			return (ZCApruveUtility) apruveObjects.get(apruveUtility + "_" + storeId);
		}
		LOGGER.fine(" Creating class for " + apruveUtility );
		Class apruveProcessorClass =  Class.forName(apruveUtility);
		Method apruveMethod = apruveProcessorClass.getMethod("getSingleton");
		ZCApruveUtility utility = (ZCApruveUtility) apruveMethod.invoke(null);
		apruveObjects.put(apruveUtility + "_" + storeId,utility);
		
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return utility;
	}
	
	public  ZCApruveRequestBuilder getApruveRequestBuilder(Integer storeId) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		String METHOD_NAME = "getApruveRequestBuilder";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		String apruveBuilderName = getResourceString("ApruveRequestBuilder", storeId);
		if ( apruveObjects.containsKey(apruveBuilderName+ "_" + storeId))
		{
			LOGGER.finest(" Retrning class for " + apruveBuilderName );
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			return (ZCApruveRequestBuilder) apruveObjects.get(apruveBuilderName+ "_" + storeId);
		}
		
		//String apruveBuilderType = getResourceString("ApruveRequestType", storeId);
		//LOGGER.fine(" Creating class for " + apruveBuilderName + " with type " + apruveBuilderType );
		LOGGER.fine(" Creating class for " + apruveBuilderName );
		Class apruveBuilderClass =  Class.forName(apruveBuilderName);
		Constructor apruveConstructor = apruveBuilderClass.getConstructor();
		
		//ZCApruveRequestBuilder builder =  (ZCApruveRequestBuilder)apruveConstructor.newInstance(apruveBuilderType);
		ZCApruveRequestBuilder builder =  (ZCApruveRequestBuilder)apruveConstructor.newInstance();
		apruveObjects.put(apruveBuilderName+ "_" + storeId,builder);
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return builder;
	}
	
	public  ZCApruveResponseHandler getApruveResponseProcessor(Integer storeId) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		String METHOD_NAME = "getApruveResponseProcessor";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		String apruveResponseHandlerName = getResourceString("ApruveResponseProcessor", storeId);
		if ( apruveObjects.containsKey(apruveResponseHandlerName + "_" + storeId))
		{
			LOGGER.finest(" Retrning class for " + apruveResponseHandlerName );
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			return (ZCApruveResponseHandler) apruveObjects.get(apruveResponseHandlerName+ "_" + storeId);
		}
		
		//String apruveBuilderType = getResourceString("ApruveRequestType", storeId);
		//LOGGER.fine(" Creating class for " + apruveResponseHandlerName + " with type " + apruveBuilderType );
		LOGGER.fine(" Creating class for " + apruveResponseHandlerName);
		Class apruveBuilderClass =  Class.forName(apruveResponseHandlerName);
		Constructor apruveConstructor = apruveBuilderClass.getConstructor();
		
		//ZCApruveResponseHandler responseProcessor =  (ZCApruveResponseHandler)apruveConstructor.newInstance(apruveBuilderType);
		ZCApruveResponseHandler responseProcessor =  (ZCApruveResponseHandler)apruveConstructor.newInstance();
		apruveObjects.put(apruveResponseHandlerName+ "_" + storeId,responseProcessor);
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return responseProcessor;
	}	
	
	public  ResourceBundle getCybersourceApruveResourceBundle(Integer storeId) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		String METHOD_NAME = "getCybersourceApruveResourceBundle";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		String cybersourceApruveProperties = getResourceString("CybersourceApruveResourceBundle", storeId);
		
		if ( apruveObjects.containsKey(cybersourceApruveProperties+ "_" + storeId))
		{
			LOGGER.finest(" Retrning class for " + cybersourceApruveProperties );
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			return (ResourceBundle) apruveObjects.get(cybersourceApruveProperties+ "_" + storeId);
		}		
		LOGGER.fine(" Creating class for " + cybersourceApruveProperties );
		ResourceBundle cybersourceApruveResourceBundle = ResourceBundle.getBundle(cybersourceApruveProperties);
		
		apruveObjects.put(cybersourceApruveProperties+ "_" + storeId,cybersourceApruveResourceBundle);
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return cybersourceApruveResourceBundle;
	}	
	
   
    public static Properties convertResourceBundleToProperties(ResourceBundle resource) {
    	
    	Properties  props = new Properties();
        Enumeration keys = resource.getKeys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            props.put(key, resource.getString(key));
        }
        return props;
    }	
	
	public String getErrorResourceBundleName(Integer storeId)
	{
		return getResourceString("apruveErrorMessage",storeId);
	}
	
	
	

}
