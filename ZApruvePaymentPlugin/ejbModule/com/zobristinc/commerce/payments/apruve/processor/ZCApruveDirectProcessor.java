package com.zobristinc.commerce.payments.apruve.processor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.payments.plugin.FinancialTransaction;
import com.ibm.commerce.payments.plugin.PluginContext;
import com.zobristinc.commerce.payments.apruve.processor.helper.ZCApruveProcessorHelper;
import com.zobristinc.commerce.payments.apruve.services.ApruveServiceError;
import com.zobristinc.commerce.payments.apruve.services.ApruveServiceErrorException;
import com.zobristinc.commerce.payments.apruve.services.ApruveServiceException;
import com.zobristinc.commerce.payments.apruve.util.ZCApruveUtility;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateInvoiceResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateOrderItemRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateOrderItemResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderCreateInvoiceRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderFinalizeRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderFinalizeResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemDeleteRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemDeleteResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemUpdateRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemUpdateResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderUpdateRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderUpdateResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveRetrieveOrderRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveRetrieveOrderResponseObject;

public class ZCApruveDirectProcessor implements ZCApruveProcessor
{
	private final static String CLASSNAME = ZCApruveDirectProcessor.class.getName();
	private final static Logger LOGGER = Logger.getLogger(CLASSNAME);
	private static Map<Integer, ZCApruveProcessor> mySelfs = new HashMap();

	Integer storeId;
	String operationName = "";
	Map<String, String> credentialMap = new HashMap<String, String>();
	
	/**
	 * Default constructor. Initializes this service.
	 * 
	 * @throws Exception
	 *             if an exception occurrs while initializing the service
	 */
	private ZCApruveDirectProcessor(Integer storeId) throws ApruveServiceException
	{
		this.storeId = storeId;
		init(storeId);
	}

	/**
	 * Initializes the service
	 * 
	 * @throws Exception
	 *             if an exception occurrs while trying to initialize the
	 *             service
	 */
	private void init(Integer storeId) throws ApruveServiceException
	{
		try
		{
			credentialMap.put("Merchant_API_Key",
					ZCApruveUtility.getInstance(storeId).getConfProperty("Merchant_API_Key", storeId));

		}
		catch (ECException ece)
		{
			throw new ApruveServiceException("Service init failed", ece, ApruveServiceException.FATAL);
		}
	}

	/**
	 * Returns an instance of this service suitable for issuing service calls.
	 * If the service is not initialized, an attempt will be made to initialize
	 * it.
	 * 
	 * @return service instance
	 * @throws Exception
	 *             if an exception occurrs while initializing the service, if
	 *             needed
	 */
	public static ZCApruveProcessor getInstance(Integer storeId) throws ApruveServiceException
	{
		if (!isInitialized(storeId))
		{
			mySelfs.put(storeId, new ZCApruveDirectProcessor(storeId));
		}
		return mySelfs.get(storeId);
	}

	/**
	 * Calls the <code>doFinalizingOrder</code> service call and returns
	 * a <code>JSON</code> of service response key-value pairs.
	 * 
	 * @param JSON
	 *            request key-value pairs
	 * @return response name-value pairs
	 * @throws ApruveException
	 * @throws ApruveServiceException
	 * @throws ApruveServiceErrorException
	 * @throws ECException
	 */
	public ZCApruveOrderFinalizeResponseObject doFinalizingOrder(ZCApruveOrderFinalizeRequestObject requestObject,
			PluginContext con, FinancialTransaction tran, boolean bool) throws ECException, ApruveServiceException,
			ApruveServiceErrorException
	{
		final String methodName = "doFinalizingOrder";
		LOGGER.entering(CLASSNAME, methodName);
		operationName = "finalizeOrder";
		JSONObject responseObject = call(requestObject, operationName);
		LOGGER.exiting(CLASSNAME, methodName);
		return new ZCApruveOrderFinalizeResponseObject(responseObject);

	}
	
	public ZCApruveRetrieveOrderResponseObject doRetrieveOrder(ZCApruveRetrieveOrderRequestObject requestObject) throws ECException, ApruveServiceException,
			ApruveServiceErrorException
	{
		final String methodName = "doFinalizingOrder";
		LOGGER.entering(CLASSNAME, methodName);
		operationName = "retriveOrder";
		JSONObject responseObject = call(requestObject, operationName);
		LOGGER.exiting(CLASSNAME, methodName);
		return new ZCApruveRetrieveOrderResponseObject(responseObject);

	}
	
	public ZCApruveOrderUpdateResponseObject doUpdateOrder(ZCApruveOrderUpdateRequestObject requestObject,
			PluginContext con, FinancialTransaction tran, boolean bool) throws ECException, ApruveServiceException,
			ApruveServiceErrorException
	{
		final String methodName = "doUpdateOrder";
		LOGGER.entering(CLASSNAME, methodName);
		operationName = "updateOrder";
		JSONObject responseObject = call(requestObject, operationName);
		LOGGER.exiting(CLASSNAME, methodName);
		return new ZCApruveOrderUpdateResponseObject(responseObject);

	}
	
	public ZCApruveOrderItemUpdateResponseObject doUpdateOrderItem(ZCApruveOrderItemUpdateRequestObject requestObject,
			PluginContext con, FinancialTransaction tran, boolean bool) throws ECException, ApruveServiceException,
			ApruveServiceErrorException
	{
		final String methodName = "doUpdateOrder";
		LOGGER.entering(CLASSNAME, methodName);
		operationName = "updateOrderItem";
		JSONObject responseObject = call(requestObject, operationName);
		LOGGER.exiting(CLASSNAME, methodName);
		return new ZCApruveOrderItemUpdateResponseObject(responseObject);

	}
	
	public ZCApruveCreateOrderItemResponseObject doCreateOrderItem(ZCApruveCreateOrderItemRequestObject requestObject,
			PluginContext con, FinancialTransaction tran, boolean bool) throws ECException, ApruveServiceException,
			ApruveServiceErrorException
	{
		final String methodName = "doUpdateOrder";
		LOGGER.entering(CLASSNAME, methodName);
		operationName = "createOrderItem";
		JSONObject responseObject = call(requestObject, operationName);
		LOGGER.exiting(CLASSNAME, methodName);
		return new ZCApruveCreateOrderItemResponseObject(responseObject);

	}
	
	public ZCApruveOrderItemDeleteResponseObject doDeleteOrderItem(ZCApruveOrderItemDeleteRequestObject requestObject,
			PluginContext con, FinancialTransaction tran, boolean bool) throws ECException, ApruveServiceException,
			ApruveServiceErrorException
	{
		final String methodName = "doUpdateOrder";
		LOGGER.entering(CLASSNAME, methodName);
		operationName = "deleteOrderItem";
		JSONObject responseObject = call(requestObject, operationName);
		LOGGER.exiting(CLASSNAME, methodName);
		return new ZCApruveOrderItemDeleteResponseObject(responseObject);

	}

	public ZCApruveCreateInvoiceResponseObject doCreateInvoice(ZCApruveOrderCreateInvoiceRequestObject requestObject,
			PluginContext con, FinancialTransaction tran, boolean bool) throws ECException, ApruveServiceException,
			ApruveServiceErrorException
	{
		final String methodName = "doCreateInvoice";
		LOGGER.entering(CLASSNAME, methodName);
		operationName = "createInvoice";
		JSONObject responseObject = call(requestObject, operationName);
		LOGGER.exiting(CLASSNAME, methodName);
		return new ZCApruveCreateInvoiceResponseObject(responseObject);

	}

	private JSONObject call(ZCApruveRequestObject apruveRequestObject, String operationName) throws ApruveServiceException,
			ApruveServiceErrorException, ECException
	{
		final String METHODNAME = "call";
		LOGGER.entering(CLASSNAME, METHODNAME);
		String PPResponse = null;
		HttpResponse responseObject = null;
		String endPoint = "";
		String accept;
		String apruveAPIKey;
		String contentType;
		JSONObject jsonResponseObj = null;
		try
		{
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			accept = apruveUtility.getConfProperty("accept", storeId);
			apruveAPIKey = apruveUtility.getConfProperty("Merchant_API_Key", storeId);
			contentType = apruveUtility.getConfProperty("contentType", storeId);
			if (operationName.equals("retriveOrder"))
			{
				HttpGet get = new HttpGet(apruveRequestObject.getEndPoint());
				get.setHeader("accept", accept);
				get.setHeader("apruve-api-key", apruveAPIKey);

				HttpClient client = new DefaultHttpClient();
				responseObject = client.execute(get);
			}
			else if(operationName.equals("deleteOrderItem"))
			{
				HttpDelete delete = new HttpDelete(apruveRequestObject.getEndPoint());
				delete.setHeader("accept", accept);
				delete.setHeader("apruve-api-key", apruveAPIKey);
				HttpClient client = new DefaultHttpClient();
				responseObject = client.execute(delete);
			}
			else if(operationName.equals("updateOrder") || operationName.equals("updateOrderItem"))
			{
				HttpPut put = new HttpPut(apruveRequestObject.getEndPoint());
				put.setHeader("accept", accept);
				put.setHeader("apruve-api-key", apruveAPIKey);
				put.setHeader("content-type", contentType);
				if (apruveRequestObject.getJSONObj() != null)
				{
					StringEntity params = new StringEntity(apruveRequestObject.getJSONObj().toString());
					put.setEntity(params);
				}
				HttpClient client = new DefaultHttpClient();
				responseObject = client.execute(put);
			}
			else
			{
				HttpPost post = new HttpPost(apruveRequestObject.getEndPoint());
				post.setHeader("accept", accept);
				post.setHeader("apruve-api-key", apruveAPIKey);
				post.setHeader("content-type", contentType);

				if (apruveRequestObject.getJSONObj() != null)
				{
					StringEntity params = new StringEntity(apruveRequestObject.getJSONObj().toString());
					post.setEntity(params);
				}
				HttpClient client = new DefaultHttpClient();
				responseObject = client.execute(post);
			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(responseObject.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null)
			{
				result.append(line);
			}

			LOGGER.info(" response result is : " + result.toString() );
			jsonResponseObj = new JSONObject(result.toString());
			if (!isSuccess(responseObject)) {
				ApruveServiceError error = getErrors(responseObject,jsonResponseObj);
				LOGGER.info( " Response is not success so throwing an error   " + error );
				ApruveServiceErrorException apruveError = new ApruveServiceErrorException(error);
				apruveError.setResourceBundleName(ZCApruveProcessorHelper.getInstance(storeId).getErrorResourceBundleName(storeId));
				
				throw apruveError;
			}
			
		}
		catch(ApruveServiceErrorException apruveError){
			LOGGER.logp(Level.SEVERE, CLASSNAME, METHODNAME, "Encountered exception ["+apruveError.toString()+"]", apruveError);
			throw apruveError;
		}
		catch (Exception e)
		{
			LOGGER.logp(Level.SEVERE, CLASSNAME, METHODNAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ApruveServiceException(e.getMessage(), ApruveServiceException.UNEXPECTED);
		}
		LOGGER.info("Response object for opration : "+operationName+" is : " + jsonResponseObj.toString() );
		LOGGER.exiting(CLASSNAME, METHODNAME);

		return jsonResponseObj;
	}

	private ApruveServiceError getErrors(HttpResponse responseObj,JSONObject jsonObj)
	{

		final String METHODNAME = "getErrors";
		LOGGER.entering(CLASSNAME, METHODNAME);
		
		//int numOfErrors = getErrorSize(responseMap);
		ApruveServiceError errors;
		String shortMsg = "";
		String errorCode = responseObj.getStatusLine().getStatusCode()+"";
		try{
			shortMsg = jsonObj.getString("errors");
		}
		catch(Exception e){
			
		}
		errors = new ApruveServiceError(errorCode, shortMsg);
		LOGGER.info("Error message is : "+errors);
		LOGGER.exiting(CLASSNAME, METHODNAME);
		return errors;
	}

	private boolean isSuccess(HttpResponse response)
	{
		final String METHODNAME = "getErrors";
		LOGGER.entering(CLASSNAME, METHODNAME);
		int responseCode =  response.getStatusLine().getStatusCode();
		LOGGER.info("response code is : "+responseCode);
		LOGGER.exiting(CLASSNAME, METHODNAME);
		return (response != null && (responseCode == 200 || responseCode == 201));
	}

	/**
	 * Returns <code>true</code> if this service is initialized, otherwise
	 * <code>false</code>. This service is considered initialized if the
	 * internal reference to the service is not null.
	 * 
	 * @return <code>true</code> if this service is initialized, otherwise
	 *         <code>false</code>
	 */
	private static boolean isInitialized(Integer storeId)
	{
		final String METHODNAME = "isInitialized";
		LOGGER.entering(CLASSNAME, METHODNAME);
		LOGGER.exiting(CLASSNAME, METHODNAME);
		return mySelfs.containsKey(storeId);
	}

}
