/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.events;


/**
 * The channel defines the type of medium used to propagate the messages to the
 * interested listeners. The same event can be propagated through multiple 
 * channels if listeners exist.
 * @author marcelbaumann
 * @version $Revision$
 */
public interface Channel {
	/** Remote method invocation type can be used between and inside virtual 
	 * machines.
	 */
	String RMI_CHANNEL = "RMI";
	
	/** Java message service type can be used between virtual machines. Using a
	 * bridge it can communicate with other message systems.
	 */
	String JMS_CHANNEL = "JMS-Subscribe";
		
	/** Returns the source of the channel.
	 * @return source of the channel.
	 */
	EventSource getSource();
	
	/** Returns the type of the channel.
	 * @return type of the channel.
	 */
	String getType();
	
	/** Returns the name of the channel.
	 * @return name of the channel.
	 */	
	String getName();
		
	/** Sends the event through the channel.
	 * @param event event to send.
	 */
	void sendEvent(Event event);
}
