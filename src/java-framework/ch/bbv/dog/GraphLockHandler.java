/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog;

import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * The graph lock manager keeps track of data object graphs and their roots. The
 * manager permits cooperative client threads to insure that changes are atomic.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface GraphLockHandler {
  /**
   * Locks all the root data objects members of the graph. The graph is
   * traversed using the collect root functor.
   * @param root root which candidate data object root objects should be locked.
   * @return list of all locked data object root identities.
   * @throws ConcurrentModificationException At least one of the data object
   *         roots was already locked.
   */
  List lockGraph(DataObject root) throws ConcurrentModificationException;

  /**
   * Locks all the root data objects members of the list of graphs. The graphs
   * are traversed using the collect root functor.
   * @param graphs graphs which candidate data object root objects should be
   *        locked.
   * @return list of all locked data object root identities.
   * @throws ConcurrentModificationException At least one of the data object
   *         roots was already locked.
   */
  List lockGraphs(List graphs) throws ConcurrentModificationException;

  /**
   * Lock the data object identity.
   * @param id data object root identities to lock.
   * @return the list containing the identity.
   * @throws ConcurrentModificationException At least one of the data object
   *         roots was already locked.
   */
  List lockRootId(DataObjectId id) throws ConcurrentModificationException;

  /**
   * Locks all the data object identities.
   * @param ids data object root identities to lock.
   * @return the list of identities passed as parameters.
   * @throws ConcurrentModificationException At least one of the data object
   *         roots was already locked.
   */
  List lockRootIds(List ids) throws ConcurrentModificationException;

  /**
   * Unlocks all the given data object root identities.
   * @param rootIds identifiers of the root objects to unlock.
   */
  void unlockGraphs(List rootIds);
}