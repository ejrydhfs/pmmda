/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.kernel.fsm;

import java.io.Serializable;

/**
 * An event is the specification of a significant occurence that has a location
 * in time and space. In the context of state machines, an event is an occurence
 * of a stimulus that can trigger a state transition.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface Event extends Serializable {

  /**
   * Returns true if the event is equivalent to the object. An event is
   * equivalent if and only if it activates the trigger of a transition.
   * @param trigger trigger to evaluate.
   * @return true if equivalent otherwise false.
   */
  public boolean equals(Event trigger);
}