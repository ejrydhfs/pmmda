/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.application.events;

import java.util.List;

/**
 * The listener registration registers a listener to a channel of events and
 * defines the filters on the received events.
 * @author marcelbaumann
 * @version $Revision$
 */
public class ListenerRegistration {
  private EventListener listener;

  private Channel channel;

  private List filters;

  /**
   * Constructor of the class.
   * @param listener listener interested in the channel.
   * @param channel channel to listen to.
   * @param filters the filters of the listeners on the received events.
   */
  public ListenerRegistration(EventListener listener, Channel channel,
      List filters) {
    this.listener = listener;
    this.channel = channel;
    this.filters = filters;
  }

  public EventListener getListener() {
    return listener;
  }

  /**
   * Returns the list of filters defined for this channel.
   * @return the list of filters for the channel. If none is defined an empty
   *         list is returned.
   * @see #setFilters(List)
   */
  public List getFilters() {
    return filters;
  }

  /**
   * Sets the filter list for the channel.
   * @param filters new filter list.
   * @see #getFilters()
   */
  void setFilters(List filters) {
    this.filters = filters;
  }

  /**
   * Returns the channel of the listener.
   * @return
   */
  Channel getChannel() {
    return channel;
  }
}