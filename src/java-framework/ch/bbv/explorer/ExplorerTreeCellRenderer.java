/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * The tree cell renderer implements how the icons of the explorer nodes are
 * displayed in the left view of the explorer.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ExplorerTreeCellRenderer extends DefaultTreeCellRenderer {

  /**
   * Returns the tree cell renderer component for this renderer.
   * @param tree tree using the renderer.
   * @param node node to be rendered.
   * @param selected true if the node is selected otherwise false.
   * @param expanded true if the node is expanded otherwise false.
   * @param leaf true if the node is a leaf otherwise false.
   * @param row row of the node
   * @param hasFocus true if the node has focus otherwise false.
   * @return the tree cell renderer component to display.
   */
  public Component getTreeCellRendererComponent(JTree tree, Object node,
      boolean selected, boolean expanded, boolean leaf, int row,
      boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, node, selected, expanded, leaf,
        row, hasFocus);
    ExplorerNode explorerNode = (ExplorerNode) node;
    IconFactory iconFactory = explorerNode.getExplorer().getNodeTypeFactory()
        .getIconFactory();
    ImageIcon icon = iconFactory.getSmallIconFor(explorerNode, expanded);
    if (icon != null) {
      setIcon(icon);
    }
    return this;
  }
}