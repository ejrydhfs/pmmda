/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import ch.bbv.dog.DataObject;

/**
 * The tree cell renderer implements how the icons of the explorer nodes are
 * displayed in the left view of the explorer.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ExplorerTreeCellRenderer extends DefaultTreeCellRenderer {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Formats for the tree label: The first part is not-loaded, loaded, modified
   * for editable nodes, the second part is for read-only nodes.
   */
  private static final String[][] formats = { { "<html><i>", "</i></html>" },
      { "", "" }, { "<html><b>", "</b></html>" },
      { "<html><i><font color=grey>", "</i></font></html>" },
      { "<html><font color=grey>", "</font></html>" },
      { "<html><b><font color=grey>", "</b></font></html>" } };

  /**
   * Offset in the formats structure to reach the read-only definitions.
   */
  private static final int READ_ONLY_OFFSET = 3;

  /**
   * buffer used to minimize the cost of string operations and of object
   * creation.
   */
  private StringBuilder buffer;

  /**
   * Default constructor of the class.
   */
  public ExplorerTreeCellRenderer() {
    buffer = new StringBuilder();
  }

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
    setText(explorerNode);
    return this;
  }

  /**
   * Sets the text of the label based on the status of the node.
   * @param node node which text label should be computed.
   */
  private void setText(ExplorerNode node) {
    DataObject object = node.getDataObject();
    int index;
    if (object == null) {
      index = 0;
    } else if (object.isModified()) {
      index = 2;
    } else {
      index = 1;
    }
    if (!node.getNodeType().isEditable(node.getLightweightObject())) {
      index += READ_ONLY_OFFSET;
    }
    buffer.setLength(0);
    buffer.append(formats[index][0]).append(node.getName()).append(
        formats[index][1]);
    setText(buffer.toString());
  }
}