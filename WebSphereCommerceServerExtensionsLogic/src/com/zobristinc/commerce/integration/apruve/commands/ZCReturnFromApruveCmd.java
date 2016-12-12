package com.zobristinc.commerce.integration.apruve.commands;

import com.ibm.commerce.command.ControllerCommand;

public interface ZCReturnFromApruveCmd extends ControllerCommand{
	public static final String defaultCommandClassName = "com.zobristinc.commerce.integration.apruve.commands.ZCReturnFromApruveCmdImpl";
    public static final String APRUVE_BILLING_ADDRESS_NICKNAME = "apruve_billing_address";
}
