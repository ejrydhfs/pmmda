/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.services;

import java.util.List;

/**
 * The service defines the interface for a single invocable service. Each 
 * service can be a composition of more primitive ones.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface Service {
	/** Returns the name of the service.
	 * @return name of the service
	 */
	String getName();
	
	/** Starts the service. This method is only called once during the lifecycle
	 * of the application.
	 */
	void start();
	
	/** Preprocess a service request.
	 * @param context context of the service request.
	 */
	void preprocess(ServiceContext context);

	/** Process a service request. The processing is part of a unit of work containing
	 * the complete chain of operations.
	 * @param context context of the service request.
	 */
	void process(ServiceContext context);

	/** Postprocess a service request.
	 * @param context context of the service request.
	 */
	void postprocess(ServiceContext context);

	/** Stops the service. This method is only called once during the lifecycle
	 * of the application.
	 */
	void stop();
	
	/** Adds a service to the chain of services defining a unit of work.
	 * @param service service to add.
	 */
	void addChain(Service service);
	
	/** Returns the chain of services of a unit of work.
	 * @return list of services defining a unit of work.
	 */
	List getChains();
}
