/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.events;

import java.util.*;

/**
 * The event manager is the local responsible for all registered source channels
 * and listener channels.
 * @author marcelbaumann
 * @version $Revision$
 */
public class EventManager {
  /**
   * The map contains all channels defined by a source. The map key is an event
   * source, the value is a list of channels.
   */
  private Map<EventSource, List<Channel>> sourceChannels;

  /**
   * The map contains all channels defined by a listener. The map key is a
   * channel, the value is a list of listeners.
   */
  private Map<Channel, List<EventListener>> listenerChannels;

  public EventManager() {
    sourceChannels = new HashMap<EventSource, List<Channel>>();
    listenerChannels = new HashMap<Channel, List<EventListener>>();
  }

  public void register(EventSource source, Channel channel) {
    List<Channel> channels = sourceChannels.get(source);
    if (channels == null) {
      channels = new ArrayList<Channel>();
      sourceChannels.put(source, channels);
    }
    channels.add(channel);
  }

  public void unregister(EventSource source, Channel channel) {
    List<Channel> channels = sourceChannels.get(source);
    if (channels != null) {
      channels.remove(channel);
    }
  }

  public void register(EventListener listener, Channel channel) {
    List<EventListener> listeners = listenerChannels.get(channel);
    if (listeners == null) {
      listeners = new ArrayList<EventListener>();
      listenerChannels.put(channel, listeners);
    }
    listeners.add(listener);
  }

  public List<EventListener> getListeners(Channel channel) {
    List<EventListener> listeners = listenerChannels.get(channel);
    if (listeners == null) {
      listeners = Collections.emptyList();
    } else {
      listeners = Collections.unmodifiableList(listeners);
    }
    return listeners;
  }

  public void unregister(EventListener listener, Channel channel) {
    List<EventListener> listeners = listenerChannels.get(channel);
    if (listeners != null) {
      listeners.remove(channel);
    }
  }

  public void send(EventSource source, Event event) {
    if (sourceChannels.containsKey(source)) {
      for (Channel channel : sourceChannels.get(source)) {
        channel.sendEvent(event);
      }
    }
  }
}
