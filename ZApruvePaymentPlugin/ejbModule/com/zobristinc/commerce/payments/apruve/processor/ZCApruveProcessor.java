package com.zobristinc.commerce.payments.apruve.processor;

import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.payments.plugin.FinancialTransaction;
import com.ibm.commerce.payments.plugin.PluginContext;
import com.zobristinc.commerce.payments.apruve.services.ApruveServiceErrorException;
import com.zobristinc.commerce.payments.apruve.services.ApruveServiceException;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateInvoiceResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateOrderItemRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateOrderItemResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderFinalizeRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderFinalizeResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderCreateInvoiceRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemDeleteRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemDeleteResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemUpdateRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemUpdateResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderUpdateRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderUpdateResponseObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveRetrieveOrderRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveRetrieveOrderResponseObject;

public interface ZCApruveProcessor {
	public ZCApruveOrderFinalizeResponseObject doFinalizingOrder(ZCApruveOrderFinalizeRequestObject requestObject,  PluginContext con, FinancialTransaction tran, boolean bool) throws  ECException, ApruveServiceException, ApruveServiceErrorException ;
	public ZCApruveCreateInvoiceResponseObject doCreateInvoice(ZCApruveOrderCreateInvoiceRequestObject requestObject,  PluginContext con, FinancialTransaction tran, boolean bool) throws  ECException, ApruveServiceException, ApruveServiceErrorException ;
	public ZCApruveOrderUpdateResponseObject doUpdateOrder(ZCApruveOrderUpdateRequestObject requestObject,  PluginContext con, FinancialTransaction tran, boolean bool) throws  ECException, ApruveServiceException, ApruveServiceErrorException ;
	public ZCApruveOrderItemUpdateResponseObject doUpdateOrderItem(ZCApruveOrderItemUpdateRequestObject requestObject,  PluginContext con, FinancialTransaction tran, boolean bool) throws  ECException, ApruveServiceException, ApruveServiceErrorException ;
	public  ZCApruveRetrieveOrderResponseObject doRetrieveOrder(ZCApruveRetrieveOrderRequestObject requestObject) throws ECException, ApruveServiceException,ApruveServiceErrorException;
	public ZCApruveCreateOrderItemResponseObject doCreateOrderItem(ZCApruveCreateOrderItemRequestObject requestObject,  PluginContext con, FinancialTransaction tran, boolean bool) throws  ECException, ApruveServiceException, ApruveServiceErrorException ;
	public ZCApruveOrderItemDeleteResponseObject doDeleteOrderItem(ZCApruveOrderItemDeleteRequestObject requestObject,  PluginContext con, FinancialTransaction tran, boolean bool) throws  ECException, ApruveServiceException, ApruveServiceErrorException ;
}
