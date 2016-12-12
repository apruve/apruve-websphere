package com.zobristinc.commerce.integration.apruve.commands;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import com.ibm.commerce.command.CommandContext;
import com.ibm.commerce.command.CommandFactory;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.contract.beansrc.BusinessPolicyCache;
import com.ibm.commerce.contract.objects.BusinessPolicyAccessBean;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.edp.api.EDPPaymentInstruction;
import com.ibm.commerce.edp.commands.PIAddCmd;
import com.ibm.commerce.edp.commands.PIRemoveCmd;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ECSystemException;
import com.ibm.commerce.foundation.logging.LoggingHelper;
import com.ibm.commerce.fulfillment.commands.InventoryManagementHelper;
import com.ibm.commerce.order.beans.OrderDataBean;
import com.ibm.commerce.order.commands.OrderPrepareCmd;
import com.ibm.commerce.order.commands.OrderProcessCmd;
import com.ibm.commerce.order.objects.OrderAccessBean;
import com.ibm.commerce.payment.commands.CheckPaymentPolicyDisplayCmd;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.server.ECConstants;
import com.ibm.commerce.user.objects.AddressAccessBean;
import com.ibm.commerce.webcontroller.HttpControllerRequestObject;
import com.ibm.misc.BASE64Encoder;
import com.zobristinc.commerce.payments.apruve.util.ZCApruveUtility;

public class ZCReturnFromApruveCmdImpl extends ControllerCommandImpl implements ZCReturnFromApruveCmd {
	private static final String CLASS_NAME = "ZCReturnFromApruveCmdImpl";
	final Logger LOGGER = LoggingHelper.getLogger(ZCReturnFromApruveCmdImpl.class);
	OrderAccessBean abOrder = null;
	private ResourceBundle storeConfigBundle;
	private String selectedPaymentType = null;
	boolean isFundingFailureHandleEnabled = false;
	
	public ZCReturnFromApruveCmdImpl() {
		super();
	}
	public void performExecute() throws ECException {
		final String METHOD_NAME = "performExecute";
		
		LOGGER.entering( CLASS_NAME, METHOD_NAME);
		String apruveId;
		TypedProperty reqProp = getRequestProperties();
		
		try
		{
			ZCApruveUtility utility = ZCApruveUtility.getInstance(getStoreId());
			super.performExecute();
			apruveId = reqProp.getString("apruveId", null);
			String url = reqProp.getString("URL", "OrderShippingBillingView");
			String orderId = reqProp.getString("orderId");
			abOrder = new OrderAccessBean();
			abOrder.setInitKey_orderId(orderId);
			abOrder.refreshCopyHelper();
			String errorViewName = reqProp.getString("errorViewName", "OrderShippingBillingView");
			TypedProperty responseProp = new TypedProperty();
			if ("true".equals(utility.getConfProperty("addPaymentOnReturn", getStoreId())))
				addOrUpdatePaymentInformation(apruveId);
				
				responseProp.put(ECConstants.EC_VIEWTASKNAME, ECConstants.EC_GENERIC_REDIRECTVIEW );
				responseProp.put("errorViewName", errorViewName);
				responseProp.put("orderId", orderId);
				responseProp.put("storeId", getCommandContext().getStoreId());
				
				
				OrderPrepareCmd orderPrepare = (OrderPrepareCmd)CommandFactory.createCommand("com.ibm.commerce.order.commands.OrderPrepareCmd", getStoreId());
				orderPrepare.setCommandContext(getCommandContext());
				orderPrepare.setRequestProperties(getRequestProperties());
				orderPrepare.execute();
				String flage = 	 utility.getConfProperty("FINALIZE_ON_CREATE", getStoreId());
				if ("true".equals(utility.getConfProperty("FINALIZE_ON_CREATE", getStoreId())))
				{
					TypedProperty orderProcessProp = new TypedProperty();
                    orderProcessProp.put("storeId", getCommandContext().getStoreId());
                    orderProcessProp.put("catalogId", getRequestProperties().getString("catalogId", null));
                    orderProcessProp.put("langId", getCommandContext().getLanguageId());
                    orderProcessProp.put("orderId", orderId);
                    OrderProcessCmd orderProcess = (OrderProcessCmd) CommandFactory.createCommand(
                                         "com.ibm.commerce.order.commands.OrderProcessCmd", getStoreId());
                    orderProcess.setCommandContext(getCommandContext());
                    orderProcess.setRequestProperties(orderProcessProp);
                    orderProcess.execute();
                    responseProp.put("langId", getCommandContext().getLanguageId());
                    responseProp.put("catalogId", getRequestProperties().getString("catalogId"));
                   responseProp.put(ECConstants.EC_URL, "OrderShippingBillingConfirmationView");
				}
				else{
					responseProp.put(ECConstants.EC_URL, url);
					
				}
				LOGGER.log(Level.FINEST,"responseProp: " + responseProp);
		        setResponseProperties(responseProp);
		        InventoryManagementHelper.flush();
			}
			catch(Exception e )
			{
				LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
				throw new ECApplicationException(ECMessage._ERR_CMD_BAD_EXEC_CMD, CLASS_NAME, METHOD_NAME,e.getMessage());
			}
			
			LOGGER.exiting( CLASS_NAME, METHOD_NAME);
			
		}
	public BigDecimal getGiftCardAmount(OrderAccessBean abOrder, CommandContext commandContext,Integer storeId) throws Exception
	{
		final String METHOD_NAME = "getGiftCardAmount";
		
		LOGGER.entering( CLASS_NAME, METHOD_NAME);
		OrderDataBean orderBean = new OrderDataBean(abOrder);
   		orderBean.setCommandContext(commandContext);
   		orderBean.populate();
   		TypedProperty gcProp = new TypedProperty();
   		String giftPayMethod = ZCApruveUtility.getInstance(getStoreId()).getGiftCardPaymethod(getStoreId());
   		//String giftPayMethod = "COD";
   		
	    BigDecimal giftCardAmount = new BigDecimal(0);  
		OrderDataBean.PaymentInstruction[] paymentInstructions = orderBean.getPaymentInstructions();
   		for (int i = 0; i < paymentInstructions.length; i++) {
   			EDPPaymentInstruction instruction = paymentInstructions[i].getPaymentInstruction();
   		    // get the gift card payment
   			if (instruction.getPaymentMethod().equals(giftPayMethod)) {
   				giftCardAmount = giftCardAmount.add(instruction.getAmount());
   			}
   		}
   		LOGGER.exiting( CLASS_NAME, METHOD_NAME);
		return giftCardAmount;
	} 
	public void addOrUpdatePaymentInformation(String apruveId) throws Exception
	{
		final String METHOD_NAME = "addOrUpdatePaymentInformation";
		LOGGER.entering( CLASS_NAME, METHOD_NAME);
		ZCApruveUtility utility = ZCApruveUtility.getInstance(getStoreId());
		removeNonGiftcardPayments(abOrder,getCommandContext(),getStoreId(),null);
		String policyId = null;
		/*try
		{
			policyId = utility.getConfProperty("PayPalPolicyId", getStoreId());
		}
		catch (Exception e)
		{
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
		} */
		String payMethodId = null;
		payMethodId = utility.getConfProperty("ApruvePayMethodId", getStoreId());
		policyId = getPolicyIdByPaymentMethod(payMethodId);
		//String payMethodId = utility.getConfProperty("PayPalPayMethodId", getStoreId());
		TypedProperty reqProp = new TypedProperty(); 
		reqProp.put("storeId", getCommandContext().getStoreId());
		reqProp.put("langId", getCommandContext().getLanguageId());
		reqProp.put("orderId", abOrder.getOrderId());
		reqProp.put("catalogId", getRequestProperties().getString("catalogId"));
		reqProp.put("policyId", policyId);
		reqProp.put("payMethodId", payMethodId);
		reqProp.put("apruveId", apruveId);
		//reqProp.put("billing_address_id", getRequestProperties().getString("apruve_billing_address_id"));
		//System.out.println("billing addressId in ZCReturnFromApruve is : "+getRequestProperties().getString("apruve_billing_address_id"));
 		if (abOrder.getAddressId() != null && abOrder.getAddressId().length()>0) {
 			
 			AddressAccessBean abBean = new AddressAccessBean();
 			abBean.setInitKey_AddressId(abOrder.getAddressId());
 			abBean.refreshCopyHelper();
 			if(abBean.getStatus().equals("P"))
 				reqProp.put("billing_address_id", abOrder.getAddressId());
 			System.out.println("addressId in ZCReturnfromApruve is : "+abOrder.getAddressId());
		}
 		reqProp.put("langId", getCommandContext().getLanguageId());
		//reqProp.put("is_profile_support_needed", false );
		
		BigDecimal dOrderTotal = null;
		
			BigDecimal giftCardAmount = getGiftCardAmount(abOrder,getCommandContext(),getStoreId());
			if(giftCardAmount != null && (giftCardAmount.compareTo(BigDecimal.ZERO)>=0) ){
			dOrderTotal = utility.getTotalAmount(abOrder).subtract(giftCardAmount);
			}else{
			dOrderTotal = utility.getTotalAmount(abOrder);
			}
		reqProp.put("piAmount", dOrderTotal.toString());
		reqProp.put("URL", "dummy");
		
		String ipAddress = null;

		
		try {
			if ( commandContext.getRequest() instanceof HttpControllerRequestObject  )
			{
				HttpServletRequest request = ((HttpControllerRequestObject)commandContext.getRequest()).getHttpRequest();
				ipAddress = request.getHeader("True-Client-IP");
				if(null == ipAddress || "".equals(ipAddress) || "null".equals(ipAddress)){
					 ipAddress  = request.getHeader("X-FORWARDED-FOR");  
			        if(ipAddress == null)  
			        {  
			          ipAddress = request.getRemoteAddr();  
			        }
				
			}	
				ipAddress =  getIPEncode(ipAddress);
				reqProp.put("decisionParameter", ipAddress);
				
			}				
			
		}catch (Exception  ex){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+ex.toString()+"]", ex); 
			ipAddress = "";
		}
		
		LOGGER.info("request properties for piAddCmd : "+reqProp);		
		
   		PIAddCmd piAddCmd = (PIAddCmd) CommandFactory.createCommand("com.ibm.commerce.edp.commands.PIAddCmd", getStoreId());
   		piAddCmd.setCommandContext(getCommandContext());
   		piAddCmd.setRequestProperties(reqProp);
   		piAddCmd.execute();
   		LOGGER.exiting( CLASS_NAME, METHOD_NAME);
	}
	protected String getPolicyIdByPaymentMethod(String localPaymentMethod) {
		final String METHOD_NAME = "getPolicyIdFromPaymentMethod";
		LOGGER.entering(CLASS_NAME, METHOD_NAME, new Object[] { localPaymentMethod });
		String localPolicyId = null;
		String defaultPolicyId = null;
		try {
			Enumeration<BusinessPolicyAccessBean> enPolicy = BusinessPolicyCache.findByNameTypeAndStore(localPaymentMethod, "Payment", getStoreId());
			if (enPolicy != null) {
				for (BusinessPolicyAccessBean abPolicy: Collections.list(enPolicy)) {
					defaultPolicyId = abPolicy.getPolicyId();
					if (isDisplayAllowed(abPolicy))
					{
						localPolicyId = abPolicy.getPolicyId();

						if (abPolicy.getStoreEntityId().equals(getStoreId()))
							break;
					}
				}
				if (localPolicyId == null)
				{
					localPolicyId = defaultPolicyId;
				}
			}
		} catch (Exception e) {
			LOGGER.logp(Level.FINER, CLASS_NAME, METHOD_NAME, "Catch exception when getPolicyIdByPaymentMethod(): {0}", new Object[] {e.getMessage()});
		} finally {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		if ((localPolicyId == null) || (localPolicyId.length() == 0)) {
			return null;
		}
		return localPolicyId;
	}
	protected boolean isDisplayAllowed(BusinessPolicyAccessBean abPolicy) throws RemoteException, CreateException, FinderException, NamingException {
		final String METHOD_NAME = "isDisplayAllowed";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		boolean isAllowed = true;
		try
		{
			CheckPaymentPolicyDisplayCmd cmd = (CheckPaymentPolicyDisplayCmd)CommandFactory.createCommand(
					"com.ibm.commerce.payment.commands.CheckPaymentPolicyDisplayCmd", getStoreId());
			cmd.setCommandContext(getCommandContext());
			cmd.execute();
			isAllowed = cmd.isDisplayAllowed(abPolicy);
		} catch (ECException e) {
			LOGGER.logp(Level.FINER, CLASS_NAME, METHOD_NAME, "Policy check fails due to exception: {0}", new Object[] {e.toString()});
			isAllowed = false;
		}

		LOGGER.exiting(CLASS_NAME, METHOD_NAME, isAllowed);
		return isAllowed;
	}
	
	public  String getIPEncode(String ip){
        BASE64Encoder base64encoder = new BASE64Encoder();
        return base64encoder.encode(ip.getBytes());
    }
	public  void removeNonGiftcardPayments(OrderAccessBean abOrder, CommandContext commandContext,Integer storeId, Set excludePids) throws Exception
	{
		final String METHOD_NAME = "removeNonGiftcardPayments";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);

		OrderDataBean orderBean = new OrderDataBean(abOrder);
   		orderBean.setCommandContext(commandContext);
   		orderBean.populate();
   		TypedProperty gcProp = new TypedProperty();
		OrderDataBean.PaymentInstruction[] paymentInstructions = orderBean.getPaymentInstructions();
		ZCApruveUtility utility = ZCApruveUtility.getInstance(getStoreId());
   		for (int i = 0; i < paymentInstructions.length; i++) {
   			EDPPaymentInstruction instruction = paymentInstructions[i].getPaymentInstruction();

   			// get the gift card payment
   			if (! instruction.getPaymentMethod().equals(utility.getGiftCardPaymethod(storeId))) {
   		//	if (! instruction.getPaymentMethod().equals("COD")) {		
   				if ( excludePids != null &&  excludePids.contains(instruction.getId()))
   					continue;
   				
   				PIRemoveCmd piRemoveCmd = (PIRemoveCmd) CommandFactory.createCommand("com.ibm.commerce.edp.commands.PIRemoveCmd", storeId);
   				piRemoveCmd.setRequestProperties(gcProp);   				
   				piRemoveCmd.setPIIDs(new Long[]{instruction.getId()});
   				piRemoveCmd.setURL("dummy");
   				
   				
   				piRemoveCmd.setCommandContext(commandContext);
   				piRemoveCmd.setOrderId(orderBean.getOrderIdInEJBType());
   				
   				
   				piRemoveCmd.execute();
   			}
   		}
   		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	}
}
