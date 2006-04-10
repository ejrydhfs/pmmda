/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.server.registration;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import ch.bbv.notification.communication.ClientProfile;
import ch.bbv.notification.server.SubjectObserverPool;

/**
 * This SessionBean is the central entering point for register clients
 * @author Bruno Persi
 */
public class RegisterHandlerBean implements RegisterHandlerBusiness{

	public void ejbActivate() throws EJBException, RemoteException {}
	public void ejbPassivate() throws EJBException, RemoteException {}
	public void ejbRemove() throws EJBException, RemoteException {}
	public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {}
	public void ejbCreate() throws EJBException, RemoteException {}
	
	public void register(ClientProfile profile) throws RemoteException {
		SubjectObserverPool pool = SubjectObserverPool.getInstance();
		pool.addProfile(profile);
	}
	
	public void deRegister(ClientProfile profile) throws RemoteException{
		SubjectObserverPool.getInstance().removeProfile(profile);
	}

}
