package com.zobristinc.commerce.payments.apruve.util;

import java.math.BigDecimal;
import java.util.logging.Logger;

import com.ibm.commerce.base.helpers.ForUpdateStrategy;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.exception.ExceptionHandler;
import com.ibm.commerce.foundation.common.util.logging.LoggingHelper;
import com.ibm.commerce.ras.ECTrace;
import com.ibm.commerce.ras.ECTraceIdentifiers;

public class ZCCommonUtil {
	final static String CLASS_NAME = ZCCommonUtil.class.getName();
	final static Logger LOGGER = LoggingHelper.getLogger(ZCCommonUtil.class) ;
	public static final int DEFAULT_SCALE = 2;
	public static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_UP;
	private static final ForUpdateStrategy FOR_UPDATE_ORDERS = new ForUpdateStrategy("ORDERS", CLASS_NAME);

	
	public static  String bigDecimalToString(BigDecimal amt) throws ECException {
		
		final String METHOD_NAME = "bigDecimalToString(amt)";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);

		try {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			return bigDecimalToString(amt, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
		} finally {
			ECTrace.exit(ECTraceIdentifiers.COMPONENT_EXTERN, CLASS_NAME, METHOD_NAME);
		}
	}

	public static String bigDecimalToString(BigDecimal amt, int scale, int roundingMode) throws ECException {
		final String METHOD_NAME = "bigDecimalToString(amt, scale, roundingMode)";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		if ( amt == null )
			return "";
		try {
			return amt.setScale(2, BigDecimal.ROUND_UP).toString();
		} catch (Exception e) {
			throw ExceptionHandler.convertToECException(e);
		} finally {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
	}
	
	public static String getIndexedParameterName(String name, String pattern, int value)
	{
		return name.replaceAll(pattern,String.valueOf(value)); 
	}
	public static String getIndexedParameterName2(String name, String pattern, int value, String pattern2, int value2)
	{
		return name.replaceAll(pattern,String.valueOf(value)).replaceAll(pattern2, String.valueOf(value2)); 
	}
	
	public static boolean isNotEmptyString(Object value)
	{
		if ( value == null )
			return false;
		return isNotEmptyString(value.toString());
	}
	
	public static boolean isNotEmptyString(String value)
	{
		return (! isEmptyString(value));
	}
	
	public static boolean isEmptyString(Object value)
	{
		if ( value == null )
			return true;
		return isEmptyString(value.toString());
	}
	
	public static boolean isEmptyString(String value)
	{
		if ( value == null )
			return true;
		if ( value.length() == 0 )
			return true;
		return false;
	}
		
}
