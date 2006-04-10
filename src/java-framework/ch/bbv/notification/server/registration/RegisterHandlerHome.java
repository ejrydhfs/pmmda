/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.server.registration;

import javax.ejb.EJBHome;

public interface RegisterHandlerHome extends EJBHome {

	public RegisterHandler create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
