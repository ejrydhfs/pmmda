/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.kernel.fsm;

/**
 * The time guard implements the guard to simulate a time event. A time event
 * triggers a transition when the guard of the condition becomes true. The time
 * guard class supports relative and absolute time limits.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class TimeGuard implements Guard {
  /**
   * The relative timeout after which the guard is true. The timeout unit is
   * milliseconds.
   */
  private long timeout;

  public boolean evaluate(Interpreter context, Transition transition) {
    return context.getTimeActivated(transition.getSource()) + timeout <= System
        .currentTimeMillis();
  }
}