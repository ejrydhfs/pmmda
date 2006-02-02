/* 
 * copyright 2000-2005 BBV Software Services AG
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 */
package ch.bbv.notification.client;

import ch.bbv.notification.communication.EventInfo;

/**
 * This interface must be implemented by all clients, which use the notification service
 * @see ch.bbv.notification.client.QueueReceiver
 * @author Bruno Persi
 */
public interface Updateable {
	
	/**
	 * method receivs an <code>EventInfo</code> with the changed values<br>
	 * its <b>mandatory</b> declare the implementation as <code>synchronized</code>
	 * while this method is called by an other thread, and <code>java Swing</code> is not thread safe
	 * @param info the event as <code>EventInfo</code>
	 * @pre info != null
	 */
	public void update(EventInfo info);

}
