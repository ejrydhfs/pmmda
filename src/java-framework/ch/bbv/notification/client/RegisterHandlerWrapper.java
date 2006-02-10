/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.client;

import ch.bbv.notification.communication.ClientProfile;

/**
 * Interface specification for the j2ee and direct RegisterHandlerWrapper
 * @author Bruno Persi
 */
public interface RegisterHandlerWrapper {

	/**
	 * register the <code>ClientProfile</code> given by argument on the notification service
	 * @param profile the <code>ClientProfile</code>
	 */
	public abstract void register(ClientProfile profile);

	/**
	 * deregister the <code>ClientProfile</code> given by argument on the notification service
	 * @param profile the <code>ClientProfile</code>
	 */
	public abstract void deRegister(ClientProfile profile);

}
