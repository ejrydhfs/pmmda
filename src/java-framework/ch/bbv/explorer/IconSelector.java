/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.explorer;

import javax.swing.ImageIcon;

/**
 * The icon selector defines the interface for all icon selectors. An icon
 * selector defines which icon should be associated with a tree node in the
 * navigation panel.
 * @author marcelbaumann
 * @version $Revision$
 */
public interface IconSelector {
  /**
   * Selects the image icon for the given explorer node.
   * @param node node which icon is requested.
   * @param expanded indicates if the node is expanded or not.
   * @return the rquested image icon if defined otherwise null.
   */
  ImageIcon select(ExplorerNode node, boolean expanded);
}