/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.events;

/**
 * The event source generates events and propagates them through even channels.
 * @author marcelbaumann
 * @version $Revision$
 */
public interface EventSource {
  /** Returns the name of the event source.
   * @return the name of the source.
   */
  String getName();
}
