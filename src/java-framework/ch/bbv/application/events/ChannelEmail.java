/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.events;

/**
 * The email channel provides a channel implementation to propagate events as
 * email messages to all registered listeners.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ChannelEmail extends ChannelImpl implements Channel {
  /**
   * Mail type can be used with any applications implementing the related
   * Internet RFC for SMTP.
   */
  public static final String CHANNEL_TYPE = "SMTP";

  /**
   * The property defining an email address to send an event.
   */
  public static final String EMAIL_ADDRESS = "email-address";

  /**
   * The reference to the event manager responsible for the listener
   * registration.
   */
  private EventManager manager;

  /**
   * Constructor of the class.
   * @param source source of the events for the channel.
   * @param name name of the channel.
   * @param manager the even manager handling the channel.
   */
  public ChannelEmail(EventSource source, String name, EventManager manager) {
    super(source, name, CHANNEL_TYPE);
    this.manager = manager;
  }

  public void sendEvent(Event event) {
    String header = getType() + ":" + getName();
    String text = event.toXML();
    for (EventListener listener : manager.getListeners(this)) {
      String address = listener.getProperty(EMAIL_ADDRESS);
      if (address != null) {
        sendEmail(address, header, text);
      }
    }
  }

  private void sendEmail(String address, String header, String text) {
    // TODO implement
  }
}
