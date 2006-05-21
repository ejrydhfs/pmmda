/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.persistence.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import ch.bbv.dog.*;
import ch.bbv.dog.persistence.ojb.CollectFunctor;

public class HibPersistenceHandler implements DataObjectHandler {

  HibDatabaseMgr databaseMgr;

  /**
   * Logger of the instances of the class.
   */
  protected static Log log = LogFactory.getLog(HibPersistenceHandler.class);

  /**
   * Hack due to compiler differences between JDK 1.5 and eclipse. This constant
   * is used instead of Collections.emptyList().
   */
  private static final List<DataObject> EMPTY_LIST = new ArrayList<DataObject>();

  public HibPersistenceHandler() {
    databaseMgr = new HibDatabaseMgr();
  }

  public void openDatasource(String source, boolean readonly) {
    // TODO: access mode
    databaseMgr.openDatabase(source, 0);
  }

  public void closeDatasource(String datasource) {
    databaseMgr.closeDatabase(databaseMgr.getDatabase(datasource));
  }

  public DataObject retrieve(String datasource, Class clazz, Integer id) {
    Session session = databaseMgr.getDatabase(datasource).getCurrentSession();
    session.beginTransaction();
    DataObject dataObject = (DataObject) session.load(clazz, id);
    session.getTransaction().commit();

    return dataObject;
  }

  public List<DataObject> retrieve(String datasource, Class clazz,
      String constraints) {

    if (constraints != null) {
      throw new RuntimeException("retrieve: constraints not implemented yet!");
    }

    Session session = databaseMgr.getDatabase(datasource).getCurrentSession();
    session.beginTransaction();
    Criteria criteria = session.createCriteria(clazz);
    List<DataObject> result = (List<DataObject>) (criteria.list());
    session.getTransaction().commit();

    return result;
  }

  public LightweightObject retrieveLw(String datasource, Class clazz, Integer id) {
    Session session = databaseMgr.getDatabase(datasource).getCurrentSession();
    session.beginTransaction();
    LightweightObject LightweightObject = (LightweightObject) session.load(
        clazz, id);
    session.getTransaction().commit();

    return LightweightObject;
  }

  public List<LightweightObject> retrieveLw(String datasource, Class clazz,
      String constraints) {

    if (constraints != null) {
      throw new RuntimeException("retrieveLW: constraints not implemented yet!");
    }

    Session session = databaseMgr.getDatabase(datasource).getCurrentSession();
    session.beginTransaction();
    Criteria criteria = session.createCriteria(clazz);
    List<LightweightObject> result = (List<LightweightObject>) (criteria.list());
    session.getTransaction().commit();

    return result;
  }

  public List<DataObjectUpdate> store(String datasource, Class clazz,
      DataObject root) {

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
    HibHandlerPolicy hibPolicy = new HibHandlerPolicy();
    hibPolicy.update(databaseMgr.getDatabase(datasource), originals, objects,
        deleted, inserted, updated);
    if (log.isTraceEnabled()) {
      StringBuilder text = new StringBuilder();
      text.append("end store operation ").append(objects.size()).append(
          " inserted: ").append(inserted.size()).append(" updated: ").append(
          updated.size()).append("deleted ").append(deleted.size());
      log.trace(text.toString());
    }
    return collect.getUpdates();
  }

  public void remove(String datasource, Class clazz, DataObject root) {
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

  private void remove(String datasource, Class clazz, DataObject root,
      DataObject originalRoot) {
    if (log.isTraceEnabled()) {
      StringBuilder text = new StringBuilder();
      text.append("start remove operation in ").append(datasource).append(
          " class:").append(clazz.getName()).append(" id: ").append(
          originalRoot.getId());
      log.trace(text.toString());
    }
    SessionFactory sessionFactory = databaseMgr.getDatabase(datasource);

    CollectFunctor collect = new CollectFunctor();
    AbstractVisitorFunctor visitor = DataObjectMgr.getVisitorFor(clazz);
    visitor.setFunctor(collect);
    DataObjectType.invokeVisitor(clazz, originalRoot, visitor);
    List<DataObject> deleted = new ArrayList<DataObject>(collect.getObjects());
    HibHandlerPolicy hibPolicy = new HibHandlerPolicy();
    hibPolicy.update(sessionFactory, deleted, deleted, deleted, EMPTY_LIST,
        EMPTY_LIST);
    if (log.isTraceEnabled()) {
      StringBuilder text = new StringBuilder();
      text.append("end remove operation ").append(deleted.size());
      log.trace(text.toString());
    }
  }
}
