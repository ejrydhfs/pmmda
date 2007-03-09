/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 * The explorer tree defines a tree of explorer nodes. The node type factory is
 * used to select the icons and to retrieve the name of the nodes.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerTree {
  /**
   * The selected node which popup menu could be display. Because a popup menu
   * is modal at most one such node exist for one application. The number of
   * explorer view instances is not relevant.
   */
  private static ExplorerNode selectedNodeForPopup;

  /**
   * The tree model displayed in the explorer.
   */
  private DefaultTreeModel treeModel;

  /**
   * Swing component of the explorer view used to display the tree model.
   */
  private JTree tree;

  /**
   * Root node of the displayed graph.
   */
  private ExplorerNode root;
  
  @Override public String toString(){
	  return tree.toString();
  }

  /**
   * Resets the current selected node used to generate the popup menu. To reset
   * the value, the node parameter can be set to null.
   * @param node new selected node of the explorer
   * @see #getSelectedNodeForPopup()
   */
  public static void setSelectedNodeForPopup(ExplorerNode node) {
    selectedNodeForPopup = node;
  }

  /**
   * Returns the current selected node uded to generate the popup menu.
   * @return selected node if existing otherwise null
   * @see #setSelectedNodeForPopup(ExplorerNode)
   */
  public static ExplorerNode getSelectedNodeForPopup() {
    return selectedNodeForPopup;
  }

  /**
   * Returns the tree of the explorer.
   * @return the graphical tree component of the explorer
   */
  public JTree getTree() {
    return tree;
  }

  public DefaultTreeModel getTreeModel() {
    return treeModel;
  }

  /**
   * Returns the root of the explorer tree.
   * @return root of the explorer if defined otherwise null
   * @see #setRoot(ExplorerNode, ExplorerNodeOwner)
   */
  public ExplorerNode getRoot() {
    return root;
  }

  /**
   * Sets the root of the explorer tree.
   * @param root new root of the explorer tree
   * @param owner owner of the explorer tree
   * @see #getRoot()
   */
  public void setRoot(ExplorerNode root, ExplorerNodeOwner owner) {
    ExplorerNode currentRoot = getRoot();
    if (currentRoot != null) {
      // currentRoot.setNodeOwner(null);
      selectedNodeForPopup = null;
    }
    this.root = root;
    root.setNodeOwner(owner);
    treeModel = new DefaultTreeModel(root);
    tree = new JTree(treeModel);
    tree.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setCellRenderer(new ExplorerTreeCellRenderer());
  }
}
