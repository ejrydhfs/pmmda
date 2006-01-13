package ch.bbv.notification.server.registration;

import java.rmi.RemoteException;

import ch.bbv.notification.communication.ClientProfile;

/**
 * Business interface for RegisterHandler SessionBean
 * @author Bruno Persi
 *
 */
public interface RegisterHandlerBusiness extends javax.ejb.SessionBean{

	/**
	 * register the given client and create an Observer
	 * @param profile the ClientInformations as <code>ClientProfile</code>
	 * @throws RemoteException thrown on container/J2EE dependent errors 
	 */
	public void register(ClientProfile profile) throws RemoteException;

	/**
	 * deRegister the given client
	 * @param profile the ClientInformation as <code>ClientProfile</code>
	 * @throws RemoteException thrown on container/J2EE dependent erros
	 */
	public void deRegister(ClientProfile profile) throws RemoteException;

}