/*
 * copyright 2000-2005 Marcel Baumann 
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
 * Expands all the nodes below the node to process.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExpandNodeAction extends AbstractNodeAction {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Constructor of the class.
   * @param type node type for which the action is defined.
   * @param name name of the action. If the action is an add, the name must be
   *          the name of the property.
   */
  public ExpandNodeAction(UiNodeType type, String name) {
    super(type, name, false);
  }

  public void execute(ExplorerNode node) {
    expand(node, node.getExplorer().getTree());
  }

  private void expand(ExplorerNode node, JTree tree) {
    int numberOfNodes = node.getChildCount();
    for (int i = 0; i < numberOfNodes; i++) {
      expand((ExplorerNode) node.getChildAt(i), tree);
    }
    TreePath treePath = new TreePath(node.getPath());
    if (treePath.getParentPath() != null) {
      tree.expandPath(treePath);
    }
  }

}