/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.events;

import java.util.Iterator;

/**
 * The Java channel provides a channel implementation to propagate events 
 * inside a virtual machine.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ChannelJava extends ChannelImpl implements Channel {
	/** Java channel type can only be used inside a virtual machine.
	 */
	public static final String CHANNEL_TYPE = "Java";

	/** The reference to the event manager responsible for the listener
	 * registration.
	 */
	private EventManager manager;

	/** Constructor of the class.
	 * @param source source of the events for the channel.
	 * @param name name of the channel.
	 * @param manager the even manager handling the channel.
	 */
	public ChannelJava(EventSource source, String name, EventManager manager) {
		super(source, name, CHANNEL_TYPE);
		this.manager = manager;
	}
	
	public void sendEvent(Event event) {
		for (Iterator i = manager.getListeners(this).iterator(); i.hasNext();) {
			((EventListener)i.next()).onMessage(event);
		}
	}
}
