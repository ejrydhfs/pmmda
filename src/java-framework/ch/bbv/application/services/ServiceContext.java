/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.services;

import java.util.Map;

/**
 * The service context defines the context of a service being executed.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface ServiceContext {
	/** Returns the parameters of the context. The map keys are parameter
	 * names and the values are the parameter values.
	 * @return the parameters of the context.
	 * @see #setParameters
	 */
	Map getParameters();
	
	/** Sets the parameteers of the context.
	 * @param parameters the parameters of the context.
	 * @see #getParameters
	 */
  void setParameters(Map parameters);
}
