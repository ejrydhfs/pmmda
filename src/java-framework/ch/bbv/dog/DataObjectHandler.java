/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog;

import java.util.List;

/**
 * The data object handler provides an interface to retrieve and store data
 * objects. The necessary CRUD operations are provided for all defined data
 * objects. The handler is aware of the semantics of data objects and can handle
 * their lightweight representation or generate a clone of a tree of objects.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface DataObjectHandler {
  /**
   * Opens the datasource with the given name.
   * @param source name of the datasource.
   * @param readonly flag indicating if the datasource is opened read-only.
   * @pre datasource != null
   */
  void openDatasource(String source, boolean readonly);

  /**
   * Closes the datasource with the given name.
   * @param datasource name of the datasource.
   * @pre datasource != null
   */
  void closeDatasource(String datasource);

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
   *        null.
   * @return the list of found data objects.
   * @pre (datasource != null) && (clazz != null)
   */
  List retrieve(String datasource, Class clazz, String constraints);

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
  List retrieveLw(String datasource, Class clazz, String constraints);

  /**
   * Stores the given data object into the persistent store.
   * @param datasource name of the datasource containing the instances.
   * @param clazz class of the object to store. Used to store subclasses as
   *        instances of a super class.
   * @param root root object of the graph to store.
   * @return list of update information of the involved object which can be
   *         propagated to the upper layer.
   * @pre (datasource != null) && (clazz != null) && (root != null)
   */
  List store(String datasource, Class clazz, DataObject root);

  /**
   * Removes the given data object from the persistent store.
   * @param datasource name of the datasource containing the instances.
   * @param clazz class of the object to remove. Used to store subclasses as
   *        instances of a super class.
   * @param root root object to remove.
   * @pre (datasource != null) && (clazz != null) && (root != null)
   */
  void remove(String datasource, Class clazz, DataObject root);

  /**
   * Removes the data object with the given identifier from the persistent
   * store.
   * @param datasource name of the datasource containing the instances.
   * @param clazz class of the object to store. Used to store subclasses as
   *        instances of a super class.
   * @param id identifier of the object to remove.
   * @pre (datasource != null) && (clazz != null) && (id != null)
   */
  void remove(String datasource, Class clazz, Integer id);
}