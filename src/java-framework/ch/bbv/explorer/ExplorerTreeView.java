/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;

/**
 * The explorer tree defines a tree of explorer nodes and the view to display
 * it.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerTreeView {
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
   * The name of the explorer tree.
   */
  private String name;

  /**
   * The selection listener for the explorer frame.
   */
  private ExplorerViewTreeSelectionListener selectionListener;

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

  public ExplorerTreeView(String name) {
    this.name = name;
    selectionListener = new ExplorerViewTreeSelectionListener();
    pane = new JScrollPane(tree);
    pane.setName(name);
  }

  public String getName() {
    return name;
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

  public JComponent getPane() {
    return pane;
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
      currentRoot.setNodeOwner(null);
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
    tree.addTreeSelectionListener(selectionListener);
    pane.setViewportView(tree);
  }

  /**
   * Implements the mouse listener for the explorer view.
   * @author Marcel Baumann
   * @version $Revision$
   */
  private final class ExplorerMouseListener extends MouseAdapter {

    /**
     * Checks if the mouse event should activate the display of the popup menu
     * upon a mouse clicked. If yes displays the popup menu.
     * @param event mouse event to handle
     */
    @Override
    public void mouseReleased(MouseEvent event) {
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

  /**
   * Implements the tree selection listener for the explorer view.
   * @author Marcel Baumann
   * @version $Revision$
   */
  private final class ExplorerViewTreeSelectionListener implements
      TreeSelectionListener {
    private ExplorerNode currentNode;

    /**
     * The views currently displayed for the selected node.
     */
    private List<NodeView> views;

    /**
     * Reacts on a tree selection event. Each time a node is selected its
     * children are displayed in the table view. Care is taken to save the
     * header visual structure onto the node type and to avoid unnecessary
     * updates.
     * @param event tree selection event sent by the JTree component
     */
    public void valueChanged(TreeSelectionEvent event) {
      ExplorerNode node = (ExplorerNode) getTree()
          .getLastSelectedPathComponent();
      if (node != null) {
        if ((currentNode == null)
            || (node.getNodeType() != currentNode.getNodeType())) {
          displayViews(node);
          currentNode = node;
        } else {
          for (NodeView view : views) {
            view.setModel(node);
          }
        }
      }
    }

    /**
     * Displays the data object members of the node with the provided view type.
     * @param node node which data object should be displayed
     */
    public void displayViews(ExplorerNode node) {
      Explorer explorer = (Explorer) node.getNodeOwner();
      List<NodeView> newViews = explorer.getFactory().getNodeViews(node);
      if ((views != null) && !views.containsAll(newViews)) {
        for (NodeView view : views) {
          DockingManager.undock((JComponent) view);
        }
      }
      views = newViews;
      for (NodeView view : newViews) {
        Dockable dockable = DockingManager.registerDockable((JComponent) view,
            view.getName());
        DockingManager.dock(dockable, explorer.getMainPort(),
            DockingConstants.CENTER_REGION);
      }
    }
  }
}
