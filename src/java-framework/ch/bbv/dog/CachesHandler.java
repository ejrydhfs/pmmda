/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface CachesHandler {
  /**
   * Returns true if the data object graph copies cache is enabled.
   * @return true if the cache is enabled otherwise false.
   */
  boolean isCopiedEnabled();

  /**
   * Returns true if the data object graph cache is enabled.
   * @return true if the cache is enabled otherwise false.
   */
  boolean isCachedEnabled();

  /**
   * Removes the object from the copies cache.
   * @param root root of the data object graph to remove.
   * @pre root != null
   */
  void removeFromCopiesCache(DataObject root);

  /**
   * Removes the object from the originals and copied caches.
   * @param root root of the data object graph to remove.
   * @pre root != null
   */
  void removeFromOriginalsCache(DataObject root);

  /**
   * Reverts to the original data object graph. If the data object handler has
   * copies cache activated the new copy replaces the old one. Care should be
   * taken to inform all interested parties that the official copy of the graph
   * has changed.
   * @param datasource name of the datasource.
   * @param root root of the data object graph to revert.
   * @return the new copy of the data object graph.
   */
  DataObject revert(String datasource, DataObject root);

  /**
   * Reloads the data object graph from the underlying layer. If the data object
   * handler has caches the newly reloaded graph replaces the old one. Care
   * should be taken to inform all interested parties that the official copy of
   * the graph has changed.
   * @param datasource name of the datasource.
   * @param root root of the data object graph to revert.
   * @return the data object graph retrieved from the underlying layer.
   */
  DataObject reload(String datasource, DataObject root);

}