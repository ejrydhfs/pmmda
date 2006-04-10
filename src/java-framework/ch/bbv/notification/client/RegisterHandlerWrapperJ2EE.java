/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.client;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import ch.bbv.notification.communication.ClientProfile;
import ch.bbv.notification.server.registration.RegisterHandler;
import ch.bbv.notification.server.registration.RegisterHandlerHome;


/**
 * Clientside wrapper class, but why we dont create a clientside serviceLocater for all jndi lookup?
 * @author Bruno Persi
 */
public class RegisterHandlerWrapperJ2EE extends ch.bbv.application.ComponentImpl implements RegisterHandlerWrapper {
	
	private final String REGISTERHANDLER_JNDI_NAME = "ejb/RegisterHandler";
	
	public RegisterHandlerWrapperJ2EE(){
		super("RegisterHandlerWrapper");
	}
	

	public void register(ClientProfile profile){
		assert profile != null;
		try{
			lookupRegisterHandler().register(profile);
		} catch (RemoteException re){
			re.printStackTrace();
			throw new IllegalStateException(re);
		}
	}
	
	private RegisterHandler lookupRegisterHandler() throws RemoteException{
		try{
			InitialContext ic = new InitialContext();
			Object obj = ic.lookup(REGISTERHANDLER_JNDI_NAME);
			RegisterHandlerHome home = (RegisterHandlerHome)PortableRemoteObject.narrow(obj,RegisterHandlerHome.class);
			return home.create();
		} catch (NamingException ne){
			ne.printStackTrace();
			throw new IllegalStateException(ne.getMessage());
		} catch (CreateException cre){
			cre.printStackTrace();
			throw new IllegalStateException(cre.getMessage());
		}
	}
	
	public void deRegister(ClientProfile profile){
		assert profile != null;
		try{
			lookupRegisterHandler().deRegister(profile);
		} catch (RemoteException re){
			re.printStackTrace();
			throw new IllegalStateException(re);
		}
	}

}
