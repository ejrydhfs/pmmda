/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer;

import javax.swing.JPopupMenu;

/**
 * The popup menu factory provides popup menus for displayed nodes. The menus
 * are selected based on the node type and state information of the node. Custom
 * configuration is available by registring menu factory for a specific node
 * type.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface PopupFactory {
  /**
   * Register the selector responsible to return the icon for all instances of
   * the given node type.
   * @param nodeType node type handled by the given icon selector;
   * @param selector popup menu selector for the node type.
   * @pre (nodeType != null) && (selector != null)
   */
  void registerPopupSelector(UiNodeType nodeType, PopupSelector selector);

  /**
   * Selects the popup menu for the given explorer node.
   * @param node node which icon is requested.
   * @return the rquested popup menu if defined otherwise null.
   */
  JPopupMenu getMenu(ExplorerNode node);
}