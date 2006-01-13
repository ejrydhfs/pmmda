package ch.bbv.notification.server.registration;

import javax.ejb.EJBHome;

public interface RegisterHandlerHome extends EJBHome {

	public RegisterHandler create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
