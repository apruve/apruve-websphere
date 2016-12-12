package com.zobristinc.commerce.payments.apruve.responseProcessor;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.payments.plugin.FinancialTransaction;
import com.ibm.commerce.payments.plugin.PluginContext;
import com.ibm.commerce.ras.ECMessage;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateInvoiceResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateOrderItemResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderFinalizeResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemDeleteResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemUpdateResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderUpdateResponseObject;

public class ZCApruveDirectResponseHandler implements ZCApruveResponseHandler {
String apruveBuilderType;
	
	final String CLASS_NAME = ZCApruveDirectResponseHandler.class.getName();
	final Logger LOGGER = LoggingHelper.getLogger(ZCApruveDirectResponseHandler.class);

	
	//public ZCApruveDirectResponseHandler(String apruveBuilderType)
	public ZCApruveDirectResponseHandler()
	{
		//this.apruveBuilderType = apruveBuilderType;
		super();
	}

	@Override
	public void processApproveFinalizeTransactionResponse(
			ZCApruveOrderFinalizeResponseObject responseObject, PluginContext con,
			FinancialTransaction tran, boolean bool) throws ECException {
		String METHOD_NAME = "processApproveDoReferenceTransactionResponse";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		try{
			
			Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
			System.out.println("status in response : "+responseObject.getValue("status")+" id in response : "+responseObject.getValue("id"));
			tran.setReferenceNumber( responseObject.getValue("id"));//apruveOrderId
			String amount_cents = responseObject.getValue("amount_cents").toString();
			tran.setProcessedAmount(new BigDecimal(amount_cents).divide(new BigDecimal(100)));//Amount
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			e.printStackTrace();
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	}
	@Override
	public void processApproveCreateInvoiceTransactionResponse(
			ZCApruveCreateInvoiceResponseObject responseObject, PluginContext con,
			FinancialTransaction tran, boolean bool) throws ECException {
		String METHOD_NAME = "processApproveCreateInvoiceTransactionResponse";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		try{
			
			System.out.println(responseObject.getValue("status"));
			tran.setReferenceNumber( responseObject.getValue("id"));//apruveOrderId
			String amount_cents = responseObject.getValue("amount_cents").toString();
			tran.setProcessedAmount(new BigDecimal(amount_cents).divide(new BigDecimal(100)));//Amount
		
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			e.printStackTrace();
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	}
	@Override
	public void processApproveOrderUpdateTransactionResponse(
			ZCApruveOrderUpdateResponseObject responseObject, PluginContext con,
			FinancialTransaction tran, boolean bool) throws ECException {
		String METHOD_NAME = "processApproveCreateInvoiceTransactionResponse";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		try{
			
			
		
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	}
	@Override
	public void processApproveOrderItemUpdateTransactionResponse(
			ZCApruveOrderItemUpdateResponseObject responseObject, PluginContext con,
			FinancialTransaction tran, boolean bool) throws ECException {
		String METHOD_NAME = "processApproveCreateInvoiceTransactionResponse";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		try{
			
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	}
	@Override
	public void processApproveCreateOrderItemTransactionResponse(
			ZCApruveCreateOrderItemResponseObject responseObject, PluginContext con,
			FinancialTransaction tran, boolean bool) throws ECException {
		String METHOD_NAME = "processApproveCreateInvoiceTransactionResponse";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		try{
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	}
	@Override
	public void processApproveDeleteOrderItemTransactionResponse(
			ZCApruveOrderItemDeleteResponseObject responseObject, PluginContext con,
			FinancialTransaction tran, boolean bool) throws ECException {
		String METHOD_NAME = "processApproveCreateInvoiceTransactionResponse";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		try{
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	}
}
