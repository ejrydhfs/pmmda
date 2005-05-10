/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.kernel;

import java.io.Serializable;

/**
 * The message interface defines the common methods of messages send to tasks.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface Message extends Serializable {
	/** Returns the type of the message.
	 * @return type of the message.
	 */
  int getType();
  
  /** Returns the identifier of the message.
   * @return identifier of the message.
   */
  long getId();
}
