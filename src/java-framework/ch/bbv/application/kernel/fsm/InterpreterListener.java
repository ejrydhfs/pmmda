/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.kernel.fsm;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface InterpreterListener {
  /**
   * Called when an entry action is fired.
   * @param context the context of the final state machine instance.
   * @param state the involved state.
   */
  void entryActionFired(Interpreter context, State state);

  /**
   * Called when an exit action is fired.
   * @param context the context of the final state machine instance.
   * @param state the involved state.
   */
  void exitActionFired(Interpreter context, State state);

  /**
   * Called when a set of local transitions are fired.
   * @param context the context of the final state machine instance.
   * @param transition the involved transition.
   */
  void localTransitionsFired(Interpreter context, Transition transition);

  /**
   * Called when a transition is fired.
   * @param context the context of the final state machine instance.
   * @param transition the involved transition.
   */
  void transitionFired(Interpreter context, Transition transition);
  
  /**
   * Called when the processing of the event is started.
   * @param context the context of the final state machine instance.
   * @param event event being processed.
   */
  void eventProcessingStarted(Interpreter context, Event event);

  /**
   * Called when the processing of the event is completed.
   * @param context the context of the final state machine instance.
   * @param event event being processed.
   */
  void eventProcessingCompleted(Interpreter context, Event event);
}