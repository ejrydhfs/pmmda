/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.kernel.fsm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.functor.Procedure;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public class Transition implements Serializable {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The state affected by the transition. If an object is in t he source state,
   * an outgoing transition may fire when the object receives the trigger event
   * of the transition and if the guard cfondition, if any, is satisfied.
   */
  private State source;

  /**
   * The state that is active after the competion of the transition.
   */
  private State target;

  /**
   * The event whose reception by the object iin the source state makes the
   * transition eligible to fire, providing its guard condition is satisfied.
   */
  private Event trigger;

  /**
   * A boolean expression that is evaluated when the transition is triggered by
   * the reception of the event trigger. If the expression evaluates to true,
   * the transitionis eligible to fire; if the expression evaluates to false,
   * the transition does not fire and if there is no other transition that could
   * be triggered by that same event, the event is lost.
   */
  private Guard guard;

  /**
   * An executable atomic computation that may directly act on the object that
   * owns the state machine, and indireclty on other objects that are visible to
   * the object.
   */
  private Procedure action;

  private transient List<State> exitedStates;

  private transient List<State> enteredStates;

  public boolean evaluate(Event event, Interpreter context) {
    boolean success = false;
    if (trigger.equals(event)) {
      if (guard != null) {
        // TODO process guard
      } else {
        success = true;
      }
    }
    return success;
  }

  public void execute(Object owner) {
    if (action != null) {
      // TODO execute action
    }
  }

  public State getSource() {
    return source;
  }

  public State getTarget() {
    return target;
  }

  public List<State> getEnteredStates() {
    if (enteredStates == null) {
      computePath();
    }
    return enteredStates;
  }

  public List<State> getExitedStates() {
    if (exitedStates == null) {
      computePath();
    }
    return exitedStates;
  }

  private void computePath() {
    enteredStates = new ArrayList<State>();
    exitedStates = new ArrayList<State>();
    State exitedState = source;
    while (!target.isSubstate(exitedState)) {
      exitedStates.add(exitedState);
      exitedState = exitedState.getContext();
    }
    State enteredState = target;
    while (enteredState != exitedState) {
      enteredStates.add(0, enteredState);
      enteredState = enteredState.getContext();
    }
    enteredStates = Collections.unmodifiableList(enteredStates);
    exitedStates = Collections.unmodifiableList(exitedStates);
  }

}