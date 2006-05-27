/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.persistence.ojb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.odmg.Database;

import ch.bbv.dog.*;
import ch.bbv.dog.persistence.CollectFunctor;
import ch.bbv.dog.persistence.PersistenceHandler;

/**
 * The data object persistence handler retrieves and stores data object graphs
 * in the database. The handler uses the services defined in the package and
 * provided in OJB layer to communicate with the underlying database. The
 * handler always returns a copy of the graph to be able later to infer which
 * objects were removed.
 * <ul>
 * <li>The retrieval of a graph of data objects is the creation of the correct
 * OQL statement and its execution.</li>
 * <li>The update of a graph of data objects is to organize the objects into
 * one set of objects to delete, to insert and to update and to perform these
 * operations. Upon completion the update information should be sent back to the
 * client.</li>
 * </ul>
 * @author MarcelBaumann
 * @version $Revision$
 */

public class PersistenceHandlerOjb implements PersistenceHandler {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(PersistenceHandlerOjb.class);

  /**
   * Hack due to compiler differences between JDK 1.5 and eclipse. This constant
   * is used instead of Collections.emptyList().
   */
  private static final List<DataObject> EMPTY_LIST = new ArrayList<DataObject>();

  /**
   * The database manager used in the handler to access the databases.
   */
  private DatabaseMgrOjb databaseMgr;

  /**
   * The handler responsible for the OJB specific policy.
   */
  private HandlerPolicyOjb policy;

  /**
   * The mode used to synchronize the modified objects with the original ones.
   */
  private Mode mode;

  /**
   * Default constructor of the class.
   * @param managed indicates if the managed is used in a managed context or
   *          not.
   * @param mode mode used to synchronize the modified objects with the ones in
   *          the cache or the persistent store.
   */
  public PersistenceHandlerOjb(boolean managed, Mode mode) {
    databaseMgr = new DatabaseMgrOjb();
    if (managed) {
      policy = new HandlerPolicyManagedOjb(databaseMgr);
    } else {
      policy = new HandlerPolicyOjb(databaseMgr);
    }
    this.mode = mode;
  }

  public void openDatasource(String source, boolean readonly) {
    assert source != null;
    int mode = (readonly ? Database.OPEN_READ_ONLY : Database.OPEN_READ_WRITE);
    databaseMgr.openDatabase(source, mode);
  }

  public void closeDatasource(String source) {
    assert source != null;
    Database database = getDatabase(source);
    databaseMgr.closeDatabase(database);
  }

  public DataObject retrieve(String datasource, Class clazz, Integer id) {
    assert (datasource != null) && (clazz != null) && (id != null);
    Database database = getDatabase(datasource);
    return (DataObject) policy.retrieve(database, clazz, id);
  }

  public List<DataObject> retrieve(String datasource, Class clazz,
      String constraints) {
    assert (datasource != null) && (clazz != null) && (constraints != null);
    Database database = getDatabase(datasource);
    List objects = policy.retrieve(database, clazz, constraints);
    List<DataObject> list = new ArrayList<DataObject>(objects);
    return list;
  }

  public LightweightObject retrieveLw(String datasource, Class clazz, Integer id) {
    assert (datasource != null) && (clazz != null) && (id != null);
    Database database = getDatabase(datasource);
    return (LightweightObject) policy.retrieve(database, clazz, id);
  }

  public List<LightweightObject> retrieveLw(String datasource, Class clazz,
      String constraints) {
    assert (datasource != null) && (clazz != null);
    Database database = getDatabase(datasource);
    List objects = policy.retrieve(database, clazz, constraints);
    List<LightweightObject> list = new ArrayList<LightweightObject>(objects);
    return list;
  }

  public List<DataObjectUpdate> store(String datasource, Class clazz,
      DataObject root) {
    assert (datasource != null) && (clazz != null) && (root != null);
    if (log.isTraceEnabled()) {
      StringBuilder text = new StringBuilder();
      text.append("start store operation in ").append(datasource).append(
          " class:").append(clazz.getName()).append(" id: ").append(
          root.getId());
      log.trace(text.toString());
    }
    List<DataObjectUpdate> updates = null;
    switch (mode) {
    case DETACH:
      // not supported in OJB, handled similarly to replace
      updates = storeReplace(datasource, clazz, root);
      break;
    case REPLACE:
      updates = storeReplace(datasource, clazz, root);
      break;
    case SYNCHRONIZE:
      updates = storeUpdate(datasource, clazz, root);
      break;
    }
    return updates;
  }

  public void remove(String datasource, Class clazz, DataObject root) {
    assert (datasource != null) && (clazz != null) && (root != null);
    DataObject originalRoot = retrieve(datasource, clazz, root.getId());
    remove(datasource, clazz, root, originalRoot);
  }

  public void remove(String datasource, Class clazz, Integer id) {
    assert (datasource != null) && (clazz != null) && (id != null);
    DataObject object = retrieve(datasource, clazz, id);
    if (object != null) {
      remove(datasource, clazz, null, object);
    }
  }

  /**
   * Stores the data object root with replace mode. Modified objects will
   * replace the original ones.
   * @param datasource name of the datasource containing the instances
   * @param clazz class of the object to store. Used to store subclasses as
   *          instances of a super class
   * @param root root object of the graph to store
   * @return list of update information of the involved object which can be
   *         propagated to the upper layer
   */
  private List<DataObjectUpdate> storeReplace(String datasource, Class clazz,
      DataObject root) {
    Database database = getDatabase(datasource);
    CollectFunctor collect = new CollectFunctor();
    AbstractVisitorFunctor visitor = DataObjectMgr.getVisitorFor(clazz);
    visitor.setFunctor(collect);
    List<DataObject> originals = Collections.emptyList();
    if (!root.isNew()) {
      DataObject originalRoot = retrieve(datasource, clazz, root.getId());
      if (originalRoot != null) {
        DataObjectType.invokeVisitor(clazz, originalRoot, visitor);
        originals = new ArrayList<DataObject>(collect.getObjects());
      }
    }
    collect.reset();
    DataObjectType.invokeVisitor(clazz, root, visitor);
    List<DataObject> objects = collect.getObjects();
    List deleted = collect.getMissingObjectsFrom(originals);
    List<DataObject> inserted = collect.getNewObjects();
    List<DataObject> updated = collect.getUdpatedObjects();
    policy.update(database, originals, objects, deleted, inserted, updated);
    if (log.isTraceEnabled()) {
      StringBuilder text = new StringBuilder();
      text.append("end store operation ").append(objects.size()).append(
          " inserted: ").append(inserted.size()).append(" updated: ").append(
          updated.size()).append("deleted ").append(deleted.size());
      log.trace(text.toString());
    }
    return collect.getUpdates();
  }

  /**
   * Stores the data object root with replace mode. Modified objects will
   * replace the original ones.
   * @param datasource name of the datasource containing the instances
   * @param clazz class of the object to store. Used to store subclasses as
   *          instances of a super class
   * @param root root object of the graph to store
   * @return list of update information of the involved object which can be
   *         propagated to the upper layer
   */
  private List<DataObjectUpdate> storeUpdate(String datasource, Class clazz,
      DataObject root) {
    if (root.isNew()) {
      // stores the new graph in the database. All objects must be new.
      Database database = getDatabase(datasource);
      CollectFunctor collect = new CollectFunctor();
      AbstractVisitorFunctor visitor = DataObjectMgr.getVisitorFor(clazz);
      visitor.setFunctor(collect);
      DataObjectType.invokeVisitor(clazz, root, visitor);
      List<DataObject> objects = collect.getObjects();
      // check that all objects are new
      for (DataObject object : objects) {
        assert object.isNew();
      }
      policy.update(database, EMPTY_LIST, objects, EMPTY_LIST, objects,
          EMPTY_LIST);
    } else {
      // synchronizes the new graph with the original one
      ChangeFunctor changes = new ChangeFunctor();
      AbstractVisitorFunctor visitor = DataObjectMgr.getVisitorFor(clazz);
      visitor.setFunctor(changes);
      DataObject originalRoot = retrieve(datasource, clazz, root.getId());
      if (originalRoot != null) {
        changes.reset(originalRoot);
        // propagate changes and commit them to the database
        // TODO: implement the function
      } else {
        // orginal root was deleted. An optimistic locking error happened.
      }
    }
    return null;
  }

  /**
   * Returns the database with the given datasource name.
   * @param datasource name of the datasource to be found
   * @return the database associated with the datasource
   * @throws MissingResourceException if the requested database is unknown
   */
  private Database getDatabase(String datasource)
      throws MissingResourceException {
    Database database = databaseMgr.getDatabase(datasource);
    if (database == null) {
      throw new MissingResourceException("datasource not found",
          PersistenceHandlerOjb.class.getName(), datasource);
    }
    return database;
  }

  /**
   * Removes the data object graph.
   * @param datasource datasouce containing the data objects to remove
   * @param clazz class of the root object
   * @param root root of the data object graph to remove
   * @param originalRoot root of the orginal data object graph to remove
   */
  private void remove(String datasource, Class clazz, DataObject root,
      DataObject originalRoot) {
    if (log.isTraceEnabled()) {
      StringBuilder text = new StringBuilder();
      text.append("start remove operation in ").append(datasource).append(
          " class:").append(clazz.getName()).append(" id: ").append(
          originalRoot.getId());
      log.trace(text.toString());
    }
    Database database = getDatabase(datasource);
    CollectFunctor collect = new CollectFunctor();
    AbstractVisitorFunctor visitor = DataObjectMgr.getVisitorFor(clazz);
    visitor.setFunctor(collect);
    DataObjectType.invokeVisitor(clazz, originalRoot, visitor);
    List<DataObject> deleted = new ArrayList<DataObject>(collect.getObjects());
    policy.update(database, deleted, deleted, deleted, EMPTY_LIST, EMPTY_LIST);
    if (log.isTraceEnabled()) {
      StringBuilder text = new StringBuilder();
      text.append("end remove operation ").append(deleted.size());
      log.trace(text.toString());
    }
  }
}