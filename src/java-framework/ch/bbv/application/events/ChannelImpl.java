/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.events;


/**
 * The channel implementation provides a default implemenation of all common
 * methods of the class. The subclasses only needs to implement the send event
 * method.
 * @author marcelbaumann
 * @version $Revision$
 */
public abstract class ChannelImpl implements Channel {
	/** Source of the channel. The source pushes events through the channel.
	 */
	private EventSource source;
	
	/** Type of the channel defining the communication medium.
	 */
	private String type;
	
	/** Name of the channel for administrative purposes.
	 */
	private String name;
	
	
	/** Constructor of the class.
	 * @param source source of the events for the channel.
	 * @param name name of the channel.
	 * @param type type of the channel.
	 */
	protected ChannelImpl(EventSource source, String name, String type) {
		this.source = source;
		this.name = name;
		this.type = type;
	}

	public EventSource getSource() {
		return source;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
