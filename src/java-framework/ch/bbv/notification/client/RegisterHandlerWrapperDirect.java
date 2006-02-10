/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.client;

import ch.bbv.notification.communication.ClientProfile;

/**
 * RegisterHandlerWrapper implementation for case direct
 * @see ch.bbv.notification.client.RegisterHandlerWrapper
 * @author Bruno Persi
 *
 */
public class RegisterHandlerWrapperDirect extends ch.bbv.application.ComponentImpl implements RegisterHandlerWrapper {

	public RegisterHandlerWrapperDirect(){
		super("RegisterHandlerWrapper");
	}
		
	public void register(ClientProfile profile) {
		System.out.println("not implemented yet");

	}

	public void deRegister(ClientProfile profile) {
		System.out.println("not implemented yet");
	}

}
