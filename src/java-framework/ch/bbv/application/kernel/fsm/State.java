/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.kernel.fsm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.functor.Procedure;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public class State implements Serializable {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Entry state is a pseudo state which indicates the default starting place
   * for the state machine or substate.
   */

  public static final int ENTRY_STATE = 0;

  /**
   * Final state is a pseudo state which indicates that the execution of the
   * state machine or the enclosing state has been completed.
   */
  public static final int FINAL_STATE = 1;

  /**
   * Shallow history state is a pseudo state which remembers only the history of
   * the immediate nested state machine.
   */
  public static final int SHALLOW_HISTORY_STATE = 2;

  /**
   * Deep history state is a pseudo state which remembers down to the innermost
   * nested state of any depth.
   */
  public static final int DEEP_HISTORY_STATE = 3;

  public static final int NORMAL_STATE = 4;

  /**
   * Composite state has a nested structure similar to composition.
   */
  public static final int COMPOSITE_STATE = 5;

  /**
   * Concurrent state let you specify two or more state machines that executes
   * in parallel in the context of the enclosing concurent state.
   */
  public static final int CONCURRENT_STATE = 6;

  /**
   * Name of the state.
   * @inv name != null
   */
  private String name;

  /**
   * The context of the state is the state containing it. The top most state has
   * no context.
   */
  private State context;

  /**
   * Whenever the state is entered, its entry action is dispatched. Entry action
   * may not have arguments or guard conditions.
   */
  private Procedure entryAction;

  /**
   * Whenever the state is leaved, its exit action is dispatched. Exit action
   * may not have arguments or guard conditions.
   */
  private Procedure exitAction;

  private int type;

  /**
   * Once inside a state, you will encounter events you want to handle without
   * leaving the state. These are called internal transitions, and they are
   * subtly different from self-transition. In a self-transition, you leave the
   * state and reenter the state and execute the associated exit and entry
   * actions.
   */
  private List<Transition> internalTransitions;

  /**
   * List of substates belonging to the state.
   */
  private List<State> substates;

  /**
   * Returns the name of the state.
   * @return the name of the state.
   */
  public String getName() {
    return name;
  }

  public State getContext() {
    return context;
  }

  public boolean hasShallowHistory() {
    return false;
  }

  public boolean hasDeepHistory() {
    return false;
  }

  public void executeEntryAction(Object owner) {
    if (entryAction != null) {
      // TODO execute action
    }
  }

  public void executeExitAction(Object owner) {
    if (exitAction != null) {
      // TODO execute action
    }

  }

  public List<Transition> getEligibleTransitions(Event event,
      Interpreter context) {
    List<Transition> transitions = new ArrayList<Transition>();
    for (Transition transition : transitions) {
      if (transition.evaluate(event, context)) {
        transitions.add(transition);
      }
    }
    return transitions;
  }

  public List<Transition> getEligibleInternalTransitions(Event event,
      Interpreter context) {
    List<Transition> transitions = new ArrayList<Transition>();
    for (Transition transition : internalTransitions) {
      if (transition.evaluate(event, context)) {
        transitions.add(transition);
      }
    }
    return transitions;
  }

  public boolean isSubstate(State state) {
    State context = state.getContext();
    while (context != null && (context != state)) {
      context = context.getContext();
    }
    return (context != null);
  }
}