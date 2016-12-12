package com.zobristinc.commerce.payments.apruve.builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.commerce.catalog.objects.CatalogEntryAccessBean;
import com.ibm.commerce.catalog.objects.CatalogEntryDescriptionAccessBean;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.order.objects.OrderAccessBean;
import com.ibm.commerce.order.objects.OrderItemAccessBean;
import com.ibm.commerce.payments.plugin.ExtendedData;
import com.ibm.commerce.payments.plugin.FinancialException;
import com.ibm.commerce.payments.plugin.FinancialTransaction;
import com.ibm.commerce.payments.plugin.InvalidDataException;
import com.ibm.commerce.payments.plugin.PluginContext;
import com.ibm.commerce.ras.ECMessage;
import com.zobristinc.commerce.integration.apruve.commands.OrderItemsDetail;
import com.zobristinc.commerce.payments.apruve.processor.helper.ZCApruveProcessorHelper;
import com.zobristinc.commerce.payments.apruve.util.ZCApruveConstants;
import com.zobristinc.commerce.payments.apruve.util.ZCApruveUtility;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveAddItemInterface;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateOrderItemRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveCreateOrderRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderCreateInvoiceRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderFinalizeRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemDeleteRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemUpdateRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderItemsObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveOrderUpdateRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveRetrieveOrderRequestObject;

public abstract class ZCApruveRequestBuilder {
	public final static String CLASS_NAME = ZCApruveRequestBuilder.class.getName();
	private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
	public static final Integer DEFAULT_LANG_ID = new Integer(-1);

	public static String CALL_SOURCE_EXPRESS = "Express";

	public static String CALL_SOURCE_PAYMENT = "Payment";

	
	/*private String paypalBuilderType = null;
	
	public  ZCApruveRequestBuilder(String apruveBuilderType)
	{
		this.setApruveBuilderType(apruveBuilderType);
	}*/
	
	public BigDecimal getTotalAmount(String orderId) throws ECException {
		final String methodName = "getTotalAmount";
		LOGGER.entering(CLASS_NAME, methodName);	
	
        BigDecimal dOrderTotal = null; 

        try {
            OrderAccessBean abOrder = new OrderAccessBean(); 
            abOrder.setInitKey_orderId(orderId);

            dOrderTotal = abOrder.getTotalProductPriceInEJBType(); 
            dOrderTotal = dOrderTotal.add(abOrder.getTotalTaxInEJBType()); 
            dOrderTotal = dOrderTotal.add(abOrder.getTotalShippingChargeInEJBType()); 
            dOrderTotal = dOrderTotal.add(abOrder.getTotalShippingTaxInEJBType()); 
            dOrderTotal = dOrderTotal.add(abOrder.getTotalAdjustmentInEJBType()); 
	    } catch (javax.ejb.CreateException ex) { 
	            throw new ECSystemException(ECMessage._ERR_CREATE_EXCEPTION, CLASS_NAME, methodName, ex); 
	    } catch (javax.ejb.FinderException ex) { 
	            throw new ECSystemException(ECMessage._ERR_FINDER_EXCEPTION, CLASS_NAME, methodName, ex); 
	    } catch (javax.naming.NamingException ex) { 
	            throw new ECSystemException(ECMessage._ERR_NAMING_EXCEPTION, CLASS_NAME, methodName, ex); 
	    } catch (java.rmi.RemoteException ex) { 
	            throw new ECSystemException(ECMessage._ERR_REMOTE_EXCEPTION, CLASS_NAME, methodName, ex); 
	    }
	    LOGGER.exiting(CLASS_NAME, methodName);
	    return dOrderTotal; 
    }


	
	
	public abstract ZCApruveOrderFinalizeRequestObject getApruveFinalizeTransactionObject(PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount) throws ECException ;
	
	public abstract ZCApruveOrderCreateInvoiceRequestObject getApruveCreateInvoiceTransactionObject(PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount) throws ECException ;
	
	public abstract ZCApruveOrderUpdateRequestObject getApruveUpdateOrderTransactionObject(PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount) throws ECException ;
	
	public abstract ZCApruveOrderItemUpdateRequestObject getApruveUpdateOrderItemTransactionObject(PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount,OrderItemAccessBean commerceOrderItem,ZCApruveOrderItemsObject apruveOrderItemObj) throws ECException ;
	
	public abstract ZCApruveOrderItemUpdateRequestObject getApruveUpdateAdjustmentsOrderItemTransactionObject(PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount,ZCApruveOrderItemsObject apruveOrderItemObj) throws ECException ;
	
	public abstract ZCApruveOrderItemUpdateRequestObject getApruveUpdateOtherPaymentsOrderItemTransactionObject(PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount,ZCApruveOrderItemsObject apruveOrderItemObj) throws ECException ;
	
	public abstract ZCApruveCreateOrderItemRequestObject getApruveCreateAdjustmentsOrderItemTransactionObject(PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount) throws ECException ;
	
	public abstract ZCApruveCreateOrderItemRequestObject getApruveCreateOtherPaymentsOrderItemTransactionObject(PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount) throws ECException ;
	
	public abstract ZCApruveOrderItemDeleteRequestObject getApruveDeleteOrderItemTransactionObject(PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount,ZCApruveOrderItemsObject apruveOrderItemObj) throws ECException ;
	
	public abstract ZCApruveCreateOrderItemRequestObject getApruveCreateOrderItemTransactionObject(PluginContext con, FinancialTransaction tran, boolean bool,BigDecimal amount,OrderItemAccessBean commerceOrderItem) throws ECException ;
	
	public abstract ZCApruveCreateOrderRequestObject getApruveCreateOrderTransactionObject(String merchant_order_id,int storeId,Integer langId) throws ECException ;
	
	public abstract ZCApruveRetrieveOrderRequestObject getApruveRetrieveOrderTransactionObject(String url) throws ECException;
	
	//public void getApruveOrderCancelObject();
	
	//public void
	
	protected Boolean getIsFirstCall(ExtendedData data) {
		final String METHOD_NAME = "getIsFirstCall";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		Boolean isFirstCall = data.getBoolean("isFirstCall");
		if (isFirstCall == null) {
			data.setBoolean("isFirstCall", Boolean.TRUE, false);
			isFirstCall = getIsFirstCall(data);
		}
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return isFirstCall;
	}
	
	protected String getAuthType(PluginContext con, FinancialTransaction tran) throws InvalidDataException, FinancialException {
		final String METHOD_NAME = "getAuthType";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);

		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return getAuthType( new Integer(tran.getPayment().getPaymentInstruction().getStore()));
		
	}
	
	protected String getAuthType(Integer storeId) throws InvalidDataException, FinancialException {
		final String METHOD_NAME = "getAuthType";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);

		String authType = null;
		try
		{
			authType = ZCApruveUtility.getInstance(storeId).getConfProperty("ApruveAuthType", storeId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} finally {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return authType;
	}	
	
	protected void addOrderItems(BigDecimal amt, ZCApruveAddItemInterface requestObject, OrderAccessBean abOrder, Integer storeId, Integer langId) throws Exception {
		final String METHOD_NAME = "addOrderItems";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);

		ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(storeId);
		// don't add order items if paypal is being used for partial payment of order
		BigDecimal orderTotal = ZCApruveUtility.getInstance(abOrder.getStoreEntityIdInEJBType()).getTotalAmount(abOrder);
		BigDecimal otherpayments = BigDecimal.ZERO;
		if (amt.compareTo(ZCApruveUtility.getInstance(abOrder.getStoreEntityIdInEJBType()).getTotalAmount(abOrder)) < 0)
		{
			otherpayments = ZCApruveUtility.getInstance(abOrder.getStoreEntityIdInEJBType()).getTotalAmount(abOrder).subtract(amt); 
		}
		else if (amt.compareTo(ZCApruveUtility.getInstance(abOrder.getStoreEntityIdInEJBType()).getTotalAmount(abOrder)) > 0)
		{
			 
			return;
		}
		
		// don't add order items if a discount was applied because the sum of order item amount won't equal order total, a PP requirement
		BigDecimal dShippingDiscount = abOrder.getTotalAdjustmentByCalculationUsageId(new Integer(-7));
		
		BigDecimal dTotalDiscount = abOrder.getTotalAdjustmentInEJBType();
		BigDecimal shippingCharge = abOrder.getTotalShippingChargeInEJBType();
		BigDecimal shippingChargeTax = abOrder.getTotalShippingTaxInEJBType();
		
		BigDecimal dtemDiscount = (dTotalDiscount.subtract(dShippingDiscount));
		BigDecimal totalProductPrice =  abOrder.getTotalProductPriceInEJBType();
		BigDecimal totalTaxes = abOrder.getTotalTaxInEJBType();
		
		// in product price is less than total discounts then do not send item informatoin
		//if ( totalProductPrice.add(dTotalDiscount).compareTo(BigDecimal.ZERO) <= 0 )
			//return;
		
		
		//if ( otherpayments.compareTo(totalProductPrice.add(dTotalDiscount)) >= 0 )
			//return; // do not send item information because total amount is less than paypal item amount
		
		
		OrderItemAccessBean[] orderitems = abOrder.getOrderItems();
		boolean containsItems = false;
		
		for(int i = 0;i < orderitems.length;i++){
			 //oidb = orderitems[i];
			CatalogEntryAccessBean abCatalogEntry = orderitems[i].getCatalogEntry();
			CatalogEntryDescriptionAccessBean abCatalogEntryDescription = abCatalogEntry.getDescription(langId, storeId);
			OrderItemsDetail oid = new OrderItemsDetail();
			if(abCatalogEntryDescription.getName() != null)
				oid.setTitle(abCatalogEntryDescription.getName());
			if(abCatalogEntryDescription.getShortDescription() != null)
				oid.setDescription(abCatalogEntryDescription.getShortDescription());
			if(abCatalogEntry.getManufacturerName() != null)
				oid.setVendor(abCatalogEntry.getManufacturerName());
			if(orderitems[i].getCatalogEntryId() != null && apruveUtility.getVariant(orderitems[i].getCatalogEntryId(), langId) != null)
				oid.setVariant(apruveUtility.getVariant(orderitems[i].getCatalogEntryId(), langId));
			if(orderitems[i].getQuantity() != null)
				oid.setQuantity((int) Double.parseDouble(orderitems[i].getQuantity()));
			if(orderitems[i].getPartNumber() != null)
				oid.setSku(orderitems[i].getPartNumber());
			if(orderitems[i].getTotalProduct() != null)
				oid.setAmount_cents( (int) (Double.parseDouble(orderitems[i].getTotalProduct())*100));
			if(orderitems[i].getPrice() != null)
				oid.setPrice( (int) (Double.parseDouble(orderitems[i].getPrice())*100)); 
			requestObject.addItem(oid);
		}
		ZCApruveProcessorHelper helper = new ZCApruveProcessorHelper();
		//helper.getApruveDetailValue("Shipping_Discount", storeId, langId);
		if(dTotalDiscount.compareTo(BigDecimal.ZERO) != 0)
		{
			OrderItemsDetail oid = new OrderItemsDetail();
			oid.setTitle(helper.getApruveDetailValue(ZCApruveConstants.ORDER_AND_SHIPPING_DISCOUNTS,storeId, langId));
			//oid.setDescription("Order and shipping discounts.");
			oid.setQuantity(1);
			oid.setPrice((dTotalDiscount.multiply(new BigDecimal(100))).intValue());
			oid.setAmount_cents((dTotalDiscount.multiply(new BigDecimal(100))).intValue());
			requestObject.addItem(oid);
		}
		if ( otherpayments.compareTo(BigDecimal.ZERO) != 0 )
		{
			OrderItemsDetail itemObject = new OrderItemsDetail();
			itemObject.setPrice((otherpayments.multiply(new BigDecimal(-100))).intValue());
			itemObject.setTitle(helper.getApruveDetailValue("Other_Payments",storeId, langId));
			itemObject.setSku(helper.getApruveDetailValue("Other_Payments",storeId, langId));
			
			itemObject.setQuantity(new Integer(1));
			itemObject.setAmount_cents((otherpayments.multiply(new BigDecimal(-100))).intValue());
			requestObject.addItem(itemObject);	
			
		}
		BigDecimal itemTotal = BigDecimal.ZERO;
		List<OrderItemsDetail> itemsAdded = requestObject.getItems();
		for (OrderItemsDetail item : itemsAdded)
		{
			double tempValHolder = Double.parseDouble(item.getAmount_cents().toString())/100;
			BigDecimal finalItemValIndollars = new BigDecimal(tempValHolder+"");
			
				itemTotal = itemTotal.add(finalItemValIndollars);
		}
		System.out.println("values before clearing items are : itemTotal value : "+itemTotal+" abOrder.getTotalTaxInEJBType() value : "+abOrder.getTotalTaxInEJBType()+" abOrder.getTotalShippingTaxInEJBType() value : "+abOrder.getTotalShippingTaxInEJBType()+" abOrder.getTotalShippingChargeInEJBType() value : "+abOrder.getTotalShippingChargeInEJBType()+" amt value : "+amt);
		if ( itemTotal.add(abOrder.getTotalTaxInEJBType().add(abOrder.getTotalShippingTaxInEJBType().add(abOrder.getTotalShippingChargeInEJBType()))).compareTo(amt) != 0 )
		{
			// for some reason amounts are not matching then clear items to avoid payment errors
			//requestObject.clearItems();
		}
		if (containsItems) {
			requestObject.setItemDiscount(dtemDiscount);
			requestObject.setItemAmount(abOrder.getTotalProductPriceInEJBType());
			requestObject.setTaxAmount(abOrder.getTotalTaxInEJBType().add(abOrder.getTotalShippingTaxInEJBType()));
			requestObject.setShippingCharge(abOrder.getTotalShippingChargeInEJBType());
			requestObject.setShippingDiscount(dShippingDiscount);
		}
	
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	}
	

	
	
	

/*

	public void setApruveBuilderType(String paypalBuilderType) {
		this.paypalBuilderType = paypalBuilderType;
	}



	public String getApruveBuilderType() {
		return paypalBuilderType;
	}*/
}
