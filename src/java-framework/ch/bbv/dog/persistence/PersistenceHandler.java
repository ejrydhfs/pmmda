/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.persistence;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.odmg.Database;

import ch.bbv.application.Locator;
import ch.bbv.dog.*;

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
public class PersistenceHandler implements DataObjectHandler {
  /**
   * Logger of the instances of the class.
   */
  private static Log log = LogFactory.getLog(PersistenceHandler.class);

  /**
   * The database manager used in the handler to access the databases.
   */
  private DatabaseMgr databaseMgr;

  /**
   * The handler responsible for the OJB specific policy.
   */
  private HandlerPolicyOjb policy;

  /**
   * Default constructor of the class.
   * @param managed indicates if the managed is used in a managed context or
   *        not.
   */
  public PersistenceHandler(boolean managed) {
    databaseMgr = new DatabaseMgr();
    if (managed) {
      policy = new HandlerPolicyManagedOjb(databaseMgr);
    } else {
      policy = new HandlerPolicyOjb(databaseMgr);
    }
  }

  public void openDatasource(String source, boolean readonly) {
    assert (source != null);
    int mode = (readonly ? Database.OPEN_READ_ONLY : Database.OPEN_READ_WRITE);
    databaseMgr.openDatabase(source, mode);
  }

  public void closeDatasource(String source) {
    assert (source != null);
    Database database = getDatabase(source);
    databaseMgr.closeDatabase(database);
  }

  public DataObject retrieve(String datasource, Class clazz, Integer id) {
    assert ((datasource != null) && (clazz != null) && (id != null));
    Database database = getDatabase(datasource);
    return (DataObject) policy.retrieve(database, clazz, id);
  }

  public List retrieve(String datasource, Class clazz, String constraints) {
    assert ((datasource != null) && (clazz != null) && (constraints != null));
    Database database = getDatabase(datasource);
    return policy.retrieve(database, clazz, constraints);
  }

  public LightweightObject retrieveLw(String datasource, Class clazz, Integer id) {
    assert ((datasource != null) && (clazz != null) && (id != null));
    Database database = getDatabase(datasource);
    return (LightweightObject) policy.retrieve(database, clazz, id);
  }

  public List retrieveLw(String datasource, Class clazz, String constraints) {
    assert ((datasource != null) && (clazz != null));
    Database database = getDatabase(datasource);
    return policy.retrieve(database, clazz, constraints);
  }

  public List store(String datasource, Class clazz, DataObject root) {
    assert ((datasource != null) && (clazz != null) && (root != null));
    if (log.isTraceEnabled()) {
      StringBuffer text = new StringBuffer();
      text.append("start store operation in ").append(datasource).append(
          " class:").append(clazz.getName()).append(" id: ").append(
          root.getId());
      log.trace(text.toString());
    }

    Database database = getDatabase(datasource);
    CollectFunctor collect = new CollectFunctor();
    AbstractVisitorFunctor visitor = getVisitor(clazz);
    List originals = Collections.EMPTY_LIST;
    visitor.setFunctor(collect);
    if (!root.isNew()) {
      DataObject originalRoot = retrieve(datasource, clazz, root.getId());
      if (originalRoot != null) {
        DataObjectType.invokeVisitor(clazz, originalRoot, visitor);
        originals = new ArrayList(collect.getObjects());
      }
    }
    collect.reset();
    DataObjectType.invokeVisitor(clazz, root, visitor);

    List objects = collect.getObjects();
    List deleted = collect.getMissingObjectsFrom(originals);
    List inserted = collect.getNewObjects();
    List updated = collect.getUdpatedObjects();
    policy.update(database, originals, objects, deleted, inserted, updated);

    if (log.isTraceEnabled()) {
      StringBuffer text = new StringBuffer();
      text.append("end store operation ").append(objects.size()).append(
          " inserted: ").append(inserted.size()).append(" updated: ").append(
          updated.size()).append("deleted ").append(deleted.size());
      log.trace(text.toString());
    }
    return collect.getUpdates();
  }

  public void remove(String datasource, Class clazz, DataObject root) {
    assert ((datasource != null) && (clazz != null) && (root != null));
    DataObject originalRoot = retrieve(datasource, clazz, root.getId());
    remove(datasource, clazz, root, originalRoot);
  }

  public void remove(String datasource, Class clazz, Integer id) {
    assert ((datasource != null) && (clazz != null) && (id != null));
    DataObject object = retrieve(datasource, clazz, id);
    if (object != null) {
      remove(datasource, clazz, null, object);
    }
  }

  /**
   * Returns the visitor of the class.
   * @param clazz business object class which visitor is requested.
   * @return requested visitor if found otherwise false.
   */
  private AbstractVisitorFunctor getVisitor(Class clazz) {
    DataObjectManager manager = (DataObjectManager) Locator.getInstance()
        .getComponent(DataObjectManager.NAME);
    DataObjectType type = manager.getType(clazz);
    return type.createVisitorInstance();
  }

  /**
   * Returns the database with the given datasource name.
   * @param datasource name of the datasource to be found.
   * @return the database associated with the datasource.
   * @throws MissingResourceException the requested database is unknown.
   */
  private Database getDatabase(String datasource)
      throws MissingResourceException {
    Database database = databaseMgr.getDatabase(datasource);
    if (database == null) {
      throw new MissingResourceException("datasource not found",
          PersistenceHandler.class.getName(), datasource);
    }
    return database;
  }

  /**
   * Remvoes the data object graph.
   * @param datasource datasouce containing the data objects to remove.
   * @param clazz class of the root object.
   * @param root root of the data object graph to remove.
   * @param originalRoot root of the orginal data object graph to remove.
   */
  private void remove(String datasource, Class clazz, DataObject root,
      DataObject originalRoot) {
    if (log.isTraceEnabled()) {
      StringBuffer text = new StringBuffer();
      text.append("start remove operation in ").append(datasource).append(
          " class:").append(clazz.getName()).append(" id: ").append(
          originalRoot.getId());
      log.trace(text.toString());
    }
    Database database = getDatabase(datasource);
    CollectFunctor collect = new CollectFunctor();
    AbstractVisitorFunctor visitor = getVisitor(clazz);
    visitor.setFunctor(collect);
    DataObjectType.invokeVisitor(clazz, originalRoot, visitor);
    List deleted = new ArrayList(collect.getObjects());
    policy.update(database, deleted, deleted, deleted, Collections.EMPTY_LIST,
        Collections.EMPTY_LIST);
    if (log.isTraceEnabled()) {
      StringBuffer text = new StringBuffer();
      text.append("end remove operation ").append(deleted.size());
      log.trace(text.toString());
    }
  }

}