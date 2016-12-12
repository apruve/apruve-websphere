package com.zobristinc.commerce.payments.apruve.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.commerce.catalog.objects.CatalogEntryAccessBean;
import com.ibm.commerce.catalog.objects.CatalogEntryDescriptionAccessBean;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.order.beans.OrderDataBean;
import com.ibm.commerce.order.objects.OrderItemAccessBean;
import com.ibm.commerce.payments.plugin.ExtendedData;
import com.ibm.commerce.payments.plugin.FinancialTransaction;
import com.ibm.commerce.payments.plugin.PluginContext;
import com.ibm.commerce.ras.ECMessage;
import com.zobristinc.commerce.integration.apruve.commands.OrderItemsDetail;
import com.zobristinc.commerce.payments.apruve.processor.helper.ZCApruveProcessorHelper;
import com.zobristinc.commerce.payments.apruve.util.ZCApruveConstants;
import com.zobristinc.commerce.payments.apruve.util.ZCApruveUtility;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateOrderItemRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateOrderRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderFinalizeRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderCreateInvoiceRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemDeleteRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemUpdateRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemsObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderUpdateRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveRetrieveOrderRequestObject;

public class ZCApruveDirectRequestBuilder extends ZCApruveRequestBuilder {
	public String CLASS_NAME = "ZCApruveDirectRequestBuilder";
	final Logger LOGGER = LoggingHelper.getLogger(ZCApruveDirectRequestBuilder.class);
	Integer langId = -1;
	
//	public ZCApruveDirectRequestBuilder(String apruveBuilderType)
	public ZCApruveDirectRequestBuilder()
	{
		super();
	}

	

	@Override
	public  ZCApruveOrderFinalizeRequestObject getApruveFinalizeTransactionObject(
			PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount) throws  ECException 
	{
				
		String METHOD_NAME = "getApruveFinalizeTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
		
		ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
		ExtendedData extData = tran.getPayment().getPaymentInstruction().getExtendedData();
		
		String apruveId = extData.getString("apruveId");
		String startURL = apruveUtility.getConfProperty("APRUVE_ORDER_FINALIZE_URL_START", storeId);
		String endURL = apruveUtility.getConfProperty("APRUVE_ORDER_FINALIZE_URL_END", storeId);
		
		String url = startURL+apruveId.trim()+endURL;
		
		LOGGER.info(" FinalizeOrderUrl is : " + url );
		
		ZCApruveOrderFinalizeRequestObject finalizeObject = new ZCApruveOrderFinalizeRequestObject();
		
		finalizeObject.setEndPoint(url);
		
		LOGGER.info(" finalizeObject " + finalizeObject );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return finalizeObject;
		
	}
	
	@Override
	public  ZCApruveOrderUpdateRequestObject getApruveUpdateOrderTransactionObject(
			PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount) throws  ECException 
	{
				
		String METHOD_NAME = "getApruveUpdateOrderTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		ZCApruveOrderUpdateRequestObject updateOrderObject = new ZCApruveOrderUpdateRequestObject();
		try
		{
			Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
			
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			ExtendedData extData = tran.getPayment().getPaymentInstruction().getExtendedData();
			
			String orderId = tran.getPayment().getPaymentInstruction().getOrderId();
			String apruveId = extData.getString("apruveId");
			String updateOrderURL = apruveUtility.getConfProperty("APRUVE_UPDATE_ORDER_URL", storeId);
			String url = updateOrderURL+apruveId.trim();
			
			LOGGER.info(" UpdateOrderUrl is : " + url );
			
			updateOrderObject.setEndPoint(url);
			updateOrderObject.setAmount(amount.multiply(new BigDecimal(100)));
			
			OrderDataBean odb = new OrderDataBean();
			odb.setInitKey_orderId(orderId);
			odb.refreshCopyHelper();
			
			updateOrderObject.setShipping_cents((int) (Double.parseDouble(odb.getTotalShippingCharge())*100));
			updateOrderObject.setTax_cents((int)((Double.parseDouble(odb.getTotalTax()) + Double.parseDouble(odb.getTotalShippingTax()))*100));
			
			if(odb.getExpireDate() != null)
				updateOrderObject.setExpire_at(odb.getExpireDate().toString());
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.info(" updateOrderRequestObject " + updateOrderObject.getJSONRequestObj().toString() );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return updateOrderObject;
		
	}
	
	@Override
	public  ZCApruveOrderCreateInvoiceRequestObject getApruveCreateInvoiceTransactionObject(
			PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount) throws  ECException 
	{
				
		String METHOD_NAME = "getApruveCreateInvoiceTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		ArrayList<OrderItemsDetail> order_items = new ArrayList<OrderItemsDetail>();
		ZCApruveOrderCreateInvoiceRequestObject invoiceObject = new ZCApruveOrderCreateInvoiceRequestObject();
		
		try
		{
			Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
			String merchant_order_id = tran.getPayment().getPaymentInstruction().getOrderId();
			
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			ExtendedData extData = tran.getPayment().getPaymentInstruction().getExtendedData();
			langId = (int) (Double.parseDouble(extData.getString("langId")));
			OrderDataBean odb = new OrderDataBean();
			odb.setInitKey_orderId(merchant_order_id);
			odb.refreshCopyHelper();
			
			String apruveId = extData.getString("apruveId");
			String createInvoiceURLStart = apruveUtility.getConfProperty("APRUVE_ORDER_CREATE_INVOICE_URL_START", storeId);
			String createInvoiceURLEnd = apruveUtility.getConfProperty("APRUVE_ORDER_CREATE_INVOICE_URL_END", storeId);
			String url = createInvoiceURLStart+apruveId.trim()+createInvoiceURLEnd;
			
			LOGGER.info(" CreateInvoiceUrl is : " + url );
			
			invoiceObject.setEndPoint(url);
			invoiceObject.setAmount(amount.movePointRight(2).setScale(0,BigDecimal.ROUND_HALF_DOWN));// change it to cents,  (int) Double.parseDouble(odb.getTotalProductPrice())*100
			invoiceObject.setCurrency(tran.getPayment().getPaymentInstruction().getCurrency());
			invoiceObject.setIssueOnCreate(true);
			invoiceObject.setTax_cents((int) ((Double.parseDouble(odb.getTotalTax()) + Double.parseDouble(odb.getTotalShippingTax()))*100));
			invoiceObject.setShipping_cents((int) (Double.parseDouble(odb.getTotalShippingCharge())*100));
			if(odb.getExpireDate() != null)
				invoiceObject.setDue_at(odb.getExpireDate().toString());
			addOrderItems(amount, invoiceObject, odb, storeId, langId);
			
		}
		catch(Exception e)
		{
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		
		LOGGER.info(" createInvoiceRequestObject : " + invoiceObject.getJSONRequestObj().toString() );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return invoiceObject;
		
	}
	
	public  ZCApruveCreateOrderRequestObject getApruveCreateOrderTransactionObject(
			String merchant_order_id,int storeId,Integer langId) throws  ECException 
	{
				
		String METHOD_NAME = "getApruveCreateOrderTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		//ArrayList<OrderItemsDetail> order_items = new ArrayList<OrderItemsDetail>();
		ZCApruveCreateOrderRequestObject createOrderObject = new ZCApruveCreateOrderRequestObject();
		try
		{
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			createOrderObject.setMerchant_id(apruveUtility.getConfProperty("Merchant_ID", storeId));
			createOrderObject.setAPI_Key(apruveUtility.getConfProperty("Merchant_API_Key", storeId));
			createOrderObject.setFinalize_on_create(apruveUtility.getConfProperty("FINALIZE_ON_CREATE", storeId));
			createOrderObject.setInvoice_on_create(apruveUtility.getConfProperty("INVOICE_ON_CREATE", storeId));
			createOrderObject.setMerchant_order_id(merchant_order_id);
			
			OrderDataBean odb = new OrderDataBean();
			odb.setInitKey_orderId(merchant_order_id);
			odb.refreshCopyHelper();
			
			createOrderObject.setCurrency(odb.getCurrency());
			createOrderObject.setTax_cents((int) ((Double.parseDouble(odb.getTotalTax()) + Double.parseDouble(odb.getTotalShippingTax()))*100));
			
			BigDecimal amount = getTotalAmount(merchant_order_id).subtract(apruveUtility.getGiftCardAmount(odb, storeId));
			createOrderObject.setAmount_cents((amount.multiply(new BigDecimal(100))).intValue());
			createOrderObject.setShipping_cents((int) (Double.parseDouble(odb.getTotalShippingCharge())*100));
			
			if(odb.getExpireDate() != null)
			createOrderObject.setExpire_at(odb.getExpireDate().toString());
			
			addOrderItems(amount, createOrderObject, odb, storeId, langId);
			//createOrderObject.setOrder_items(order_items);
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.info(" createOrderObject " + createOrderObject.getJSONRequestObj().toString() );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return createOrderObject;
		
	}
	
	@Override
	public  ZCApruveRetrieveOrderRequestObject getApruveRetrieveOrderTransactionObject(String url) throws  ECException 
	{
				
		String METHOD_NAME = "getApruveFinalizeTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		LOGGER.info(" Retrieve Order URL is : " + url );
		
		ZCApruveRetrieveOrderRequestObject retrieveOrderObj = new ZCApruveRetrieveOrderRequestObject();
		retrieveOrderObj.setEndPoint(url);
		
		//LOGGER.info(" retrieveOrderRequestObj " + retrieveOrderObj.getJSONRequestObj().toString() );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return retrieveOrderObj;
		
	}
	
	@Override
	public  ZCApruveOrderItemUpdateRequestObject getApruveUpdateOrderItemTransactionObject(
			PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount,OrderItemAccessBean commerceOrderItem,ZCApruveOrderItemsObject apruveOrderItemObj) throws  ECException 
	{
				
		String METHOD_NAME = "getApruveUpdateOrderItemTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());

		ZCApruveOrderItemUpdateRequestObject updateOrderItemObject = new ZCApruveOrderItemUpdateRequestObject();
		try{
			
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			ExtendedData extData = tran.getPayment().getPaymentInstruction().getExtendedData();
			CatalogEntryAccessBean abCatalogEntry = commerceOrderItem.getCatalogEntry();
			
			langId = (int) (Double.parseDouble(extData.getString("langId")));
			CatalogEntryDescriptionAccessBean abCatalogEntryDescription = abCatalogEntry.getDescription(langId, storeId);
			
			
			String orderId = tran.getPayment().getPaymentInstruction().getOrderId();
			String apruveId = extData.getString("apruveId");
			String updateOrderItemsURL = apruveUtility.getConfProperty("APRUVE_UPDATE_ORDER_ITEMS_URL", storeId);
			
			String url = updateOrderItemsURL+apruveOrderItemObj.getOrderItemId();
			
			LOGGER.info(" UpdateOrderItemUrl is : " + url );
			
			updateOrderItemObject.setEndPoint(url);
			
			if(commerceOrderItem.getPrice() != null && !"".equals(commerceOrderItem.getPrice()))
				updateOrderItemObject.setPrice_ea_cents((int) (Double.parseDouble(commerceOrderItem.getPrice())*100));
			
			if(commerceOrderItem.getQuantity() != null && !"".equals(commerceOrderItem.getQuantity()))
				updateOrderItemObject.setQuantity((int) Double.parseDouble(commerceOrderItem.getQuantity()));
			
			if(commerceOrderItem.getTotalProduct() != null && !"".equals(commerceOrderItem.getTotalProduct()))
				updateOrderItemObject.setPrice_total_cents((int) (Double.parseDouble(commerceOrderItem.getTotalProduct())*100));
			
			if(commerceOrderItem.getCurrency() != null && !"".equals(commerceOrderItem.getCurrency()))
				updateOrderItemObject.setCurrency(commerceOrderItem.getCurrency());
			
			if(abCatalogEntryDescription.getName() != null && !"".equals(abCatalogEntryDescription.getName()))
				updateOrderItemObject.setTitle(abCatalogEntryDescription.getName());
			
			if(apruveOrderItemObj.getMerchantNotes() != null && !"".equals(apruveOrderItemObj.getMerchantNotes()))
				updateOrderItemObject.setMerchant_notes(apruveOrderItemObj.getMerchantNotes());
			
			if(abCatalogEntryDescription.getShortDescription() != null && !"".equals(abCatalogEntryDescription.getShortDescription()))
				updateOrderItemObject.setDescription(abCatalogEntryDescription.getShortDescription());
			
			if(commerceOrderItem.getPartNumber() != null && !"".equals(commerceOrderItem.getPartNumber()))
				updateOrderItemObject.setSku(commerceOrderItem.getPartNumber());
			
			if(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId) != null && !"".equals(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId)))
				updateOrderItemObject.setVariant_info(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId));
			
			if(abCatalogEntry.getManufacturerName() != null && !"".equals(abCatalogEntry.getManufacturerName()))
				updateOrderItemObject.setVendor(abCatalogEntry.getManufacturerName());
			
			if(apruveOrderItemObj.getProductURL() != null && !"".equals(apruveOrderItemObj.getProductURL()))
				updateOrderItemObject.setProduct_url(apruveOrderItemObj.getProductURL());
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.info(" updateOrderItemObject " + updateOrderItemObject.getJSONRequestObj().toString() );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return updateOrderItemObject;
		
	}
	
	@Override
	public  ZCApruveOrderItemUpdateRequestObject getApruveUpdateAdjustmentsOrderItemTransactionObject(
			PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount,ZCApruveOrderItemsObject apruveOrderItemObj) throws  ECException 
	{
				
		String METHOD_NAME = "getApruveUpdateAdjustmentsOrderTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		String merchant_order_id = tran.getPayment().getPaymentInstruction().getOrderId();
		Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
		
		ZCApruveOrderItemUpdateRequestObject updateOrderItemObject = new ZCApruveOrderItemUpdateRequestObject();
		
		try
		{
			OrderDataBean odb = new OrderDataBean();
			odb.setInitKey_orderId(merchant_order_id);
			odb.refreshCopyHelper();
			
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			ExtendedData extData = tran.getPayment().getPaymentInstruction().getExtendedData();
			langId = (int) (Double.parseDouble(extData.getString("langId")));
			
			String orderId = tran.getPayment().getPaymentInstruction().getOrderId();
			String apruveId = extData.getString("apruveId");
			String updateOrderItemsURL = apruveUtility.getConfProperty("APRUVE_UPDATE_ORDER_ITEMS_URL", storeId);
			String url = updateOrderItemsURL+apruveOrderItemObj.getOrderItemId();
			
			LOGGER.info(" UpdateAdjustmentsOrderItemUrl is : " + url );
			
			
			updateOrderItemObject.setEndPoint(url);
			
			if(odb.getTotalAdjustmentInEJBType() != null && !"".equals(odb.getTotalAdjustmentInEJBType()))
				updateOrderItemObject.setPrice_ea_cents((odb.getTotalAdjustmentInEJBType().multiply(new BigDecimal(100))).intValue());
			if(apruveOrderItemObj.getQuantity() != null && apruveOrderItemObj.getQuantity() != 1)
				updateOrderItemObject.setQuantity(1);
			if(odb.getTotalAdjustmentInEJBType() != null && !"".equals(odb.getTotalAdjustmentInEJBType()))
				updateOrderItemObject.setPrice_total_cents((odb.getTotalAdjustmentInEJBType().multiply(new BigDecimal(100))).intValue());
			/*if(apruveOrderItemObj.getCurrency() != null && commerceOrderItem.getCurrency() != null && !apruveOrderItemObj.getCurrency().equals(commerceOrderItem.getCurrency()))
				updateOrderItemObject.setCurrency(commerceOrderItem.getCurrency());*/
			if(apruveOrderItemObj.getTitle() != null)
				updateOrderItemObject.setTitle(apruveOrderItemObj.getTitle());
			/*if(apruveOrderItemObj.getMerchantNotes() != null && !"".equals(apruveOrderItemObj.getMerchantNotes()))
				updateOrderItemObject.setMerchant_notes(apruveOrderItemObj.getMerchantNotes());
			if(apruveOrderItemObj.getDescription() != null && !"".equals(apruveOrderItemObj.getDescription()) && abCatalogEntryDescription.getShortDescription() != null && !"".equals(abCatalogEntryDescription.getShortDescription()))
				updateOrderItemObject.setDescription(abCatalogEntryDescription.getShortDescription());
			if(apruveOrderItemObj.getSku() != null && !"".equals(apruveOrderItemObj.getSku()) && commerceOrderItem.getPartNumber() != null && !"".equals(commerceOrderItem.getPartNumber()) && !apruveOrderItemObj.getSku().equals(commerceOrderItem.getPartNumber()))
				updateOrderItemObject.setSku(commerceOrderItem.getPartNumber());
			if(apruveOrderItemObj.getVariant() != null && !"".equals(apruveOrderItemObj.getVariant()) && !apruveOrderItemObj.getVariant().equals(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId)))
				updateOrderItemObject.setVariant_info(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId));
			if(apruveOrderItemObj.getVendor() != null && !"".equals(apruveOrderItemObj.getVendor()) && !apruveOrderItemObj.getVendor().equals(abCatalogEntry.getManufacturerName()))
				updateOrderItemObject.setVendor(abCatalogEntry.getManufacturerName());
			if(apruveOrderItemObj.getProductURL() != null && !"".equals(apruveOrderItemObj.getProductURL()))
				updateOrderItemObject.setProduct_url(apruveOrderItemObj.getProductURL());*/
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.info(" updateAdjustmentOrderItemObject " + updateOrderItemObject.getJSONRequestObj().toString() );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return updateOrderItemObject;
		
	}
	
	@Override
	public  ZCApruveOrderItemUpdateRequestObject getApruveUpdateOtherPaymentsOrderItemTransactionObject(
			PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount,ZCApruveOrderItemsObject apruveOrderItemObj) throws  ECException 
	{
				
		String METHOD_NAME = "getApruveUpdateOtherPaymentsOrderItemTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		String merchant_order_id = tran.getPayment().getPaymentInstruction().getOrderId();
		Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
		
		ZCApruveOrderItemUpdateRequestObject updateOrderItemObject = new ZCApruveOrderItemUpdateRequestObject();
		
		try
		{
			OrderDataBean odb = new OrderDataBean();
			odb.setInitKey_orderId(merchant_order_id);
			odb.refreshCopyHelper();
			
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			ExtendedData extData = tran.getPayment().getPaymentInstruction().getExtendedData();
			langId = (int) (Double.parseDouble(extData.getString("langId")));
			
			String orderId = tran.getPayment().getPaymentInstruction().getOrderId();
			String apruveId = extData.getString("apruveId");
			String updateOrderItemsURL = apruveUtility.getConfProperty("APRUVE_UPDATE_ORDER_ITEMS_URL", storeId);
			String url = updateOrderItemsURL+apruveOrderItemObj.getOrderItemId();
			
			LOGGER.info(" UpdateOtherPaymentsOrderItemUrl is : " + url );
			
			
			updateOrderItemObject.setEndPoint(url);
			
			if(odb.getTotalProductPrice() != null && tran.getPayment().getPaymentInstruction().getAmount() != null && !"".equals(odb.getTotalProductPrice()) && !"".equals(tran.getPayment().getPaymentInstruction().getAmount()))
				updateOrderItemObject.setPrice_ea_cents(((ZCApruveUtility.getInstance(storeId).getTotalAmount(odb).subtract(tran.getPayment().getPaymentInstruction().getAmount())).multiply(new BigDecimal(-100))).intValue());
			if(apruveOrderItemObj.getQuantity() != null && apruveOrderItemObj.getQuantity() != 1)
				updateOrderItemObject.setQuantity(1);
			if(odb.getTotalProductPrice() != null && tran.getPayment().getPaymentInstruction().getAmount() != null && !"".equals(odb.getTotalProductPrice()) && !"".equals(tran.getPayment().getPaymentInstruction().getAmount()))
				updateOrderItemObject.setPrice_total_cents(((ZCApruveUtility.getInstance(storeId).getTotalAmount(odb).subtract(tran.getPayment().getPaymentInstruction().getAmount())).multiply(new BigDecimal(-100))).intValue());
			/*if(apruveOrderItemObj.getCurrency() != null && commerceOrderItem.getCurrency() != null && !apruveOrderItemObj.getCurrency().equals(commerceOrderItem.getCurrency()))
				updateOrderItemObject.setCurrency(commerceOrderItem.getCurrency());*/
			if(apruveOrderItemObj.getTitle() != null)
				updateOrderItemObject.setTitle(apruveOrderItemObj.getTitle());
			if(apruveOrderItemObj.getSku() != null && !"".equals(apruveOrderItemObj.getSku()))
				updateOrderItemObject.setSku(apruveOrderItemObj.getSku());
			/*if(apruveOrderItemObj.getMerchantNotes() != null && !"".equals(apruveOrderItemObj.getMerchantNotes()))
				updateOrderItemObject.setMerchant_notes(apruveOrderItemObj.getMerchantNotes());
			if(apruveOrderItemObj.getDescription() != null && !"".equals(apruveOrderItemObj.getDescription()) && abCatalogEntryDescription.getShortDescription() != null && !"".equals(abCatalogEntryDescription.getShortDescription()))
				updateOrderItemObject.setDescription(abCatalogEntryDescription.getShortDescription());
			if(apruveOrderItemObj.getSku() != null && !"".equals(apruveOrderItemObj.getSku()) && commerceOrderItem.getPartNumber() != null && !"".equals(commerceOrderItem.getPartNumber()) && !apruveOrderItemObj.getSku().equals(commerceOrderItem.getPartNumber()))
				updateOrderItemObject.setSku(commerceOrderItem.getPartNumber());
			if(apruveOrderItemObj.getVariant() != null && !"".equals(apruveOrderItemObj.getVariant()) && !apruveOrderItemObj.getVariant().equals(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId)))
				updateOrderItemObject.setVariant_info(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId));
			if(apruveOrderItemObj.getVendor() != null && !"".equals(apruveOrderItemObj.getVendor()) && !apruveOrderItemObj.getVendor().equals(abCatalogEntry.getManufacturerName()))
				updateOrderItemObject.setVendor(abCatalogEntry.getManufacturerName());
			if(apruveOrderItemObj.getProductURL() != null && !"".equals(apruveOrderItemObj.getProductURL()))
				updateOrderItemObject.setProduct_url(apruveOrderItemObj.getProductURL());*/
					
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.info(" updateOtherPaymentsOrderItemObject " + updateOrderItemObject.getJSONRequestObj().toString() );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return updateOrderItemObject;
		
	}
	
	@Override
	public  ZCApruveCreateOrderItemRequestObject getApruveCreateAdjustmentsOrderItemTransactionObject(
			PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount) throws  ECException 
	{
				
		String METHOD_NAME = "getApruveCreateAdjustmentsOrderItemTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		String merchant_order_id = tran.getPayment().getPaymentInstruction().getOrderId();
		Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
		ZCApruveCreateOrderItemRequestObject updateOrderItemObject = new ZCApruveCreateOrderItemRequestObject();
		
		try
		{
			OrderDataBean odb = new OrderDataBean();
			odb.setInitKey_orderId(merchant_order_id);
			odb.refreshCopyHelper();
			
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			ZCApruveProcessorHelper helper = new ZCApruveProcessorHelper();
			ExtendedData extData = tran.getPayment().getPaymentInstruction().getExtendedData();
			langId = (int) (Double.parseDouble(extData.getString("langId")));
			
			String orderId = tran.getPayment().getPaymentInstruction().getOrderId();
			String apruveId = extData.getString("apruveId");
			String createOrderItemURLStart = apruveUtility.getConfProperty("APRUVE_ORDER_CREATE_ORDER_ITEMS_URL_START", storeId);
			String createOrderItemURLEnd = apruveUtility.getConfProperty("APRUVE_ORDER_CREATE_ORDER_ITEMS_URL_END", storeId);
			String url = createOrderItemURLStart+apruveId+createOrderItemURLEnd;
			
			LOGGER.info(" createAdjustmentsOrderItemUrl is : " + url );
			
			updateOrderItemObject.setEndPoint(url);
			
			if(odb.getTotalAdjustmentInEJBType() != null && !"".equals(odb.getTotalAdjustmentInEJBType()))
				updateOrderItemObject.setPrice_ea_cents((odb.getTotalAdjustmentInEJBType().multiply(new BigDecimal(100))).intValue());
			updateOrderItemObject.setQuantity(1);
			if(odb.getTotalAdjustmentInEJBType() != null && !"".equals(odb.getTotalAdjustmentInEJBType()))
				updateOrderItemObject.setPrice_total_cents((odb.getTotalAdjustmentInEJBType().multiply(new BigDecimal(100))).intValue());
			/*if(commerceOrderItem.getCurrency() != null && !"".equals(commerceOrderItem.getCurrency()))
				updateOrderItemObject.setCurrency(commerceOrderItem.getCurrency());*/
			updateOrderItemObject.setTitle(helper.getApruveDetailValue(ZCApruveConstants.ORDER_AND_SHIPPING_DISCOUNTS,storeId, langId));
			/*if(abCatalogEntryDescription.getShortDescription() != null && !"".equals(abCatalogEntryDescription.getShortDescription()))
				updateOrderItemObject.setDescription(abCatalogEntryDescription.getShortDescription());
			if(commerceOrderItem.getPartNumber() != null && !"".equals(commerceOrderItem.getPartNumber()))
				updateOrderItemObject.setSku(commerceOrderItem.getPartNumber());
			if(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId) != null && !"".equals(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId)))
				updateOrderItemObject.setVariant_info(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId));
			if(abCatalogEntry.getManufacturerName() != null && !"".equals(abCatalogEntry.getManufacturerName()))
				updateOrderItemObject.setVendor(abCatalogEntry.getManufacturerName());*/
			
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.info(" createAdjustmentsOrderItem request object is : " + updateOrderItemObject.getJSONRequestObj().toString() );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return updateOrderItemObject;
		
	}
	
	@Override
	public  ZCApruveCreateOrderItemRequestObject getApruveCreateOtherPaymentsOrderItemTransactionObject(
			PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount) throws  ECException 
	{
				
		String METHOD_NAME = "getApruveCreateOtherPaymentsOrderItemTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		String merchant_order_id = tran.getPayment().getPaymentInstruction().getOrderId();
		Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
		ZCApruveCreateOrderItemRequestObject updateOrderItemObject = new ZCApruveCreateOrderItemRequestObject();
		
		try
		{
			OrderDataBean odb = new OrderDataBean();
			odb.setInitKey_orderId(merchant_order_id);
			odb.refreshCopyHelper();
			
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			ZCApruveProcessorHelper helper = new ZCApruveProcessorHelper();
			ExtendedData extData = tran.getPayment().getPaymentInstruction().getExtendedData();
			langId = (int) (Double.parseDouble(extData.getString("langId")));
			
			String orderId = tran.getPayment().getPaymentInstruction().getOrderId();
			String apruveId = extData.getString("apruveId");
			String createOrderItemURLStart = apruveUtility.getConfProperty("APRUVE_ORDER_CREATE_ORDER_ITEMS_URL_START", storeId);
			String createOrderItemURLEnd = apruveUtility.getConfProperty("APRUVE_ORDER_CREATE_ORDER_ITEMS_URL_END", storeId);
			String url = createOrderItemURLStart+apruveId+createOrderItemURLEnd;
			
			LOGGER.info(" createOtherPaymentsOrderItemUrl is : " + url );
			
			updateOrderItemObject.setEndPoint(url);
			
			if(odb.getTotalProductPriceInEJBType() != null && tran.getPayment().getPaymentInstruction().getAmount() != null)
				updateOrderItemObject.setPrice_ea_cents(((ZCApruveUtility.getInstance(storeId).getTotalAmount(odb).subtract(tran.getPayment().getPaymentInstruction().getAmount())).multiply(new BigDecimal(-100))).intValue());
			updateOrderItemObject.setQuantity(1);
			if(odb.getTotalProductPriceInEJBType() != null && tran.getPayment().getPaymentInstruction().getAmount() != null)
				updateOrderItemObject.setPrice_total_cents(((ZCApruveUtility.getInstance(storeId).getTotalAmount(odb).subtract(tran.getPayment().getPaymentInstruction().getAmount())).multiply(new BigDecimal(-100))).intValue());
			/*if(commerceOrderItem.getCurrency() != null && !"".equals(commerceOrderItem.getCurrency()))
				updateOrderItemObject.setCurrency(commerceOrderItem.getCurrency());*/
			if(helper.getApruveDetailValue(ZCApruveConstants.OTHER_PAYMENTS,storeId, langId) != null)
				updateOrderItemObject.setTitle(helper.getApruveDetailValue(ZCApruveConstants.OTHER_PAYMENTS,storeId, langId));
			if(helper.getApruveDetailValue(ZCApruveConstants.OTHER_PAYMENTS,storeId, langId) != null)
				updateOrderItemObject.setSku(helper.getApruveDetailValue(ZCApruveConstants.OTHER_PAYMENTS,storeId, langId));
			/*if(abCatalogEntryDescription.getShortDescription() != null && !"".equals(abCatalogEntryDescription.getShortDescription()))
				updateOrderItemObject.setDescription(abCatalogEntryDescription.getShortDescription());
			if(commerceOrderItem.getPartNumber() != null && !"".equals(commerceOrderItem.getPartNumber()))
				updateOrderItemObject.setSku(commerceOrderItem.getPartNumber());
			if(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId) != null && !"".equals(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId)))
				updateOrderItemObject.setVariant_info(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId));
			if(abCatalogEntry.getManufacturerName() != null && !"".equals(abCatalogEntry.getManufacturerName()))
				updateOrderItemObject.setVendor(abCatalogEntry.getManufacturerName());*/
			
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.info(" createOtherPaymentsOrderItemObject " + updateOrderItemObject.getJSONRequestObj().toString() );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return updateOrderItemObject;
		
	}
	
	@Override
	public  ZCApruveOrderItemDeleteRequestObject getApruveDeleteOrderItemTransactionObject(
			PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount,ZCApruveOrderItemsObject apruveOrderItemObj) throws  ECException 
	{
		String METHOD_NAME = "getApruveDeleteOrderItemTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		ZCApruveOrderItemDeleteRequestObject deleteOrderItemObject = new ZCApruveOrderItemDeleteRequestObject();
		try
		{
			Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
			
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			String deleteOrderItemURL = apruveUtility.getConfProperty("APRUVE_DELETE_ORDER_ITEMS_URL", storeId);
			String url = deleteOrderItemURL+apruveOrderItemObj.getOrderItemId();
			LOGGER.info(" DeleteOrderItemUrl is : " + url );
			
			deleteOrderItemObject.setEndPoint(url);
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}

		//LOGGER.info(" deleteOrderOItemRequestbject " + deleteOrderItemObject.getJSONRequestObj().toString() );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return deleteOrderItemObject;
		
	}
	
	@Override
	public  ZCApruveCreateOrderItemRequestObject getApruveCreateOrderItemTransactionObject(
			PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount,OrderItemAccessBean commerceOrderItem) throws  ECException 
	{
				
		String METHOD_NAME = "getApruveCreateOrderItemTransactionObject";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		
		Integer storeId = new Integer(tran.getPayment().getPaymentInstruction().getStore());
		ZCApruveCreateOrderItemRequestObject updateOrderItemObject = new ZCApruveCreateOrderItemRequestObject();
		
		try
		{
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
			ExtendedData extData = tran.getPayment().getPaymentInstruction().getExtendedData();
			langId = (int) (Double.parseDouble(extData.getString("langId")));
			CatalogEntryAccessBean abCatalogEntry = commerceOrderItem.getCatalogEntry();
			CatalogEntryDescriptionAccessBean abCatalogEntryDescription = abCatalogEntry.getDescription(langId, storeId);
			
			String orderId = tran.getPayment().getPaymentInstruction().getOrderId();
			String apruveId = extData.getString("apruveId");
			String createOrderItemURLStart = apruveUtility.getConfProperty("APRUVE_ORDER_CREATE_ORDER_ITEMS_URL_START", storeId);
			String createOrderItemURLEnd = apruveUtility.getConfProperty("APRUVE_ORDER_CREATE_ORDER_ITEMS_URL_END", storeId);
			String url = createOrderItemURLStart+apruveId+createOrderItemURLEnd;
			
			LOGGER.info(" createOrderItemUrl is : " + url );
			
			updateOrderItemObject.setEndPoint(url);
			
			if(commerceOrderItem.getPrice() != null && !"".equals(commerceOrderItem.getPrice()))
				updateOrderItemObject.setPrice_ea_cents((int) (Double.parseDouble(commerceOrderItem.getPrice())*100));
			if(commerceOrderItem.getQuantity() != null && !"".equals(commerceOrderItem.getQuantity()))
				updateOrderItemObject.setQuantity((int) Double.parseDouble(commerceOrderItem.getQuantity()));
			if(commerceOrderItem.getTotalProduct() != null && !"".equals(commerceOrderItem.getTotalProduct()))
				updateOrderItemObject.setPrice_total_cents((int) (Double.parseDouble(commerceOrderItem.getTotalProduct())*100));
			if(commerceOrderItem.getCurrency() != null && !"".equals(commerceOrderItem.getCurrency()))
				updateOrderItemObject.setCurrency(commerceOrderItem.getCurrency());
			if(abCatalogEntryDescription.getName() != null && !"".equals(abCatalogEntryDescription.getName()))
				updateOrderItemObject.setTitle(abCatalogEntryDescription.getName());
			if(abCatalogEntryDescription.getShortDescription() != null && !"".equals(abCatalogEntryDescription.getShortDescription()))
				updateOrderItemObject.setDescription(abCatalogEntryDescription.getShortDescription());
			if(commerceOrderItem.getPartNumber() != null && !"".equals(commerceOrderItem.getPartNumber()))
				updateOrderItemObject.setSku(commerceOrderItem.getPartNumber());
			if(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId) != null && !"".equals(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId)))
				updateOrderItemObject.setVariant_info(apruveUtility.getVariant(commerceOrderItem.getCatalogEntryId(), langId));
			if(abCatalogEntry.getManufacturerName() != null && !"".equals(abCatalogEntry.getManufacturerName()))
				updateOrderItemObject.setVendor(abCatalogEntry.getManufacturerName());
			
			
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECSystemException(ECMessage._ERR_GENERIC,CLASS_NAME,METHOD_NAME, e);
		}
		LOGGER.info(" createOrderItem " + updateOrderItemObject.getJSONRequestObj().toString() );
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return updateOrderItemObject;
		
	}

	
}
