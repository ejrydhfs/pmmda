/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.application;

/**
 * The application error class defines all the exceptions which can be thrown by
 * the various components of the applications package. These errors define the
 * exceptional conditions, which can happen during the execution of the factory
 * startup and shutdown pattern.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ComponentException extends Exception {
  private static final long serialVersionUID = 20041201;

  /**
   * A client to create more than one application instance.
   */
  public static final int APPLICATION_ERROR = 0;

  /**
   * Error code thrown by a component during the initialize phase.
   */
  public static final int INITIALIZE_COMPONENT_ERROR = 1;

  /**
   * Error code thrown by a component during the startup phase.
   */
  public static final int STARTUP_COMPONENT_ERROR = 2;

  /**
   * Error code thrown by a component during the running phase.
   */
  public static final int RUNNING_COMPONENT_ERROR = 3;

  /**
   * Error code thrown by a component during the normal shutdown phase.
   */
  public static final int SHUTDOWN_COMPONENT_ERROR = 4;

  /**
   * Error code thrown by a component during the immediate shutdown phase.
   */
  public static final int IMMEDIATE_SHUTDOWN_COMPONENT_ERROR = 5;

  /**
   * The load operation of a given component was erroneous.
   */
  public static final int LOAD_COMPONENT_ERROR = 6;

  /**
   * Reason why the application error was thrown.
   */
  private int reason;

  /**
   * Name of the application error.
   */
  private String name;

  /**
   * Text describing the application error.
   */
  private String text;

  /**
   * Constructor of the class. All instances are initialized.
   * @param reason reason why the application error is thrown.
   * @param componentName name of the component from which it is thrown.
   * @param text text documenting why it was thrown.
   * @param cause cause of the exception.
   */
  public ComponentException(int reason, String componentName, String text,
      Throwable cause) {
    super(text, cause);
    this.reason = reason;
    this.name = componentName;
    this.text = text;
  }

  /**
   * Constructor of the class. All instances are initialized.
   * @param reason reason why the application error is thrown.
   * @param componentName name of the component from which it is thrown.
   * @param text text documenting why it was thrown.
   */
  public ComponentException(int reason, String componentName, String text) {
    super(text);
    this.reason = reason;
    this.name = componentName;
    this.text = text;
  }

  /**
   * Constructor of the class. All instances are initialized.
   * @param reason reason why the application error is thrown.
   * @param componentName name of the component from which it is thrown.
   */
  public ComponentException(int reason, String componentName) {
    this(reason, componentName, null);
  }

  /**
   * Returns the reason of the application error.
   * @return The reason of the application error.
   */
  public int getReason() {
    return reason;
  }

  /**
   * Returns the component name where the application error was created.
   * @return The component name of the application error.
   */
  public String getComponentName() {
    return name;
  }

  /**
   * Returns a string representation of the object.
   * @return String representation of the object.
   */
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append(name);
    buffer.append("(");
    buffer.append(reason);
    buffer.append(")");
    if (text != null) {
      buffer.append(text);
    }
    return buffer.toString();
  }
}
