/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.bbv.application.Component;
import ch.bbv.application.ComponentImpl;

/**
 * The data object manager defines a coherant set of handlers responsible to
 * provide all services to manipulate data objects and their lightweigth
 * representations, and to communicate with the underlying layers. The manager
 * provides capabilities for local cache of data object graphs and application
 * wide cache for copies of graphs.These capabilities are useful when the
 * cost of requesting the objects from the underlying layers - for example
 * due to network latency or bandwith - is high.
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
  private DataObjectHandler dataObjectHandler;

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
   * The map for all the data object types and their associated type.
   */
  private Map types;

  /**
   * Constructor of the class.
   * @param dataObjectHandler data object handler.
   * @param lwHandler lightweight object handler.
   * @param lockHandler lock handler.
   */
  public DataObjectManager(DataObjectHandler dataObjectHandler,
      LightweightObjectHandler lwHandler, GraphLockHandler lockHandler) {
    super(NAME);
    this.dataObjectHandler = dataObjectHandler;
    this.lwHandler = lwHandler;
    this.lockHandler = lockHandler;
    types = new HashMap();
  }

  /**
   * Returns the data object handler.
   * @return the requested handler.
   */
  public DataObjectHandler getDataObjectHandler() {
    return dataObjectHandler;
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
   * Returns the data object type associated with the type of the given data
   * object.
   * @param object data object which type is requested.
   * @return the requested type if found otherwise null.
   * @pre object != null
   */
  public DataObjectType getType(DataObject object) {
    assert (object != null);
    return (DataObjectType) types.get(object.getClass());
  }

  /**
   * Returns the data object type associated with the given class.
   * @param clazz class which data object type is requested.
   * @return the requested type if found otherwise null.
   * @pre clazz != null
   */
  public DataObjectType getType(Class clazz) {
    assert (clazz != null);
    return (DataObjectType) types.get(clazz);
  }

  /**
   * Returns the data object type describing the type of the given lightweight
   * data object.
   * @param object lightweight data object which type is requested.
   * @return the requested data object type.
   * @pre object != null
   */
  public DataObjectType getType(LightweightObject object) {
    assert (object != null);
    return lwHandler.getType(object);
  }

  /**
   * Registers the data object type defined for the given data object type.
   * @param type data object type declaration.
   * @pre type != null
   */
  public void register(DataObjectType type) {
    assert (type != null);
    types.put(type.getBoClass(), type);
    if (lwHandler != null) {
      lwHandler.register(type);
    }
  }

  /**
   * Returns all root data objects members of the tree. A root is a data object
   * which data object type has the root flag set.
   * @param root tree of data objects to traverse.
   * @return list of all root objects in the tree.
   * @pre root != null
   */
  public List getRoots(DataObject root) {
    assert (root != null);
    AbstractVisitorFunctor visitor = getType(root).createVisitorInstance();
    CollectRootFunctor functor = new CollectRootFunctor(this);
    visitor.setFunctor(functor);
    DataObjectType.invokeVisitor(root.getClass(), root, visitor);
    return functor.getRoots();
  }
 }