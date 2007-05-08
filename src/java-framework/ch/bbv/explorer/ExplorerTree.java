/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
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
   * Scroll pane containing the tree view.
   */
  private JScrollPane pane;


  /**
   * Root node of the displayed graph.
   */
  private ExplorerNode root;
  
 /**
  * Constructs a new Exlorer Tree
  *
  */ 
  public ExplorerTree(){
	  pane = new JScrollPane(tree);
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
  
  /**
   * Retruns the tree in a scroll pane
   * @return the tree in a scroll pane
   */
  public JComponent getPane() {
	    return pane;
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
      selectedNodeForPopup = null;
    }
    this.root = root;
    root.setNodeOwner(owner);
    treeModel = new DefaultTreeModel(root);
    tree = new JTree(treeModel);
    
    tree.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setCellRenderer(new ExplorerTreeCellRenderer());
    
    tree.addMouseListener(new ExplorerMouseListener());
    pane.setViewportView(tree);
  }
  
  @Override public String toString(){
	  return tree.toString();
  }
  
  /**
   * Implements the mouse listener for the explorer view. Both mouse pressed and
   * released events are handled for portability reasons. For example under
   * Windows the trigger is set on released, under OS X it is set on pressed.
   * @author Marcel Baumann
   * @version $Revision$
   */
  private final class ExplorerMouseListener extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent event) {
      evaluatePopup(event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
      evaluatePopup(event);
    }

    private void evaluatePopup(MouseEvent event) {
      if (event.isPopupTrigger()) {
        TreePath path = getTree()
            .getPathForLocation(event.getX(), event.getY());
        if (path != null) {
          getTree().setSelectionPath(path);
          ExplorerNode node = (ExplorerNode) path.getLastPathComponent();
          JPopupMenu menu = node.getNodeType().getFactory().getPopupFactory()
              .getMenu(node);
          if (menu != null) {
            ExplorerTreeView.setSelectedNodeForPopup(node);
            menu.show(getTree(), event.getX(), event.getY());
          }
        }
      }
    }
  }
}
