package com.zobristinc.commerce.integration.apruve.commands;

import java.io.BufferedReader;
import java.io.File;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.json.JSONObject;

import com.ibm.commerce.command.CommandFactory;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.foundation.logging.LoggingHelper;
import com.ibm.commerce.order.commands.AdminOrderCancelCmd;
import com.ibm.commerce.ordermanagement.objects.OrderBlockAccessBean;
import com.ibm.commerce.ordermanagement.util.OrderBlockManager;
import com.ibm.commerce.ordermanagement.util.OrderBlockManagerInterface;
import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.webcontroller.HttpControllerRequestObject;
import com.zobristinc.commerce.payments.apruve.builder.ZCApruveRequestBuilder;
import com.zobristinc.commerce.payments.apruve.processor.ZCApruveProcessor;
import com.zobristinc.commerce.payments.apruve.processor.helper.ZCApruveProcessorHelper;
import com.zobristinc.commerce.payments.apruve.util.ZCApruveUtility;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveRetrieveOrderRequestObject;
import com.zobristinc.commerce.payments.apruve.valueobjects.ZCApruveRetrieveOrderResponseObject;

public class ZCApruveProcessWebhookResponseCmdImpl extends ControllerCommandImpl implements ZCApruveProcessWebhookResponseCmd {
	
	private static final String CLASS_NAME = "ZCReturnFromApruveCmdImpl";
	final Logger LOGGER = LoggingHelper.getLogger(ZCReturnFromApruveCmdImpl.class);
	
	public ZCApruveProcessWebhookResponseCmdImpl() {
		super();
	}
	public void performExecute() throws ECException {
		
		final String METHOD_NAME = "performExecute";
		LOGGER.entering( CLASS_NAME, METHOD_NAME);
		
		String apruveId;
		String orderId = "";
		String storeId="";
		String webHookResponse = "";
		
		TypedProperty reqProp = getRequestProperties();
		storeId = reqProp.getString("storeId");
		
		JSONObject responseObj = new JSONObject();
		
		try
		{
			
			HttpServletRequest request = ((HttpControllerRequestObject)commandContext.getRequest()).getHttpRequest();
			String signature = request.getHeader("X-Apruve-Signature");
			LOGGER.info("Info signature is : "+signature); 
			
			BufferedReader bufferedReader = request.getReader();
			    
			String sCurrentLine;
				      
			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				
			   	 webHookResponse = webHookResponse + sCurrentLine;
			   	 
			}
			
			LOGGER.info("webHook request is : "+webHookResponse);
			
			//verifying signature
			if(verifySignature(signature,webHookResponse,storeId))
			{	
				ZCApruveUtility utility = ZCApruveUtility.getInstance(Integer.valueOf(storeId));
				
				super.performExecute();
				
				JSONObject webhookJSONObj = new JSONObject(webHookResponse);
				
				if(webhookJSONObj.getString("event").equals("payment_term.accepted") || webhookJSONObj.getString("event").equals("invoice.closed"))
				{
					
					//release block
					System.out.println("url in response is : "+webhookJSONObj.getJSONObject("entity").getJSONObject("links").getString("order"));
					String url = webhookJSONObj.getJSONObject("entity").getJSONObject("links").getString("order");
					ZCApruveRetrieveOrderResponseObject response  = getOrderData(url, storeId);
					//System.out.println("apruveOrderId is : "+response.getValue("id")+" merchantOrderId is : "+response.getValue("merchant_order_id")+" order status is : "+response.getValue("status"));
					orderId = response.getValue("merchant_order_id");
					
					
						Integer orderBlockReasonCode=null;
						
						OrderBlockAccessBean ordBlockAB=new OrderBlockAccessBean();
						Enumeration ordBlkEnum=ordBlockAB.findByOrdersId(Long.valueOf(orderId));
						//get order block reasonCode
						
						while(ordBlkEnum.hasMoreElements())
						{
							ordBlockAB=(OrderBlockAccessBean) ordBlkEnum.nextElement();
							orderBlockReasonCode=ordBlockAB.getBlkRsnCodeId();
						}
						
						Object localObject2 = OrderBlockManager.getInstance();
						((OrderBlockManagerInterface)localObject2).unBlock(new Long(orderId), orderBlockReasonCode, "DecisionManager has accepted the order", getCommandContext());
					
				}
				else if(webhookJSONObj.getString("event").equals("order.canceled"))
				{
					System.out.println("url in response is : "+webhookJSONObj.getJSONObject("entity").getJSONObject("links").getString("self"));
					String url = webhookJSONObj.getJSONObject("entity").getJSONObject("links").getString("self");
					ZCApruveRetrieveOrderResponseObject response  = getOrderData(url, storeId);
					System.out.println("apruveOrderId is : "+response.getValue("id")+" merchantOrderId is : "+response.getValue("merchant_order_id")+" order status is : "+response.getValue("status"));
					orderId = response.getValue("merchant_order_id");
					if(response.getValue("status").equals("canceled"))
					{
						AdminOrderCancelCmd localAdminOrderCancelCmd = (AdminOrderCancelCmd)CommandFactory.createCommand("com.ibm.commerce.order.commands.AdminOrderCancelCmd", Integer.valueOf(storeId));
						localAdminOrderCancelCmd.setAccCheck(false);
						getRequestProperties().put("orderId", orderId);
				        getRequestProperties().put("URL", "Dummy");
				        getRequestProperties().put("forcedCancel", true);
				        localAdminOrderCancelCmd.setRequestProperties(getRequestProperties());
				        localAdminOrderCancelCmd.setCommandContext(getCommandContext());
				        localAdminOrderCancelCmd.execute();
					}
				}
			}
			else
			{
				LOGGER.info("signature is invalid.");
				throw new ECApplicationException(ECMessage._ERR_CMD_BAD_EXEC_CMD, CLASS_NAME, METHOD_NAME,"signature is invalid.");
			}
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECApplicationException(ECMessage._ERR_CMD_BAD_EXEC_CMD, CLASS_NAME, METHOD_NAME,e.getMessage());
		}
		LOGGER.exiting( CLASS_NAME, METHOD_NAME);
	}
	
	public ZCApruveRetrieveOrderResponseObject getOrderData(String url,String storeId) throws ECApplicationException
	{
		final String METHOD_NAME = "getOrderData";
		LOGGER.entering( CLASS_NAME, METHOD_NAME);
		ZCApruveRetrieveOrderResponseObject response = null;
		try
		{
			ZCApruveRequestBuilder builder = ZCApruveProcessorHelper.getInstance(Integer.valueOf(storeId)).getApruveRequestBuilder(Integer.valueOf(storeId));
			ZCApruveProcessor service = ZCApruveProcessorHelper.getInstance(Integer.valueOf(storeId)).getApruveProcessor(Integer.valueOf(storeId));
			ZCApruveRetrieveOrderRequestObject retrieveOrderReq = builder.getApruveRetrieveOrderTransactionObject(url);
			response  = service.doRetrieveOrder(retrieveOrderReq);
		}
		catch(Exception e)
		{
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECApplicationException(ECMessage._ERR_CMD_BAD_EXEC_CMD, CLASS_NAME, METHOD_NAME,e.getMessage());
		}
		LOGGER.exiting( CLASS_NAME, METHOD_NAME);
		return response;
	}
	
	public boolean verifySignature(String responseSignature,String data,String storeId) throws ECApplicationException
	{
		final String METHOD_NAME = "verifySignature";
		LOGGER.entering( CLASS_NAME, METHOD_NAME);
		boolean signatureVerified = false;
		try
		{
			ZCApruveUtility apruveUtility = ZCApruveUtility.getInstance(Integer.valueOf(storeId));
			FileUtils fileUtil = new FileUtils();
			File f = new File(apruveUtility.getConfProperty("APRUVE_PUBLIC_KEY_FILE_PATH", Integer.valueOf(storeId)));
			String contents = fileUtil.readFileToString(f);
			contents = contents.replace("-----BEGIN PUBLIC KEY-----", "");
			contents = contents.replace("-----END PUBLIC KEY-----", "");
			byte[] prepared = Base64.decodeBase64(contents.getBytes());
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(prepared);
			KeyFactory kf = KeyFactory.getInstance("RSA"); // Assuming this is an RSA key
			RSAPublicKey rsaPubKey = (RSAPublicKey) kf.generatePublic(publicKeySpec);
			System.out.println("rsa public key is : "+rsaPubKey);
			Signature sig = Signature.getInstance("SHA256WithRSA");
			sig.initVerify(rsaPubKey);
			sig.update(data.getBytes());
			signatureVerified = sig.verify(Base64.decodeBase64(responseSignature.getBytes()));
		}
		catch(Exception e){
			LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Encountered exception ["+e.toString()+"]", e);
			throw new ECApplicationException(ECMessage._ERR_CMD_BAD_EXEC_CMD, CLASS_NAME, METHOD_NAME,e.getMessage());
		}
		LOGGER.entering( CLASS_NAME, METHOD_NAME);
		return signatureVerified;
	}
}
