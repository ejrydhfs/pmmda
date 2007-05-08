/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;

/**
 * The explorer tree defines a tree of explorer nodes and the view to display
 * it.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerTreeView extends ExplorerTree{

  /**
   * The name of the explorer tree.
   */
  private String name;

  /**
   * The selection listener for the explorer frame.
   */
  private ExplorerViewTreeSelectionListener selectionListener;


  /**
   * Constructor
   * @param name the name of the exlorer tree
   */
  public ExplorerTreeView(String name) {
    super();
	this.name = name;
    selectionListener = new ExplorerViewTreeSelectionListener();
    getPane().setName(name);
  }

  /**
   * Returns the explorer trees name
   * @return the explorer trees name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the root of the explorer tree.
   * @param root new root of the explorer tree
   * @param owner owner of the explorer tree
   * @see #getRoot()
   */
  @Override
  public void setRoot(ExplorerNode root, ExplorerNodeOwner owner) {
    super.setRoot(root, owner);
    getTree().addTreeSelectionListener(selectionListener);    
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
