package com.apruve.payment.plugin;

/**
 * Home interface for Enterprise Bean: ZApruvePaymentPlugin
 */
public interface ZApruvePaymentPluginHome extends javax.ejb.EJBHome {

	/**
	 * Creates a default instance of Session Bean: ZApruvePaymentPlugin
	 */
	public com.apruve.payment.plugin.ZApruvePaymentPlugin create()
		throws javax.ejb.CreateException,
		java.rmi.RemoteException;
}
