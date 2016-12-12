package com.apruve.payment.plugin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.commerce.order.beans.OrderDataBean;
import com.ibm.commerce.order.objects.OrderItemAccessBean;
import com.ibm.commerce.payments.plugin.ExtendedData;
import com.ibm.commerce.payments.plugin.FinancialTransaction;
import com.ibm.commerce.payments.plugin.FunctionNotSupportedException;
import com.ibm.commerce.payments.plugin.InvalidDataException;
import com.ibm.commerce.payments.plugin.PaymentInstruction;
import com.ibm.commerce.payments.plugin.PluginContext;
import com.ibm.commerce.payments.plugin.PluginException;
import com.ibm.commerce.payments.plugin.PluginMessages;
import com.ibm.commerce.ras.ECTrace;
import com.ibm.commerce.ras.ECTraceIdentifiers;
import com.zobristinc.commerce.payments.apruve.builder.ZCApruveRequestBuilder;
import com.zobristinc.commerce.payments.apruve.processor.ZCApruveProcessor;
import com.zobristinc.commerce.payments.apruve.processor.helper.ZCApruveProcessorHelper;
import com.zobristinc.commerce.payments.apruve.responseProcessor.ZCApruveResponseHandler;
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
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemsObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderUpdateRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderUpdateResponseObject;

/**
 * Bean implementation class for Enterprise Bean: ZApruvePaymentPlugin
 */
public class ZApruvePaymentPluginBean implements javax.ejb.SessionBean {

	private static final String CLASS_NAME = "ZApruvePaymentPluginBean";
	private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
	private javax.ejb.SessionContext mySessionCtx;
	/**
	 * getSessionContext
	 */
	public javax.ejb.SessionContext getSessionContext() {
		return mySessionCtx;
	}
	/**
	 * setSessionContext
	 */
	public void setSessionContext(javax.ejb.SessionContext ctx) {
		mySessionCtx = ctx;
	}
	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws javax.ejb.CreateException {
	}
	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
	}
	/**
	 * ejbPassivate
	 */
	public void ejbPassivate() {
	}
	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}
	
	public String getMessage(PluginContext con, String message) throws PluginException
	{
		return PluginMessages.getMessage("com.zobristinc.payments.apruve.plugin.ApruveErrorMessages", message, null, con.getLocale(), null);
	}


	public FinancialTransaction credit(PluginContext con, FinancialTransaction tran, String message) throws PluginException
	{
		String METHOD_NAME = "credit";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		throw createFunctionNotSupportedException(con, tran, METHOD_NAME);
	}

	public FinancialTransaction getAvailableBalance(PluginContext con, FinancialTransaction tran, String message) throws PluginException
	{
		String METHOD_NAME = "getAvailableBalance(PluginContext, FinancialTransaction, String)";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		throw createFunctionNotSupportedException(con, tran, METHOD_NAME);
	}


	public void checkPaymentInstruction(PluginContext con, PaymentInstruction inst) throws PluginException
	{
	}

	public FinancialTransaction reverseCredit(PluginContext con, FinancialTransaction tran, boolean bool) throws PluginException
	{
		String METHOD_NAME = "reverseCredit(PluginContext, FinancialTransaction, boolean)";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		throw createFunctionNotSupportedException(con, tran, METHOD_NAME);
	}

	/**
	 * Correspond's to Apruve's DoVoid.
	 * 
	 * @param con
	 * @param tran
	 * @param bool
	 * @return
	 * @throws PluginException
	 */
	public FinancialTransaction reverseApproval(PluginContext con, FinancialTransaction tran, boolean bool) throws PluginException
	{
		final String METHOD_NAME = "reverseApproval";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		throw createFunctionNotSupportedException(con, tran, METHOD_NAME);
	}

	/**
	 * Corresponds to Apruve's RefundTransaction.
	 * 
	 * @param con
	 * @param tran
	 * @param bool
	 * @return
	 * @throws PluginException
	 */
	public FinancialTransaction reverseDeposit(PluginContext con, FinancialTransaction tran, boolean bool) throws PluginException
	{
		final String METHOD_NAME = "reverseDeposit(PluginContext,FinancialTransaction,boolean)";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		throw createFunctionNotSupportedException(con, tran, METHOD_NAME);
	}

	/**
	 * Corresponds to Apruve's 
	 * 
	 * @param con
	 * @param tran
	 * @param bool
	 * @return
	 * @throws PluginException
	 */
	public FinancialTransaction credit(PluginContext con, FinancialTransaction tran, boolean bool) throws PluginException
	{
		final String METHOD_NAME = "credit";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		throw createFunctionNotSupportedException(con, tran, METHOD_NAME);
	}

	/**
	 * Corresponds to Apruve's DoCapture.
	 * 
	 * @param con
	 * @param tran
	 * @param bool
	 * @return
	 * @throws PluginException
	 */
	public FinancialTransaction deposit(PluginContext con, FinancialTransaction tran, boolean bool) throws PluginException
	{
		final String METHOD_NAME = "deposit(PluginContext,FinancialTransaction,boolean)";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		throw createFunctionNotSupportedException(con, tran, METHOD_NAME);
	}
	
	/**
	 * Corresponds to Apruve Authorization or Order payments.
	 * 
	 * @param con
	 * @param tran
	 * @param bool
	 * @return
	 * @throws PluginException
	 */
	public FinancialTransaction approve(PluginContext con, FinancialTransaction tran, boolean retry) throws PluginException
	{
		final String METHOD_NAME = "approve(PluginContext,FinancialTransaction,boolean)";	
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		throw createFunctionNotSupportedException(con, tran, METHOD_NAME);

		
	}

	/**
	 * Corresponds to Apruve Authorization or Order payments.
	 * 
	 * @param con
	 * @param tran
	 * @param bool
	 * @return
	 * @throws PluginException
	 */
	public FinancialTransaction approveAndDeposit(PluginContext con, FinancialTransaction tran, boolean retry) throws PluginException
	{
		final String METHOD_NAME = "approveAndDeposit";
		ExtendedData extData = tran.getPayment().getPaymentInstruction().getExtendedData();
		//ECTrace.entry(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, METHOD_NAME);
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
		try
		{
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			String finalize_on_create = apruveUtility.getConfProperty("FINALIZE_ON_CREATE", storeId);
			String invoice_on_create = apruveUtility.getConfProperty("INVOICE_ON_CREATE", storeId);
			LOGGER.fine("finalize_on_create value is : "+finalize_on_create+" invoice_on_create value is : "+invoice_on_create);
			ZCApruveRequestBuilder builder = ZCApruveProcessorHelper.getInstance(storeId).getApruveRequestBuilder(storeId);
			String apruveOrderId = null;
			String orderId = tran.getPayment().getPaymentInstruction().getOrderId();
			BigDecimal totalAmount = null;
			totalAmount = tran.getPayment().getPaymentInstruction().getAmount();
			//start apruve
			LOGGER.fine("total amount in pluginBean : "+totalAmount+" commerce orderId is : "+orderId);
			apruveOrderId = extData.getString("apruveId");//"2326e5f1f7a1a1017f292ea6223068fe";
			ZCApruveProcessor service = getApruveProcessor(con, tran);
			ZCApruveResponseHandler responseHandler = ZCApruveProcessorHelper.getInstance(storeId).getApruveResponseProcessor(storeId);
			if(apruveOrderId != null)
			{
				if ( "false".equals(finalize_on_create) )
				{
					ZCApruveOrderUpdateRequestObject orderUpdateRequest = builder.getApruveUpdateOrderTransactionObject(con, tran, retry, totalAmount);
					ZCApruveOrderUpdateResponseObject orderUpdateResponse  = service.doUpdateOrder(orderUpdateRequest ,   con,  tran,  retry);
					updateOrderItems(orderUpdateResponse, con, tran, retry, totalAmount);
					ZCApruveOrderFinalizeRequestObject finalizeReq = builder.getApruveFinalizeTransactionObject(con, tran, retry,totalAmount);
					ECTrace.trace(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, METHOD_NAME, "Calling doReferenceTransaction");
					ZCApruveOrderFinalizeResponseObject response  = service.doFinalizingOrder(finalizeReq ,   con,  tran,  retry);
					responseHandler.processApproveFinalizeTransactionResponse(response, con, tran,retry);
					//apruveOrderId = doRefResponse.getTransactionId();
					
				}
				if("false".equals(invoice_on_create)){
						tran = createInvoice(con, tran, retry);
				}
			}
			tran.getPayment().setAvsCode((short) 5);//blocking the order.
			apruveUtility.markPaymentSuccessful(tran);
		}
		catch (ApruveServiceException e) {
			tran.setResponseCode("Error: " + e.getLevel());
			tran.setReasonCode("See Extended Data");
			extData.setString("Error", e.getMessage(), false);
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new PluginException(e);
			
		} catch (ApruveServiceErrorException e) {
			StringBuffer reason = new StringBuffer();
			reason.append(e.getError().getErrorCode());
			reason.append(",");
			extData.setString("Error_", e.getError().toString(), false);
			tran.setResponseCode("Apruve Error");
			tran.setReasonCode(reason.toString());
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new PluginException(e);
			
		} catch (Exception e) {
			tran.setResponseCode("Error");
			tran.setReasonCode("See Extended Data");
			extData.setString("Error", e.getMessage(), false);
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new PluginException(e);
			
		}
		finally {
			//ECTrace.exit(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, METHOD_NAME);
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return tran;
	}
	
	public boolean checkHealth()
	{
		return true;
	}
	
	public void validatePaymentInstruction(PluginContext con, PaymentInstruction inst) throws PluginException
	{
	}

	protected ZCApruveProcessor getApruveProcessor(PluginContext con, FinancialTransaction tran) throws InvalidDataException, PluginException {
		final String METHOD_NAME = "getApruveService";
		//ECTrace.entry(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, METHOD_NAME);
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		ZCApruveProcessor service = null;
		try {

			Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
			service = ZCApruveProcessorHelper.getInstance(storeId).getApruveProcessor(storeId);
		} 
		catch (Exception e)
		{
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new PluginException(e);
		} finally {
			//ECTrace.exit(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, METHOD_NAME);
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return service;
	}
	
	private FunctionNotSupportedException createFunctionNotSupportedException(PluginContext pluginContext, FinancialTransaction tran, String methodName)
	    {
			final String METHOD_NAME = "createFunctionNotSupportedException";
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
	        FunctionNotSupportedException e = new FunctionNotSupportedException();
	        e.setClassSource("ZobristApruvePlugin");
	        e.setMethodSource(methodName);
	        e.setMessageKey("PLUGIN_FUNCTION_NOT_SUPPORTED");
	        e.addProperty("plugin context", pluginContext);
	        e.addProperty("Financial transaction", tran);
	        LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	        return e;
	    }
	
	public FinancialTransaction createInvoice(PluginContext con, FinancialTransaction tran, boolean retry) throws PluginException
	{
		final String METHOD_NAME = "createInvoice";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		try{
			Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
			BigDecimal totalAmount = null;
			totalAmount = tran.getPayment().getPaymentInstruction().getAmount();
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			ZCApruveRequestBuilder builder = ZCApruveProcessorHelper.getInstance(storeId).getApruveRequestBuilder(storeId);
			ZCApruveProcessor service = getApruveProcessor(con, tran);
			ZCApruveResponseHandler responseHandler = ZCApruveProcessorHelper.getInstance(storeId).getApruveResponseProcessor(storeId);
			ZCApruveOrderCreateInvoiceRequestObject createInvoiceRequest = builder.getApruveCreateInvoiceTransactionObject(con, tran, retry,totalAmount);
			ZCApruveCreateInvoiceResponseObject createInvoiceResponse  = service.doCreateInvoice(createInvoiceRequest ,   con,  tran,  retry);
			responseHandler.processApproveCreateInvoiceTransactionResponse(createInvoiceResponse, con, tran,retry);
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new PluginException(e);
		}
		finally {
			//ECTrace.exit(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, METHOD_NAME);
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return tran;
	}
	
	public void updateOrderItems(ZCApruveOrderUpdateResponseObject orderUpdateResponse,PluginContext con, FinancialTransaction tran, boolean retry,BigDecimal totalAmount) throws PluginException
	{
		final String METHOD_NAME = "updateOrderItems";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		String orderId = tran.getPayment().getPaymentInstruction().getOrderId();
		Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
		boolean otherPayments = false;
		HashMap apruveOrderItemsMap = new HashMap();
		
		ZCApruveProcessorHelper helper = new ZCApruveProcessorHelper();
		ExtendedData extData = tran.getPayment().getPaymentInstruction().getExtendedData();
		int langId = (int) (Double.parseDouble(extData.getString("langId")));
		
		try
		{
			
			OrderDataBean odb = new OrderDataBean();
			odb.setInitKey_orderId(orderId);
			odb.refreshCopyHelper();
			System.out.println("price is : "+ZCApruveUtility.getInstance(storeId).getTotalAmount(odb)+" odb.getTotalProductPrice() "+odb.getTotalProductPrice()+" price in transaction is : "+tran.getPayment().getPaymentInstruction().getAmount());
			if(!ZCApruveUtility.getInstance(storeId).getTotalAmount(odb).equals(tran.getPayment().getPaymentInstruction().getAmount()))
			{
				otherPayments = true;
			}
			
			OrderItemAccessBean[] commereceOrderitems = odb.getOrderItems();
			
			apruveOrderItemsMap = (HashMap)orderUpdateResponse.getApruveOrderItemsMap();
			System.out.println("apruve order items map is : "+apruveOrderItemsMap);
			LOGGER.fine("apruve order items map is : "+apruveOrderItemsMap);
			
			ZCApruveRequestBuilder builder = ZCApruveProcessorHelper.getInstance(storeId).getApruveRequestBuilder(storeId);
			ZCApruveProcessor service = getApruveProcessor(con, tran);
			
			for(int i = 0;i < commereceOrderitems.length;i++)
			{
				
				if(apruveOrderItemsMap.containsKey(commereceOrderitems[i].getPartNumber()))
				{
					
					ZCApruveOrderItemsObject apruveOrderItemObj = (ZCApruveOrderItemsObject) apruveOrderItemsMap.get(commereceOrderitems[i].getPartNumber());
					
					if(apruveOrderItemObj.getQuantity() != null && apruveOrderItemObj.getQuantity() != (int)Double.parseDouble(commereceOrderitems[i].getQuantity()))
					{
						ZCApruveOrderItemUpdateRequestObject orderItemUpdateRequest = builder.getApruveUpdateOrderItemTransactionObject(con, tran, retry, totalAmount,commereceOrderitems[i],apruveOrderItemObj);
						ZCApruveOrderItemUpdateResponseObject orderItemUpdateResponseObj = service.doUpdateOrderItem(orderItemUpdateRequest ,   con,  tran,  retry);
						
					}
					apruveOrderItemsMap.remove(commereceOrderitems[i].getPartNumber());
				}
				else{
					//Add order item
					ZCApruveCreateOrderItemRequestObject createOrderItemRequestObj = builder.getApruveCreateOrderItemTransactionObject(con, tran, retry, totalAmount,commereceOrderitems[i]);
					ZCApruveCreateOrderItemResponseObject createOrderItemResponseObj = service.doCreateOrderItem(createOrderItemRequestObj ,   con,  tran,  retry);
				}
			}
			if(otherPayments && apruveOrderItemsMap.containsKey(helper.getApruveDetailValue("Other_Payments",storeId, langId)))
			{
				ZCApruveOrderItemsObject apruveOrderItemObj = (ZCApruveOrderItemsObject) apruveOrderItemsMap.get(helper.getApruveDetailValue("Other_Payments",storeId, langId));
				if(apruveOrderItemObj.getTotalAmount_cents() != ((odb.getTotalProductPriceInEJBType().subtract(tran.getPayment().getPaymentInstruction().getAmount())).multiply(new BigDecimal(-100))).intValue())
				{	
					ZCApruveOrderItemUpdateRequestObject orderItemUpdateRequest = builder.getApruveUpdateOtherPaymentsOrderItemTransactionObject(con, tran, retry, totalAmount,apruveOrderItemObj);
					ZCApruveOrderItemUpdateResponseObject orderItemUpdateResponseObj = service.doUpdateOrderItem(orderItemUpdateRequest ,   con,  tran,  retry);
				}
				apruveOrderItemsMap.remove(helper.getApruveDetailValue("Other_Payments",storeId, langId));
			}
			else if(otherPayments && !apruveOrderItemsMap.containsKey(helper.getApruveDetailValue("Other_Payments",storeId, langId)))
			{
				ZCApruveCreateOrderItemRequestObject createOrderItemRequestObj = builder.getApruveCreateOtherPaymentsOrderItemTransactionObject(con, tran, retry, totalAmount);
				ZCApruveCreateOrderItemResponseObject createOrderItemResponseObj = service.doCreateOrderItem(createOrderItemRequestObj ,   con,  tran,  retry);
			}
			if(!apruveOrderItemsMap.containsKey("") && odb.getTotalAdjustmentInEJBType() != null && (odb.getTotalAdjustmentInEJBType().multiply(new BigDecimal(100))).intValue() != 0)
			{
				//Need to add adjustments value.
				ZCApruveCreateOrderItemRequestObject createOrderItemRequestObj = builder.getApruveCreateAdjustmentsOrderItemTransactionObject(con, tran, retry, totalAmount);
				ZCApruveCreateOrderItemResponseObject createOrderItemResponseObj = service.doCreateOrderItem(createOrderItemRequestObj ,   con,  tran,  retry);
			}
			if(apruveOrderItemsMap.containsKey(""))
			{
				//update adjustments
				ZCApruveOrderItemsObject apruveOrderItemObj = (ZCApruveOrderItemsObject) apruveOrderItemsMap.get("");
				
				if(apruveOrderItemObj.getTotalAmount_cents() != (odb.getTotalAdjustmentInEJBType().multiply(new BigDecimal(100))).intValue())
				{
					ZCApruveOrderItemUpdateRequestObject orderItemUpdateRequest = builder.getApruveUpdateAdjustmentsOrderItemTransactionObject(con, tran, retry, totalAmount,apruveOrderItemObj);
					ZCApruveOrderItemUpdateResponseObject orderItemUpdateResponseObj = service.doUpdateOrderItem(orderItemUpdateRequest ,   con,  tran,  retry);
				}
				apruveOrderItemsMap.remove("");
			}
			if(apruveOrderItemsMap.size() != 0)
			{
				//delete the order items
				Iterator entries = apruveOrderItemsMap.entrySet().iterator();
				while (entries.hasNext())
				{
					 Entry thisEntry = (Entry) entries.next();
					ZCApruveOrderItemsObject apruveOrderItemObj = (ZCApruveOrderItemsObject) thisEntry.getValue();
					ZCApruveOrderItemDeleteRequestObject orderItemDeleteRequestObject = builder.getApruveDeleteOrderItemTransactionObject(con, tran, retry, totalAmount,apruveOrderItemObj);
					ZCApruveOrderItemDeleteResponseObject orderItemDeleteResponseObj = service.doDeleteOrderItem(orderItemDeleteRequestObject ,   con,  tran,  retry);
				}
			}
			
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new PluginException(e);
		}
		finally {
			//ECTrace.exit(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, METHOD_NAME);
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
	}
	
}
