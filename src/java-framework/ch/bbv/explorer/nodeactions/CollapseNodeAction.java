/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.nodeactions;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;

/**
 * Collapses all the nodes below the node to process.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CollapseNodeAction extends AbstractNodeAction {
  /**
   * Unique serial version identifier for the instances of the class.
   */
  private static final long serialVersionUID = -8025394103384580024L;

  /**
   * Constructor of the class.
   * @param type node type for which the action is defined
   * @param name name of the action. If the action is an add, the name must be
   *          the name of the property
   */
  public CollapseNodeAction(UiNodeType type, String name) {
    super(type, name, false);
  }

  @Override
  public void execute(ExplorerNode node) {
    collapse(node, node.getNodeOwner().getTree());
  }

  /**
   * Collapses the tree from the given node.
   * @param node node which children should be collapsed
   * @param tree Swing tree to collapse
   */
  private void collapse(ExplorerNode node, JTree tree) {
    int numberOfNodes = node.getChildCount();
    for (int i = 0; i < numberOfNodes; i++) {
      collapse((ExplorerNode) node.getChildAt(i), tree);
    }
    TreePath treePath = new TreePath(node.getPath());
    tree.collapsePath(treePath);
  }
}