/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.events;

/**
 * The source registration registers a source to a channel of events.
 * @author marcelbaumann
 * @version $Revision$
 */
public class SourceRegistration {
  private EventSource source;

  private Channel channel;

  /**
   * Constructor of the class.
   * @param source source of the channel.
   * @param channel channel used to propagate the events.
   */
  public SourceRegistration(EventSource source, Channel channel) {
    this.source = source;
    this.channel = channel;
  }

  public EventSource getSource() {
    return source;
  }

  /**
   * Returns the channel of the listener.
   * @return
   */
  public Channel getChannel() {
    return channel;
  }
}