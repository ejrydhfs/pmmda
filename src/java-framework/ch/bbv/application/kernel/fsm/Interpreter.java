/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.kernel.fsm;

import java.util.*;

/**
 * The state machine implements the rules how a hierarchical state machine
 * should react on incoming events. All context information, history and pending
 * timeouts are handled in the state machine instance. The interpreter is based
 * on four axioms of the finite state machine theory.
 * <ul>
 * <li>Events are reliably delivered to their destination. No event get lost.
 * </li>
 * <li>A global time reference is available. This time reference is used to
 * evaluate timeouts and change events.</li>
 * <li>The event dispatch is synchronous. An event is received at the same
 * moment in all states.</li>
 * <li>The UML finite state machine definition is based on discrete simulation.
 * </li>
 * </ul>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class Interpreter {
  /**
   * The hierarchical state machine to process.
   */
  private State machine;

  private Object[] parameters;

  private Object owner;

  /**
   * The Map <State, Long> defines the time when the state was activated. This
   * information is used to evaluate time guards.
   */
  private Map<State, Long> activeStates;

  /**
   * The Map <State, List> defines the shallow or deep history of the state used
   * as key.
   */
  private Map<State, List<State>> histories;

  /**
   * The List <InterpreterListener>contains all registered listeners.
   */
  private List<InterpreterListener> listeners;

  /**
   * Constructor of the class.
   * @param machine the finite state machine to execute.
   */
  public Interpreter(State machine) {
    this.machine = machine;
    listeners = new ArrayList<InterpreterListener>();
  }

  /**
   * Adds a listener to the interpreter.
   * @param listener listener to add.
   * @pre listener != null
   * @post listeners.contains(listener)
   */
  public void addListener(InterpreterListener listener) {
    listeners.add(listener);
  }

  /**
   * Removew a listener to the interpreter.
   * @param listener listener to remove.
   * @pre listener != null
   * @post !listeners.contains(listener)
   */
  public void removeListener(InterpreterListener listener) {
    listeners.remove(listener);
  }

  public void init(Object[] parameters) {
    this.parameters = parameters;
  }

  /**
   * Returns the owner of the interpreter. In the UML terminology a finite state
   * machine is always associated with an object.
   * @return the owner of the interpreter and its state machine.
   */
  Object getOwner() {
    return owner;
  }

  /**
   * Returns the time when the state was activate . If the state is not active 0
   * is returned.
   * @param state state which activation time is requested.
   * @return the time the state was activated in milliseconds.
   * @see System#currentTimeMillis()
   */
  long getTimeActivated(State state) {
    Long time = activeStates.get(state);
    return (time != null) ? time.longValue() : 0;
  }

  /**
   * Processes an incoming event and fires all eligible transitions. The
   * algorithm follow the rules of hierarchical finite state machines. The
   * traversal order of the active states set is undetermined.
   * <ol>
   * <li>The local transitions of all active states are fired.</li>
   * <li>For each eligible transition of an active state the following actions
   * are executed.</li>
   * <ol>
   * <li>Collects all exited states and fires the associated exit action in the
   * order they are traversed.</li>
   * <li>Execute the transtion action.</li>
   * <li>Collects all entered states and fires the associated enter action in
   * the order they are traversed.</li>
   * </ol>
   * <li>Compute the new set of active states.</li>
   * </ol>
   * @param event
   */
  public void process(Event event) {
    Long time = new Long(System.currentTimeMillis());
    List<Transition> transitions = new ArrayList<Transition>();
    for (Iterator i = activeStates.values().iterator(); i.hasNext();) {
      State state = (State) i.next();
      for (Iterator j = state.getEligibleInternalTransitions(event, this)
          .iterator(); j.hasNext();) {
        Transition transition = (Transition) j.next();
        transition.execute(owner);
        localTransitionFired(transition);
      }
      transitions.addAll(state.getEligibleTransitions(event, this));
    }
    for (Transition transition : transitions) {
      for (State state : transition.getExitedStates()) {
        state.executeExitAction(owner);
        entryActionFired(state);
      }
      transition.execute(owner);
      transitionFired(transition);
      activeStates.remove(transition.getSource());
      activeStates.put(transition.getTarget(), time);
      for (State state : transition.getEnteredStates()) {
        state.executeEntryAction(owner);
        exitActionFired(state);
      }
    }
  }

  public void process() {

  }

  /**
   * Triggers the local transition fired event to the registered listener.
   * @param transition the fired transition.
   */
  private void localTransitionFired(Transition transition) {
    if (listeners.size() > 0) {
      for (Iterator i = listeners.iterator(); i.hasNext();) {
        ((InterpreterListener) i.next())
            .localTransitionsFired(this, transition);
      }
    }
  }

  /**
   * Triggers the transition fired event to the registered listener.
   * @param transition the fired transition.
   */
  private void transitionFired(Transition transition) {
    if (listeners.size() > 0) {
      for (Iterator i = listeners.iterator(); i.hasNext();) {
        ((InterpreterListener) i.next()).transitionFired(this, transition);
      }
    }
  }

  /**
   * Triggers the entry action fired event to the registered listener.
   * @param state state which action was fired.
   */
  private void entryActionFired(State state) {
    if (listeners.size() > 0) {
      for (Iterator i = listeners.iterator(); i.hasNext();) {
        ((InterpreterListener) i.next()).entryActionFired(this, state);
      }
    }
  }

  /**
   * Triggers the exit action fired event to the registered listener.
   * @param state state which action was fired.
   */
  private void exitActionFired(State state) {
    if (listeners.size() > 0) {
      for (Iterator i = listeners.iterator(); i.hasNext();) {
        ((InterpreterListener) i.next()).entryActionFired(this, state);
      }
    }
  }
}