/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.kernel;

/**
 * The task interface defines the common methods of active objects.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface Task {
	/** Returns the name of the task.
	 * @return the name of the task.
	 */
  String getName();
  
  /** Returns the priority of the task.
   * @return the priority of the task.
   */
  Integer getPriority();
}
