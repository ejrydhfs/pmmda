/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import java.util.List;

import ch.bbv.dog.updates.DataObjectUpdate;

/**
 * The synchronization handler provides a data object handler implementation
 * synchronizing concurrent access to data object graphs. A global lock manager
 * is used to guarantee atomic operations.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class SyncHandler implements DataObjectHandler {
  /**
   * The business object handler executing all the delegated operations.
   */
  private DataObjectHandler delegate;

  /**
   * The lock handler used to lock the data object roots to enable concurrent
   * modification of data object graphs.
   */
  private GraphLockHandler lockHandler;

  /**
   * Constructor of the class.
   * @param delegate data object handler delegate.
   * @param lockHandler lock handler used to protect objects being modified.
   */
  public SyncHandler(DataObjectHandler delegate, GraphLockHandler lockHandler) {
    this.delegate = delegate;
    this.lockHandler = lockHandler;
  }

  public void openDatasource(String source, boolean readonly) {
    delegate.openDatasource(source, readonly);
  }

  public void closeDatasource(String source) {
    delegate.closeDatasource(source);
  }

  public DataObject retrieve(String datasource, Class clazz, Integer id) {
    List<DataObjectId> ids = null;
    if (id != null) {
      ids = lockHandler.lockRootId(new DataObjectId(clazz, id));
    }

    DataObject object = delegate.retrieve(datasource, clazz, id);
    if (id != null) {
      lockHandler.unlockGraphs(ids);
    }
    return object;
  }

  public List<DataObject> retrieve(String datasource, Class clazz,
      String constraints) {
    return retrieve(datasource, clazz, constraints);
  }

  public LightweightObject retrieveLw(String datasource, Class clazz, Integer id) {
    return retrieveLw(datasource, clazz, id);
  }

  public List<LightweightObject> retrieveLw(String datasource, Class clazz,
      String constraints) {
    return delegate.retrieveLw(datasource, clazz, constraints);
  }

  public List<DataObjectUpdate> store(String datasource, Class clazz,
      DataObject root) {
    DataObject oldRoot = delegate.retrieve(datasource, clazz, root.getId());
    List<DataObjectId> ids = lockHandler.lockGraph(oldRoot);
    List<DataObjectUpdate> updates = delegate.store(datasource, clazz, root);
    lockHandler.unlockGraphs(ids);
    return updates;
  }

  public void remove(String datasource, Class clazz, DataObject root) {
    DataObject oldRoot = delegate.retrieve(datasource, clazz, root.getId());
    List<DataObjectId> ids = lockHandler.lockGraph(oldRoot);
    delegate.remove(datasource, clazz, root);
    lockHandler.unlockGraphs(ids);
  }

  public void remove(String datasource, Class clazz, Integer id) {
    DataObject oldRoot = delegate.retrieve(datasource, clazz, id);
    List<DataObjectId> ids = lockHandler.lockGraph(oldRoot);
    delegate.remove(datasource, clazz, id);
    lockHandler.unlockGraphs(ids);
  }
}