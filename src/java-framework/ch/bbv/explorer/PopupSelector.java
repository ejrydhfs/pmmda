/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer;

import javax.swing.JPopupMenu;

/**
 * The popup menu selector defines the interface for all popup menu selectors. A
 * popup menu selector defines which menu should be associated with a tree node
 * in the navigation panel.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface PopupSelector {
  /**
   * Selects the popup menu for the given explorer node.
   * @param node node which icon is requested.
   * @return the requested popup menu if defined otherwise null.
   * @pre (node != null)
   */
  JPopupMenu select(ExplorerNode node);
}