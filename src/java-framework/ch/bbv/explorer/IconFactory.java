/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import javax.swing.ImageIcon;

/**
 * The icon factory provides icons for displayed nodes. The icons are selected
 * based on the node type and state information of the node. Custom
 * configuration is available by registring icon selectors for a specific node
 * type.
 * @author Marcel Baumann
 * @version $Revision$
 */
public interface IconFactory {
  /**
   * Register the selector responsible to return the icon for all instances of
   * the given node type.
   * @param nodeType node type handled by the given icon selector;
   * @param selector icon selector for the node type.
   */
  void registerIconSelector(UiNodeType nodeType, IconSelector selector);

  /**
   * Returns the icon associated with the given object. The object is described
   * in this node type.
   * @param node explorer node which icon should be inferred.
   * @param isExpanded indicates if the node is expanded or not.
   * @return the icon representing the object and its state.
   * @pre object != null
   */
  ImageIcon getSmallIconFor(ExplorerNode node, boolean isExpanded);

  /**
   * Returns the icon with the given resource identifier.
   * @param identifier resource identifier of the icon.
   * @return the requested icon if found otherwise null.
   */
  ImageIcon getSmallIconFor(String identifier);

  /**
   * Flushes all icons from the cache. Upon flushing newly requested icons are
   * fetched from the resources.
   */
  void flushIconsCache();
}