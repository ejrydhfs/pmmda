/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

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
  private Set locks;

  /**
   * Constructor of the class.
   * @param manager manager responsible of the data objects to lock.
   */
  public GraphLockHandlerImpl(DataObjectManager manager) {
    this.manager = manager;
    functor = new CollectRootFunctor(manager);
    locks = new HashSet();
  }

  public List lockGraph(DataObject root) throws ConcurrentModificationException {
    List roots = new ArrayList();
    roots.add(root);
    return lockGraphs(roots);
  }

  public synchronized List lockGraphs(List graphs)
      throws ConcurrentModificationException {
    List lockedObjects = new ArrayList();
    List ids = null;
    for (Iterator i = graphs.iterator(); i.hasNext();) {
      DataObject root = (DataObject) i.next();
      List subroots = collectRoots(root);
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

  public List lockRootId(DataObjectId id)
      throws ConcurrentModificationException {
    List ids = new ArrayList();
    ids.add(id);
    return lockRootIds(ids);
  }

  public synchronized List lockRootIds(List identities)
      throws ConcurrentModificationException {
    List ids = null;
    if (!identities.isEmpty()) {
      ids = identities;
      locks.addAll(ids);
    }
    return ids;
  }

  public synchronized void unlockGraphs(List roots) {
    locks.removeAll(roots);
  }

  /**
   * collects all the roots existing in the given graph.
   * @param root root of the graph to parse.
   * @return list of all found root objects part of the graph.
   */
  private final List collectRoots(DataObject root) {
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

  private List createIds(List objects) {
    List ids = new ArrayList();
    Iterator objs = objects.iterator();
    while (objs.hasNext()) {
      DataObject object = (DataObject) objs.next();
      if ((object != null) && (object.getId() != null)) {
        ids.add(new DataObjectId(object));
      }
    }
    return ids;
  }
}