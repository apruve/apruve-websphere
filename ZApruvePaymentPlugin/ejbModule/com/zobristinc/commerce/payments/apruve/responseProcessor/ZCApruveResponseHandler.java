package com.zobristinc.commerce.payments.apruve.responseProcessor;

import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.payments.plugin.FinancialTransaction;
import com.ibm.commerce.payments.plugin.PluginContext;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateInvoiceResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateOrderItemResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderFinalizeResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemDeleteResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemUpdateResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderUpdateResponseObject;

public interface ZCApruveResponseHandler {
	public void processApproveOrderUpdateTransactionResponse(ZCApruveOrderUpdateResponseObject responseObject,PluginContext con, FinancialTransaction tran, boolean bool) throws ECException ;
	public void processApproveOrderItemUpdateTransactionResponse(ZCApruveOrderItemUpdateResponseObject responseObject,PluginContext con, FinancialTransaction tran, boolean bool) throws ECException ;
	public void processApproveCreateOrderItemTransactionResponse(ZCApruveCreateOrderItemResponseObject responseObject,PluginContext con, FinancialTransaction tran, boolean bool) throws ECException ;
	public void processApproveDeleteOrderItemTransactionResponse(ZCApruveOrderItemDeleteResponseObject responseObject,PluginContext con, FinancialTransaction tran, boolean bool) throws ECException ;
	public void processApproveFinalizeTransactionResponse(ZCApruveOrderFinalizeResponseObject responseObject,PluginContext con, FinancialTransaction tran, boolean bool) throws ECException ;
	public void processApproveCreateInvoiceTransactionResponse(ZCApruveCreateInvoiceResponseObject responseObject,PluginContext con, FinancialTransaction tran, boolean bool) throws ECException ;
	
}
