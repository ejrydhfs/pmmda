/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import java.util.*;

import ch.bbv.application.Locator;

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
 * graphs. The caches are controlled through flags specified in the constructor.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CacheHandler implements DataObjectHandler, CachesHandler {
  /**
   * The delegate handler performing all operations when objects are not in the
   * cache.
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
   * Flag indicating if data object graphs should be cached.
   */
  private boolean cachedEnabled;

  /**
   * Flag indicating if copies of data object graphs should be cached.
   */
  private boolean copiedEnabled;

  /**
   * Constuctor the class. All caches are disabled.
   * @param handler data object handler delegate.
   * @pre handler != null
   */
  public CacheHandler(DataObjectHandler handler) {
    this(handler, false, false);
  }

  /**
   * Constructor of the class.
   * @param handler data object handler delegate.
   * @param cachedEnabled flag indicating if data object graphs are cached.
   * @param copiedEnabled flag indicating if one copy of graphs should be
   *        cached.
   * @pre handler != null
   */
  public CacheHandler(DataObjectHandler handler, boolean cachedEnabled,
      boolean copiedEnabled) {
    assert (handler != null);
    this.handler = handler;
    this.cachedEnabled = cachedEnabled;
    this.copiedEnabled = copiedEnabled;
    originalsCache = new HashMap();
    copiesCache = new HashMap();
  }

  /**
   * Indicates if the object with the given identifier is in the copies cache.
   * @param clazz class of the inquired data object.
   * @param id identifier of the inquired data object.
   * @return true if the object is in the cache otherwise false.
   * @pre (clazz != null) && (id != null)
   */
  public boolean isInCopiesCache(Class clazz, Integer id) {
    assert ((clazz != null) && (id != null));
    boolean isInCopiesCache = false;
    if (copiedEnabled) {
      DataObjectId objectId = new DataObjectId(clazz, id);
      isInCopiesCache = copiesCache.containsKey(objectId);
    }
    return isInCopiesCache;
  }

  /**
   * Indicates if the object with the given identifier is in the originals
   * cache.
   * @param clazz class of the inquired data object.
   * @param id identifier of the inquired data object.
   * @return true if the object is in the cache otherwise false.
   * @pre (clazz != null) && (id != null)
   */
  public boolean isInOriginalsCache(Class clazz, Integer id) {
    assert ((clazz != null) && (id != null));
    boolean isInOriginalsCache = false;
    if (cachedEnabled) {
      DataObjectId objectId = new DataObjectId(clazz, id);
      isInOriginalsCache = originalsCache.containsKey(objectId);
    }
    return isInOriginalsCache;
  }

  /**
   * Returns the data object copy stored in the cache.
   * @param clazz class of the inquired data object.
   * @param id identifier of the inquired data object.
   * @return copy of the requested object if found otherwise null.
   * @pre (clazz != null) && (id != null) && isCopiedEnabled()
   */
  public DataObject getCopy(Class clazz, Integer id) {
    assert ((clazz != null) && (id != null) && isCopiedEnabled());
    DataObjectId objectId = new DataObjectId(clazz, id);
    return (DataObject) copiesCache.get(objectId);
  }

  /**
   * Flushes the content of the copies cache.
   */
  public void clearCopies() {
    copiesCache.clear();
  }

  /**
   * Flushes the content of the originals cache.
   */
  public void clearOriginals() {
    originalsCache.clear();
  }

  public void openDatasource(String datasource, boolean readonly) {
    assert (datasource != null);
    handler.openDatasource(datasource, readonly);
  }

  public void closeDatasource(String datasource) {
    assert (datasource != null);
    handler.closeDatasource(datasource);
  }

  public DataObject retrieve(String datasource, Class clazz, Integer id) {
    assert ((datasource != null) && (clazz != null) && (id != null));
    DataObject root = retrieveFromCaches(clazz, id);
    if (root == null) {
      root = handler.retrieve(datasource, clazz, id);
      root = updateCacheWithServerGraph(root);
    }
    return root;
  }

  public List retrieve(String datasource, Class clazz, String constraints) {
    assert ((datasource != null) && (clazz != null) && (constraints != null));
    List copies = new ArrayList();
    List roots = handler.retrieve(datasource, clazz, constraints);
    for (Iterator i = roots.iterator(); i.hasNext();) {
      DataObject root = (DataObject) i.next();
      copies.add(updateCacheWithClientGraph(root));
    }
    return copies;
  }

  public LightweightObject retrieveLw(String datasource, Class clazz, Integer id) {
    assert ((datasource != null) && (clazz != null) && (id != null));
    return handler.retrieveLw(datasource, clazz, id);
  }

  public List retrieveLw(String datasource, Class clazz, String constraints) {
    assert ((datasource != null) && (clazz != null));
    return handler.retrieveLw(datasource, clazz, constraints);
  }

  public List store(String datasource, Class clazz, DataObject root) {
    assert ((datasource != null) && (clazz != null) && (root != null));
    List infos = handler.store(datasource, clazz, root);
    AbstractVisitorFunctor visitor = getVisitor(clazz);
    UpdateFunctor functor = new UpdateFunctor();
    functor.reset(infos);
    visitor.setFunctor(functor);
    DataObjectType.invokeVisitor(clazz, root, visitor);
    root = updateCacheWithClientGraph(root);
    return infos;
  }

  public void remove(String datasource, Class clazz, DataObject root) {
    assert ((datasource != null) && (clazz != null) && (root != null));
    removeFromCache(root.getClass(), root.getId(), copiesCache);
    removeFromCache(root.getClass(), root.getId(), originalsCache);
    handler.remove(datasource, clazz, root);
  }

  public void remove(String datasource, Class clazz, Integer id) {
    assert ((datasource != null) && (clazz != null) && (id != null));
    handler.remove(datasource, clazz, id);
  }

  public boolean isCopiedEnabled() {
    return copiedEnabled;
  }

  public boolean isCachedEnabled() {
    return cachedEnabled;
  }

  public void removeFromCopiesCache(DataObject root) {
    assert (root != null);
    if (copiedEnabled) {
      removeFromCache(root.getClass(), root.getId(), copiesCache);
    }
  }

  public void removeFromOriginalsCache(DataObject root) {
    assert (root != null);
    if (cachedEnabled) {
      removeFromCopiesCache(root);
      removeFromCache(root.getClass(), root.getId(), originalsCache);
    }
  }

  public DataObject revert(String datasource, DataObject root) {
    removeFromCopiesCache(root);
    return retrieve(datasource, root.getClass(), root.getId());
  }

  public DataObject reload(String datasource, DataObject root) {
    removeFromOriginalsCache(root);
    return retrieve(datasource, root.getClass(), root.getId());
  }

  /**
   * Updates the cache with all root objects part of the the graph.
   * @param root root of the data object graph.
   * @param cache cache containing the data objects to remove.
   */
  private void addToCache(DataObject root, Map cache) {
    DataObjectManager manager = (DataObjectManager) Locator.getInstance()
        .getComponent(DataObjectManager.NAME);
    for (Iterator i = manager.getRoots(root).iterator(); i.hasNext();) {
      DataObject object = (DataObject) i.next();
      cache.put(new DataObjectId(object), object);
    }
  }

  /**
   * Removes all root objects part of the graph from the cache.
   * @param clazz class of the root object of the graph to remove.
   * @param id identifier of the root object.
   * @param cache cache containing the data objects to remove.
   */
  private void removeFromCache(Class clazz, Integer id, Map cache) {
    DataObjectManager manager = (DataObjectManager) Locator.getInstance()
        .getComponent(DataObjectManager.NAME);
    DataObject root = (DataObject) cache.get(new DataObjectId(clazz, id));
    if (root != null) {
      for (Iterator i = manager.getRoots(root).iterator(); i.hasNext();) {
        DataObject object = (DataObject) i.next();
        cache.remove(new DataObjectId(object));
      }
    }
  }

  /**
   * Retrieves the graph of data objects which root has the given identifier.
   * @param clazz class of the root of the data object root.
   * @param id identifier of the root of the data object root.
   * @return the requested cached graph if found otherwise null.
   */
  private DataObject retrieveFromCaches(Class clazz, Integer id) {
    DataObject root = null;
    DataObjectId objectId = new DataObjectId(clazz, id);
    if (copiedEnabled) {
      root = (DataObject) copiesCache.get(objectId);
    }
    if (cachedEnabled && (root == null)) {
      root = (DataObject) originalsCache.get(objectId);
      if (copiedEnabled && (root != null)) {
        // handles the case where the copies cache was flushed.
        root = root.copy();
        addToCache(root, copiesCache);
      }
    }
    return root;
  }

  /**
   * Updates the caches with all root objects part of the the graph. First the
   * old root objects are removed from the cache, second the new ones are added
   * to the cache.
   * @param root root of the data object graph. The graph was retrieved from the
   *        server.
   * @return the data object to return.
   */
  private DataObject updateCacheWithServerGraph(DataObject root) {
    DataObject copy = root;
    if (root != null) {
      if (cachedEnabled) {
        removeFromCache(root.getClass(), root.getId(), originalsCache);
        addToCache(root, originalsCache);
      }
      if (copiedEnabled) {
        copy = root.copy();
        removeFromCache(root.getClass(), root.getId(), copiesCache);
        addToCache(copy, copiesCache);
      } else {
        copy = root;
      }
    }
    return copy;
  }

  /**
   * Updates the caches with all root objects part of the the graph. First the
   * old root objects are removed from the cache, second the new ones are added
   * to the cache.
   * @param root root of the data object graph. The graph was retrieved from the
   *        server.
   * @return the data object to return.
   */
  private DataObject updateCacheWithClientGraph(DataObject root) {
    if (root != null) {
      if (cachedEnabled) {
        DataObject copy = root.copy();
        removeFromCache(copy.getClass(), copy.getId(), originalsCache);
        addToCache(copy, originalsCache);
      }
      if (copiedEnabled) {
        removeFromCache(root.getClass(), root.getId(), copiesCache);
        addToCache(root, copiesCache);
      }
    }
    return root;
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
}