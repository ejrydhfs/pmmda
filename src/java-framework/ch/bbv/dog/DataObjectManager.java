/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import ch.bbv.application.Component;
import ch.bbv.application.ComponentImpl;

/**
 * The data object manager defines a coherant set of handlers responsible to
 * provide all services to manipulate data objects and their lightweigth
 * representations, and to communicate with the underlying layer.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class DataObjectManager extends ComponentImpl implements Component {
  /**
   * Name of the component in the application.
   */
  public static final String NAME = "DataObjectManager";

  /**
   * data object handler used to manipulate data objects.
   */
  private DataObjectHandler boHandler;

  /**
   * Lightweight data object handler used to manipulate lightweight objects.
   */
  private LightweightObjectHandler lwHandler;

  /**
   * Lock manager responsible for mutual exclusion when modifying data object
   * graphs.
   */
  private GraphLockHandler lockHandler;

  /**
   * Constructor of the class.
   * @param boHandler data object handler.
   * @param lwHandler lightweight object handler.
   * @param lockHandler lcok handler.
   */
  public DataObjectManager(DataObjectHandler boHandler,
      LightweightObjectHandler lwHandler, GraphLockHandler lockHandler) {
    super(NAME);
    this.boHandler = boHandler;
    this.lwHandler = lwHandler;
    this.lockHandler = lockHandler;
  }

  /**
   * Returns the data object handler.
   * @return the requested handler.
   */
  public DataObjectHandler getBoHandler() {
    return boHandler;
  }

  /**
   * Returns the lightweight data object handler.
   * @return the requested handler.
   */
  public LightweightObjectHandler getLwHandler() {
    return lwHandler;
  }

  /**
   * Returns the data object lock handler.
   * @return the requested handler.
   */
  public GraphLockHandler getLockHandler() {
    return lockHandler;
  }

  /**
   * Returns the data object type describing the type of the given data object.
   * @param object data object which type is requested.
   * @return the requested data object type.
   */
  public DataObjectType getType(DataObject object) {
    return boHandler.getType(object);
  }

  /**
   * Returns the data object type describing the type of the given lightweight
   * data object.
   * @param object lightweight data object which type is requested.
   * @return the requested data object type.
   */
  public DataObjectType getType(LightweightObject object) {
    return lwHandler.getType(object);
  }

  /**
   * Registers the data object type defined for the given data object type.
   * @param type data object type declaration.
   */
  public void register(DataObjectType type) {
    if (boHandler != null) {
      boHandler.register(type);
    }
    if (lwHandler != null) {
      lwHandler.register(type);
    }
  }
}