/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.kernel.fsm;

/**
 * The guard condition is evaluated only after the trigger event for its
 * transition occurs.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface Guard {
  /**
   * Returns the result of the evaluation of the guard boolean condition.
   * @param context the context of the final state machine instance.
   * @param transition transition which is guarded.
   * @return true if the guard can be passed otherwise false.
   */
  boolean evaluate(Interpreter context, Transition transition);
}