/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.remote;

import java.util.List;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.updates.DataObjectUpdate;
import ch.bbv.dog.updates.DogUpdate;
import ch.bbv.dog.LightweightObject;

/**
 * The data object remote handler provides an interface to retrieve and store data
 * objects. The necessary CRUD operations are provided for all defined data
 * objects. The handler is aware of the semantics of data objects and can handle
 * their lightweight representation or generate a clone of a tree of objects.
 * @author UeliKurmann
 * @version $Revision$
 */
public interface DataObjectRemoteHandler {
 
  /**
   * Retrieves the data object tree where the root object has the given
   * identifier and class.
   * @param datasource name of the datasource containing the instances.
   * @param clazz class of the root object to retrieve.
   * @param id identifier of the root object to retrieve.
   * @return the requested object if found otherwise null.
   * @pre (datasource != null) && (clazz != null) && (id != null)
   */
  DataObject retrieve(String datasource, Class clazz, Integer id);

  /**
   * Retrieves the list of data objects of the given type and fulfilling the
   * constraints.
   * @param datasource name of the datasource containing the instances.
   * @param clazz class of the root object to retrieve.
   * @param constraints the constraints of the query. The constraints can be
   *          null.
   * @return the list of found data objects.
   * @pre (datasource != null) && (clazz != null)
   */
  List<DataObject> retrieve(String datasource, Class clazz, String constraints);

  /**
   * Retrieves the data lightweight object tree where the root object has the
   * given identifier and class.
   * @param datasource name of the datasource containing the instances.
   * @param clazz class of the root object to retrieve.
   * @param id identifier of the root object to retrieve.
   * @return the requested object if found otherwise null.
   * @pre (datasource != null) && (clazz != null) && (id != null)
   */
  LightweightObject retrieveLw(String datasource, Class clazz, Integer id);

  /**
   * Retrieves the list of lightweight data objects of the given type and
   * fulfilling the constraints.
   * @param datasource name of the datasource containing the instances.
   * @param clazz class of the root object to retrieve.
   * @param constraints the constraints of the query.
   * @return the list of found lightweight data objects.
   * @pre (datasource != null) && (clazz != null)
   */
  List<LightweightObject> retrieveLw(String datasource, Class clazz,
      String constraints);

  /**
   * Stores the given data object into the persistent store.
   * @param datasource name of the datasource containing the instances
   * @param dogUpdate the DOG update for the graph to store
   * @return list of update information of the involved object which can be
   *         propagated to the upper layer
   * @pre (datasource != null)
   */
  List<DataObjectUpdate> store(String datasource, DogUpdate dogUpdate);

  /**
   * Removes the given data object from the persistent store.
   * @param datasource name of the datasource containing the instances.
   * @param clazz class of the object to remove. Used to store subclasses as
   *          instances of a super class.
   * @param root root object to remove.
   * @pre (datasource != null) && (clazz != null) && (root != null)
   */
  void remove(String datasource, Class clazz, DataObject root);

  /**
   * Removes the data object with the given identifier from the persistent
   * store.
   * @param datasource name of the datasource containing the instances.
   * @param clazz class of the object to store. Used to store subclasses as
   *          instances of a super class.
   * @param id identifier of the object to remove.
   * @pre (datasource != null) && (clazz != null) && (id != null)
   */
  void remove(String datasource, Class clazz, Integer id);
}