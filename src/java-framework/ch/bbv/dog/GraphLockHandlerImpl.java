/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This implementation of graph lock handler is only optimal for an application
 * not being clustered. The class is thread safe and can be used in managed and
 * unmanaged environement.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class GraphLockHandlerImpl implements GraphLockHandler {
  /**
   * Logger of the class.
   */
  protected static Log log = LogFactory.getLog(GraphLockHandlerImpl.class);

  /**
   * Data object manager of the objects handled in the lock handler.
   */
  private DataObjectManager manager;

  /**
   * Functor used to collect all root objects of a graph.
   */
  private CollectRootFunctor functor;

  /**
   * Set of all locks currently hold in the application.
   */
  private Set<DataObjectId> locks;

  /**
   * Constructor of the class.
   * @param manager manager responsible of the data objects to lock.
   */
  public GraphLockHandlerImpl(DataObjectManager manager) {
    this.manager = manager;
    functor = new CollectRootFunctor(manager);
    locks = new HashSet<DataObjectId>();
  }

  public List<DataObjectId> lockGraph(DataObject root)
      throws ConcurrentModificationException {
    List<DataObject> roots = new ArrayList<DataObject>();
    roots.add(root);
    return lockGraphs(roots);
  }

  public synchronized List<DataObjectId> lockGraphs(List<DataObject> graphs)
      throws ConcurrentModificationException {
    List<DataObject> lockedObjects = new ArrayList<DataObject>();
    List<DataObjectId> ids = null;
    for (DataObject root : graphs) {
      List<DataObject> subroots = collectRoots(root);
      if (!locks.containsAll(subroots)) {
        lockedObjects.clear();
        break;
      }
      lockedObjects.addAll(subroots);
    }
    if (!lockedObjects.isEmpty()) {
      ids = createIds(lockedObjects);
      locks.addAll(ids);
    }
    return ids;
  }

  public List<DataObjectId> lockRootId(DataObjectId id)
      throws ConcurrentModificationException {
    List<DataObjectId> ids = new ArrayList<DataObjectId>();
    ids.add(id);
    return lockRootIds(ids);
  }

  public synchronized List<DataObjectId> lockRootIds(
      List<DataObjectId> identities) throws ConcurrentModificationException {
    List<DataObjectId> ids = null;
    if (!identities.isEmpty()) {
      ids = identities;
      locks.addAll(ids);
    }
    return ids;
  }

  public synchronized void unlockGraphs(List<DataObjectId> rootIds) {
    locks.removeAll(rootIds);
  }

  /**
   * collects all the roots existing in the given graph.
   * @param root root of the graph to parse.
   * @return list of all found root objects part of the graph.
   */
  private final List<DataObject> collectRoots(DataObject root) {
    functor.reset();

    DataObjectType type = manager.getType(root);
    if (type != null) {
      AbstractVisitorFunctor visitor = type.createVisitorInstance();
      visitor.setFunctor(functor);
      DataObjectType.invokeVisitor(root, visitor);
    } else {
      if (log.isDebugEnabled()) {
        log.debug("root data object type unknown " + root.getClass());
      }
    }
    return functor.getRoots();
  }

  private List<DataObjectId> createIds(List<DataObject> objects) {
    List<DataObjectId> ids = new ArrayList<DataObjectId>();
    for (DataObject object : objects) {
      if ((object != null) && (object.getId() != null)) {
        ids.add(new DataObjectId(object));
      }
    }
    return ids;
  }
}