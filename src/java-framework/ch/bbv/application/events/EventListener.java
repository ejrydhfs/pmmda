/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.events;

/**
 * The event listener defines the interface for a class wishing to listen to
 * events it registered to.
 * @author marcelbaumann
 * @version $Revision$
 */
public interface EventListener {
  /** Returns the name of the event listener.
   * @return the name of the listener.
   */
  String getName();

  /** Adds a property to the event listener.
	 * @param property name of the property.
	 * @param value value of the property.
	 */
	void addProperty(String property, String value);
	
	/** Returns the value of property.
	 * @param property name of the property.
	 * @return value of the property.
	 */
	String getProperty(String property);
	
	/** The method is called each time a relevant event is available to the 
	 * listener. An event is relevant if it is received on a channel where the
	 * listener is registered and passes through the optional filters.
	 * @param event event to process.
	 */
  void onMessage(Event event);
}
