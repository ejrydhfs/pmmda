/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.events;

import java.sql.Timestamp;

/**
 * The event defines a notification event sent to interested listeners. The
 * signature of the class provides services to send and receive events through
 * various communication channels. Events are always sent asynchronously.
 * @author marcelbaumann
 * @version $Revision$
 */
public interface Event {
	/** Returns the identifier of the event. The identifier is application
	 * specific and not managed in the framework.
	 * @return the identifier of the event.
	 */
  int getId();
  
  /** Returns the qualified name of the event class. 
   * @return qualified class name of the event class.
   */
  String getType();
  
  /** Returns the timestamp when the event instance was created.
   * @return timestamp of the event creation.
   */
  Timestamp getTimestamp();
  
  /** Returns the XML representation of the event instance.
   * @return XML represenation of the event.
   */
  String toXML();
}
