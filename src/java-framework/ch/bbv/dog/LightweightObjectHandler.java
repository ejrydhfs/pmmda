/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

/**
 * The lightweight object handler is responsible to connect data object instance
 * to their lightweight representations. It provides services to create
 * lightweight representations and to synchronize them with their data object
 * instance. The handler could also implement application specific conversions
 * between the data object types and their lightweight representation.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface LightweightObjectHandler {
  /**
   * synchronizes the lightweight object properties with the ones of the
   * associated data object.
   * @param lightweight lightweight object to synchronize.
   * @param object data object modeled through the lightweight object.
   * @pre (lightweight != null) && (object != null) && (lightweight.getId() ==
   *      object.getId())
   */
  void synchronize(Object lightweight, Object object);

  /**
   * Creates the lightweight object representing the data object. The owned
   * objects of the data objects are not mapped.
   * @param object data object to map to a lightweight object.
   * @return lightweight representation of the data object.
   * @pre object != null
   */
  LightweightObject create(DataObject object);

  /**
   * Creates the complete lightweight representation of the graph of data
   * objects.
   * @param root root data object of the graph to map.
   * @return the graph of lightweight objects.
   */
  LightweightObject createGraph(DataObject root);

  /**
   * Registers the data object type defined for the given data object type.
   * @param type data object type declaration.
   * @pre type != null
   */
  void register(DataObjectType type);

  /**
   * Returns the lightweight data object type associated with the type of the
   * given data object.
   * @param object lightweight data object which type is requested.
   * @return the requested type if found otherwise null.
   * @pre object != null
   */
  DataObjectType getType(LightweightObject object);
}