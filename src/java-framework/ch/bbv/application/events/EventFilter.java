/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.events;

/**
 * The event filter filters the events a listener will receive. 
 * @author marcelbaumann
 * @version $Revision$
 */
public interface EventFilter {
	/** Returns true if the event is relevant otherwise false. Only
	 * accepted events should be processed further.
	 * @param event event to filter.
	 * @return result of the filter process.
	 */
  boolean accept(Event event);
}
