package com.zobristinc.commerce.payments.apruve.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import com.ibm.commerce.base.objects.ServerJDBCHelperAccessBean;
import com.ibm.commerce.beans.DataBeanManager;
import com.ibm.commerce.common.objects.StoreAccessBean;
import com.ibm.commerce.edp.api.EDPPaymentInstruction;
import com.ibm.commerce.ejb.helpers.nc_crypt;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.fulfillment.objects.ShippingModeAccessBean;
import com.ibm.commerce.order.beans.OrderDataBean;
import com.ibm.commerce.order.objects.OrderAccessBean;
import com.ibm.commerce.order.objects.OrderItemAccessBean;
import com.ibm.commerce.payment.rules.EDPServices;
import com.ibm.commerce.payments.plugin.ExtendedData;
import com.ibm.commerce.payments.plugin.FinancialException;
import com.ibm.commerce.payments.plugin.FinancialTransaction;
import com.ibm.commerce.payments.plugin.PluginException;
import com.ibm.commerce.payments.plugincontroller.beans.MerchantConfAccessBean;
import com.ibm.commerce.payments.plugincontroller.beans.MerchantConfInfoAccessBean;
import com.ibm.commerce.payments.plugincontroller.beans.PluginController;
import com.ibm.commerce.payments.plugincontroller.beans.StoreMerchantAccessBean;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.ras.ECTrace;
import com.ibm.commerce.ras.ECTraceIdentifiers;
import com.ibm.commerce.registry.StoreRegistry;
import com.ibm.commerce.security.keys.WCKeyRegistry;
import com.ibm.commerce.user.objects.AddressAccessBean;
import com.zobristinc.commerce.payments.apruve.processor.helper.ZCApruveProcessorHelper;
import com.zobristinc.commerce.payments.apruve.services.ApruveServiceError;
import com.zobristinc.commerce.payments.apruve.services.ApruveServiceErrorException;

public class ZCApruveUtility {
	final Logger LOGGER = LoggingHelper.getLogger(ZCApruveUtility.class) ;
	private static final String CLASS_NAME = "ZCApruveUtility";
	
	

	public static final String PARAM_PAYMENT_ID = "payment_id";
	public static final String PARAM_STORE_ID = "store_id";
	public static final String PARAM_STOREENT_ID="storeId";
	public static final String PARAM_LANG_ID="langId";
	public static final String PARAM_CATALOG_ID="catalogId";
	
	
	
	
	Map configValueMap = new HashMap();
	protected ZCApruveUtility()
	{
		
	}
	
	public static ZCApruveUtility getSingleton()
	{
		return  new ZCApruveUtility();
	}
	
	public static ZCApruveUtility getInstance(Integer storeId)
	{
		try {
			return ZCApruveProcessorHelper.getInstance(storeId).getApruveUtility( storeId);
		} catch (Exception  e) {
			e.printStackTrace();
			return getSingleton();
		}
	}

		
	public  String getConfProperty(String name, Integer storeId,String defaultValue) throws ECException {
		String configValue = defaultValue;
		try
		{
			configValue = getConfProperty(name,storeId);
		}
		catch(ECException e)
		{
			// return default value
		}
		return configValue; 
	}
	public  String getConfProperty(String name, Integer storeId) throws ECException {
		final String METHOD_NAME = "getConfigProperty";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		String value = null;

		String sourceType = "";
		try
		{
			//ResourceBundle apruveResourceBundle = ZCApruveProcessorHelper.getInstance(storeId).getApruveResourceBundle(storeId);
			//sourceType = apruveResourceBundle.getString("apruveParamsSource");
			sourceType = ZCApruveProcessorHelper.getInstance(storeId).getResourceString("apruveParamsSource", storeId);
			if ( configValueMap.containsKey(storeId + "_" + name))
				{
					value = (String)configValueMap.get(storeId + "_" + name);
					LOGGER.fine( " Return config value from static cache  " + name + " value " + value );
					return value;
				}
				else
				{
					if(sourceType.equals("MERCHCONFINFO"))
					{
						PluginController paymentsFacade = EDPServices.getServices().getPaymentsAccessor().getPayments();
						StoreMerchantAccessBean storeMerchBean = paymentsFacade.getStoreMerchantByStoreId(storeId);
						Collection merchConfs = paymentsFacade.getMerchantConf(storeMerchBean.getMerchantId(), "Apruve", "default");
						Collection merchConfInfos = paymentsFacade.getMerchantConfInfo(((MerchantConfAccessBean)merchConfs.toArray()[0]).getMerchantConfId());
						Iterator iter = merchConfInfos.iterator();
						while (iter.hasNext())
						{
							MerchantConfInfoAccessBean merchInfoBean = (MerchantConfInfoAccessBean)iter.next();
							if (merchInfoBean.getPropertyName().equals(name))
							{
								value = merchInfoBean.getPropertyValue();
								if (merchInfoBean.getEncrypted().shortValue()==1)
								{
									value = nc_crypt.decrypt(merchInfoBean.getPropertyValue(), WCKeyRegistry.getInstance().getKey("MerchantKey").getValueAsString());
								}
								LOGGER.fine( " returning config value for DB " + name + " value " + value );
								configValueMap.put(name,value);
								return value;
		
		
							}
						}
					}
					else
					{
						StoreAccessBean  storeBean = StoreRegistry.singleton().getStore(storeId);
						String directory = storeBean.getDirectory();
						ResourceBundle  storeResourceBundle = null;
						try
						{
							storeResourceBundle = ResourceBundle.getBundle(directory+"."+sourceType);	
						}
						catch(Exception e )
						{
							// fall back with out store directory
						}
						System.out.println("storeResourceBundle is : "+storeResourceBundle+" sourceType is : "+sourceType);
						if(storeResourceBundle == null)
						{
							storeResourceBundle = ResourceBundle.getBundle(sourceType);
							 
						}
						if(storeResourceBundle== null)
							return null;
						// value = storeResourceBundle.getString(name);
						 value = getApruvePropResourceString(name, storeId, storeResourceBundle);
						 if(value != null && !value.trim().equals(""))
							 configValueMap.put(name,value);
						return value; 
					}
				}
			
		} catch (NamingException ex)
		{
			throw new ECSystemException(ECMessage._ERR_ADM_NAMINGEXCEPTION, CLASS_NAME, METHOD_NAME, ex);
	  	}
		catch (CreateException ex)
		{
			throw new ECSystemException(ECMessage._ERR_ADM_CREATEEXCEPTION, CLASS_NAME, METHOD_NAME, ex);
	  	}
		catch (FinderException ex)
		{
			throw new ECSystemException(ECMessage._ERR_FINDER_EXCEPTION, CLASS_NAME, METHOD_NAME, ex);
	  	}
		catch (Exception ex)
		{
			throw new ECSystemException(ECMessage._ERR_GENERIC, CLASS_NAME, METHOD_NAME, ex);
	  	}
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return null;
	}

	public  String getApruvePropResourceString(String key, Integer storeId, ResourceBundle propBundle) {
		String METHOD_NAME = "getApruvePropResourceString";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		LOGGER.fine("key-"+key+", storeId-"+storeId+", propBundle-"+propBundle+"");
		String identifier = null;
		String value = "";
		try
		{
			StoreAccessBean  storeBean = StoreRegistry.singleton().getStore(storeId);
			identifier = storeBean.getIdentifier();
		}
		catch(Exception e){
			// do not break
		}
		
 		String identifierKey = key + "_" + identifier;
		String storeKey = key + "_" + storeId.toString();
			if (identifier != null && propBundle.containsKey(identifierKey)) {
				value = propBundle.getString(identifierKey);
			}	
			else if ( propBundle.containsKey(storeKey)) {
				value = propBundle.getString(storeKey);
			}
			else if (propBundle.containsKey(key)) {
				value = propBundle.getString(key);
			}
			LOGGER.fine("value-"+value+"");
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return value;
	}
	
	
	public  BigDecimal getTotalAmount(OrderAccessBean abOrder) throws ECException {
		final String METHOD_NAME = "getTotalAmount";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);

        try {
        	BigDecimal dOrderTotal = abOrder.getTotalProductPriceInEJBType();
            dOrderTotal = dOrderTotal.add(abOrder.getTotalTaxInEJBType());
            dOrderTotal = dOrderTotal.add(abOrder.getTotalShippingChargeInEJBType());
            dOrderTotal = dOrderTotal.add(abOrder.getTotalShippingTaxInEJBType());
            dOrderTotal = dOrderTotal.add(abOrder.getTotalAdjustmentInEJBType());
            LOGGER.exiting(CLASS_NAME, METHOD_NAME);
         	return dOrderTotal;
	    } catch (javax.ejb.CreateException ex) {
	            throw new ECSystemException(ECMessage._ERR_CREATE_EXCEPTION, CLASS_NAME, METHOD_NAME, ex);
	    } catch (javax.ejb.FinderException ex) {
	            throw new ECSystemException(ECMessage._ERR_FINDER_EXCEPTION, CLASS_NAME, METHOD_NAME, ex);
	    } catch (javax.naming.NamingException ex) {
	            throw new ECSystemException(ECMessage._ERR_NAMING_EXCEPTION, CLASS_NAME, METHOD_NAME, ex);
	    } catch (java.rmi.RemoteException ex) {
	            throw new ECSystemException(ECMessage._ERR_REMOTE_EXCEPTION, CLASS_NAME, METHOD_NAME, ex);
	    } finally {
	    	ECTrace.exit(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, METHOD_NAME);
	    }
    }
	
	public  void markPaymentSuccessful(FinancialTransaction financialTransaction) throws FinancialException, PluginException
	{
		final String METHOD_NAME = "markPaymentSuccessful";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);

		
		
		financialTransaction.setState(FinancialTransaction.STATE_SUCCESS);
		financialTransaction.setResponseCode("0");
		financialTransaction.setReasonCode("0");
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	}
	
 
	public  BigDecimal getTotalAmount(String orderId) throws ECException {
		final String METHOD_NAME = "getTotalAmount";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);

        try {
			OrderAccessBean abOrder = new OrderAccessBean();
			abOrder.setInitKey_orderId(orderId);
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			return getTotalAmount(abOrder);
	    } finally {
	    	ECTrace.exit(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, METHOD_NAME);
	    }
    }
		
	public String getGiftCardPaymethod(Integer storeId) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ECException
	{
		return getConfProperty("GiftCardPayMethod",storeId);
	}
	
	public String getVariant(String catentryId,Integer langId){
		String veriant = "";
		try{
			ServerJDBCHelperAccessBean jdbcHelper = new ServerJDBCHelperAccessBean();
			Vector attributeResults=null;
			String attributeSQL = " select  a.catentry_id, f.name, d.value  from " +
			"    catentryattr a, attr b, attrval c, attrvaldesc d,  attrdesc f " + 
			" where a.catentry_id = "+catentryId+" and a.attr_id in b.attr_id and a.attr_id = f.attr_id " +
			" and f.language_id = ? and a.attrval_id = c.attrval_id " + 
			" and a.attrval_id = d.attrval_id " + 
			" and d.language_id= ? and a.usage=1";
			
			Object[] attributeParameters = null;
			attributeParameters = new Object[]{  langId ,langId };
			   attributeResults = jdbcHelper.executeParameterizedQuery(attributeSQL,attributeParameters);
			  
			Iterator attrIt=attributeResults.iterator();
			while (attrIt.hasNext()){  
			 Vector row = (Vector) attrIt.next();
			catentryId=row.get(0).toString();
			String attributeName = row.get(1).toString();
			String attributeValue = row.get(2).toString();
			veriant += attributeName;
			veriant += " : ";
			veriant += attributeValue;
			if(attrIt.hasNext())
				veriant += ",";
			}
			System.out.println("veriant is : "+veriant);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		

		return veriant;
	}
	
	public BigDecimal getGiftCardAmount(OrderAccessBean abOrder,Integer storeId){
		final String METHOD_NAME = "getGiftCardAmount";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		BigDecimal giftCardAmt = BigDecimal.ZERO;
		try
		{
			OrderDataBean orderBean = new OrderDataBean(abOrder);
			orderBean.setStoreEntityId(storeId);
			DataBeanManager.activate(orderBean);
			OrderDataBean.PaymentInstruction[] paymentInstructions = orderBean.getPaymentInstructions();
			for (int i = 0; i < paymentInstructions.length; i++) {
	   			EDPPaymentInstruction instruction = paymentInstructions[i].getPaymentInstruction();
	   			// get the gift card payment
	   			if (instruction.getPaymentMethod().equals(getGiftCardPaymethod(storeId)) && instruction.getAmount() != null) {
	   				giftCardAmt = giftCardAmt.add(instruction.getAmount());
	   			}
			}
			return giftCardAmt;
		}
		catch(Exception e)
		{
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception while fetching Gift Card amount ["+e.toString()+"]");
		}
		
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return BigDecimal.ZERO;
	}
	
	public static final String DEFAULT_SHIP_NAME = "Recipient";
}
