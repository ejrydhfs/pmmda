/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.application.kernel.ui;

import javax.swing.JComponent;

import ch.bbv.application.kernel.fsm.Event;
import ch.bbv.application.kernel.fsm.Interpreter;
import ch.bbv.application.kernel.fsm.InterpreterListener;
import ch.bbv.application.kernel.fsm.State;
import ch.bbv.application.kernel.fsm.Transition;

/**
 * The text interpreter listener generates a text message for each event
 * received from the interpreter.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class TextInterpreterListener extends JComponent implements
    InterpreterListener {

  private static final String EVENT_RECEIVED = " Event Received      ";

  private static final String EVENT_PROCESSED = " Event Processed     ";

  private static final String STATE_ENTERED = "     State Entered   ";

  private static final String STATE_EXITED = "     State Entered   ";

  private static final String INTERNAL_TRANSITION_FIRED = "   I-Transtion Fired ";

  private static final String TRANSITION_FIRED = "   Transtion Fired   ";

  private StringBuilder buffer;

  public TextInterpreterListener() {
    buffer = new StringBuilder();
  }

  public void entryActionFired(Interpreter context, State state) {
    buffer.setLength(0);
    buffer.append(System.currentTimeMillis()).append(STATE_ENTERED).append(
        state.toString());
  }

  public void exitActionFired(Interpreter context, State state) {
    buffer.setLength(0);
    buffer.append(System.currentTimeMillis()).append(STATE_EXITED).append(
        state.toString());
  }

  public void localTransitionsFired(Interpreter context, Transition transition) {
    buffer.setLength(0);
    buffer.append(System.currentTimeMillis()).append(INTERNAL_TRANSITION_FIRED)
        .append(transition.getSource().toString()).append(" :: ").append(
            transition.toString());
  }

  public void transitionFired(Interpreter context, Transition transition) {
    buffer.setLength(0);
    buffer.append(System.currentTimeMillis()).append(TRANSITION_FIRED).append(
        transition.getSource().toString()).append(" -> ").append(
        transition.getTarget().toString()).append(" :: ").append(
        transition.toString());
  }

  public void eventProcessingStarted(Interpreter context, Event event) {
    buffer.setLength(0);
    buffer.append(System.currentTimeMillis()).append(EVENT_RECEIVED).append(
        event.toString());
  }

  public void eventProcessingCompleted(Interpreter context, Event event) {
    buffer.setLength(0);
    buffer.append(System.currentTimeMillis()).append(EVENT_PROCESSED).append(
        event.toString());
  }
}