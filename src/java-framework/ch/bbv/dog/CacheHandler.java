/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import java.util.*;

/**
 * The cache handler defines a proxy object between the clients and the handler
 * performing the retrieval operation. The cache handler caches all retrieved
 * data in a local cache. Each retrieved data object graph is stored in the
 * cache containing the original objects, a copy of the graph is done and stored
 * in the cache containing the copied objects, and the copy is returned.
 * Lightweight object graphs are not cached because they can be rebuild from the
 * cached data object graph. Services are provided to manipulate the cache
 * content. Please read the documentation about the data object graph framework
 * for a discussion why the cache handler is necessary for correct update of
 * graphs.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CacheHandler implements DataObjectHandler {
  /**
   * The delegate handler performing all operations when the objects are not in
   * the cache.
   */
  private DataObjectHandler handler;

  /**
   * Cache containing the original data object graphs.
   */
  private Map originalsCache;

  /**
   * Cache containing the copied data object graphs.
   */
  private Map copiesCache;

  /**
   * Constuctor the class.
   * @param handler data object handler delegate.
   */
  public CacheHandler(DataObjectHandler handler) {
    this.handler = handler;
    originalsCache = new HashMap();
    copiesCache = new HashMap();
  }

  /**
   * Indicates if the object with the given identifier is in the copies cache.
   * @param clazz class of the inquired data object.
   * @param id identifier of the inquired data object.
   * @return true if the object is in the cache otherwise false.
   */
  public boolean isInCopiesCache(Class clazz, Integer id) {
    DataObjectId objectId = new DataObjectId(clazz, id);
    return copiesCache.containsKey(objectId);
  }

  /**
   * Indicates if the object with the given identifier is in the originals
   * cache.
   * @param clazz class of the inquired data object.
   * @param id identifier of the inquired data object.
   * @return true if the object is in the cache otherwise false.
   */
  public boolean isInOriginalsCache(Class clazz, Integer id) {
    DataObjectId objectId = new DataObjectId(clazz, id);
    return originalsCache.containsKey(objectId);
  }

  /**
   * Removes the object from the copies cache.
   * @param clazz class of the inquired data object.
   * @param id identifier of the inquired data object.
   */
  public void removeFromCopiesCache(Class clazz, Integer id) {
    DataObjectId objectId = new DataObjectId(clazz, id);
    copiesCache.remove(objectId);
  }

  /**
   * Removes the object from the originals cache.
   * @param clazz class of the inquired data object.
   * @param id identifier of the inquired data object.
   */
  public void removeFromOriginalsCache(Class clazz, Integer id) {
    DataObjectId objectId = new DataObjectId(clazz, id);
    originalsCache.remove(objectId);
  }

  /**
   * Returns a copy of the data object.
   * @param clazz class of the inquired data object.
   * @param id identifier of the inquired data object.
   * @return copy of the requested object if found otherwise null.
   */
  public DataObject getCopy(Class clazz, Integer id) {
    DataObjectId objectId = new DataObjectId(clazz, id);
    return (DataObject) copiesCache.get(objectId);
  }

  /**
   * Flushes the content of the copies cache.
   */
  public void flushCopiesCache() {
    copiesCache.clear();
  }

  /**
   * Flushes the content of the originals cache.
   */
  public void flushOriginalsCache() {
    originalsCache.clear();
  }

  public void openDatasource(String source, boolean readonly) {
    handler.openDatasource(source, readonly);
  }

  public void closeDatasource(String source) {
    handler.closeDatasource(source);
  }

  public DataObject retrieve(String datasource, Class clazz, Integer id) {
    DataObjectId objectId = new DataObjectId(clazz, id);
    DataObject object = (DataObject) originalsCache.get(objectId);
    if (object == null) {
      object = handler.retrieve(datasource, clazz, id);
      originalsCache.put(objectId, object);
      copiesCache.remove(objectId);
    }

    DataObject copy = (DataObject) copiesCache.get(objectId);
    if (copy == null) {
      copy = handler.deepCopy(clazz, object);
      copiesCache.put(objectId, copy);
    }
    return copy;
  }

  public List retrieve(String datasource, Class clazz, String constraints) {
    List copies = new ArrayList();
    List roots = handler.retrieve(datasource, clazz, constraints);
    for (Iterator i = roots.iterator(); i.hasNext();) {
      DataObject object = (DataObject) i.next();
      DataObjectId objectId = new DataObjectId(object.getClass(), object
          .getId());
      originalsCache.put(objectId, object);
      DataObject copy = handler.deepCopy(clazz, object);
      copiesCache.put(objectId, copy);
      copies.add(copy);
    }
    return copies;
  }

  public LightweightObject retrieveLw(String datasource, Class clazz, Integer id) {
    return handler.retrieveLw(datasource, clazz, id);
  }

  public List retrieveLw(String datasource, Class clazz, String constraints) {
    return handler.retrieveLw(datasource, clazz, constraints);
  }

  public List store(String datasource, Class clazz, DataObject root) {
    List infos = handler.store(datasource, clazz, root);
    DataObjectId objectId = new DataObjectId(root.getClass(), root.getId());
    copiesCache.put(objectId, root);
    originalsCache.put(objectId, handler.deepCopy(clazz, root));
    return infos;
  }

  public void remove(String datasource, Class clazz, DataObject root) {
    DataObjectId objectId = new DataObjectId(root.getClass(), root.getId());
    copiesCache.remove(objectId);
    originalsCache.remove(objectId);
    handler.remove(datasource, clazz, root);
  }

  public void remove(String datasource, Class clazz, Integer id) {
    DataObjectId objectId = new DataObjectId(clazz, id);
    copiesCache.remove(objectId);
    originalsCache.remove(objectId);
    handler.remove(datasource, clazz, id);
  }

  public DataObject deepCopy(Class clazz, DataObject object) {
    return handler.deepCopy(clazz, object);
  }

  public void register(DataObjectType type) {
    handler.register(type);
  }

  public DataObjectType getType(DataObject object) {
    return handler.getType(object);
  }
}