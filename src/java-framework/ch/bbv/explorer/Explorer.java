/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ch.bbv.dog.*;
import ch.bbv.explorer.components.DockingArea;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The explorer view is responsible to visualize hierarchical data in an
 * explorer window. The left pane contains a tree of the data and the right pane
 * contains a table view of the children of the currently selected node. All
 * node and node types are created with the help of a private factory. Each
 * instance of an explorer view has an own factory. Special care was taken to
 * optimize the visualization of the tables and to conserve the header
 * definition of the various node types. The table header is stored on the
 * associated node type. The creation of an explorer based client has three
 * phases.
 * <ol>
 * <li>The constructor creates an instance of the class. The constructors of
 * the ancestors are called.</li>
 * <li>The initialize method is called starting from the leaf descendent. Each
 * subclass, which overwrites the method must call the parent method.</li>
 * <li>The display method shows the explorer on the screen.</li>
 * </ol>
 * <p>
 * The right pane provide two panels. The top panel provides a detailed view of
 * the selected node in the navigator. The bottom panel is a docking panel where
 * additional information view can be displayed.
 * </p>
 * @author Marcel Baumann
 * @version $Revision$
 */
public abstract class Explorer {
  /**
   * The selected node which popup menu could be display. Because a popup menu
   * is modal at most one such node exist for one application. The number of
   * explorer view instances is not relevant.
   */
  private static ExplorerNode selectedNodeForPopup;

  private ExplorerNode root;

  /**
   * The frame of the explorer view.
   */
  private JFrame frame;

  /**
   * Swing component of the explorer view.
   */
  private JSplitPane splitPane;

  /**
   * Swing component of the explorer view.
   */
  private JScrollPane leftPane;

  /**
   * Swing component of the explorer view.
   */
  private JSplitPane rightPane;

  /**
   * Swing component of the explorer view.
   */
  private JScrollPane topRightPane;

  /**
   * Swing component of the explorer view.
   */
  private JScrollPane bottomRightPane;

  /**
   * Swing component of the explorer view.
   */
  private DockingArea dockingArea;

  /**
   * Status bar of the explorer view.
   */
  private StatusBar statusBar;

  /**
   * Swing component of the explorer view used to display the tree model.
   */
  private JTree tree;

  private NodeView nodeView;

  /**
   * The tree model displayed in the explorer.
   */
  private DefaultTreeModel treeModel;

  /**
   * The factory of user interface type used to create all nodes in this
   * explorer.
   */
  private UiNodeTypeFactory factory;

  /**
   * The handler of the lightweight and data objects.
   */
  private DataObjectManager manager;

  /**
   * Data source used to retrieve the data objects.
   */
  private String datasource;

  /**
   * The menu bar of the explorer frame.
   */
  private ExplorerMenuBar menuBar;

  /**
   * The task class implements the Swing pattern where graphical user interface
   * should be executed in the AWT event processing thread.
   * @author MarcelBaumann
   * @version $Revision$
   */
  private static class GuiTask implements Runnable {
    private Explorer explorer;

    /**
     * Constructor of the class. The constructor of the class with the given
     * parameters is called.
     * @param clazz class of the explorer to instantiate.
     * @param types signature of the constructur to call.
     * @param parameters parameters of the constructor to call.
     */
    public GuiTask(Class clazz, Class[] types, Object[] parameters) {
      if (parameters == null) {
        explorer = (Explorer) ReflectUtilities.newInstance(clazz);
      } else {
        explorer = (Explorer) ReflectUtilities.newInstance(clazz, types,
            parameters);
      }
    }

    public void run() {
      explorer.initialize();
      explorer.display();
    }

    /**
     * Returns the instantiated explorer.
     * @return the newly created explorer.
     */
    public Explorer getExplorer() {
      return explorer;
    }
  }

  /**
   * Instantiates a new explorer and runs it in the context of the AWT event
   * processing thread as requested in Swing best practices.
   * @param clazz class of the explorer to create.
   * @param types signature of the constructur to call.
   * @param parameters parameters of the constructor to call.
   * @return the newly created explorer.
   */
  public static Explorer createAndShow(Class clazz, Class[] types,
      Object[] parameters) {
    GuiTask task = new GuiTask(clazz, types, parameters);
    SwingUtilities.invokeLater(task);
    return task.getExplorer();
  }

  /**
   * Resets the current selected node used to generate the popup menu. To reset
   * the value, the node parameter can be set to null.
   * @param node new selected node of the explorer.
   * @see #getSelectedNodeForPopup()
   */
  public static void setSelectedNodeForPopup(ExplorerNode node) {
    selectedNodeForPopup = node;
  }

  /**
   * Returns the current selected node uded to generate the popup menu.
   * @return selected node if existing otherwise null.
   * @see #setSelectedNodeForPopup(ExplorerNode)
   */
  public static ExplorerNode getSelectedNodeForPopup() {
    return selectedNodeForPopup;
  }

  /**
   * Constructor of the class.
   * @param factory node type factory of the explorer.
   * @param manager data object manager of the explorer.
   * @param datasource name of the datasource used to access persistent data.
   */
  public Explorer(UiNodeTypeFactory factory, DataObjectManager manager,
      String datasource) {
    this.factory = factory;
    this.manager = manager;
    this.datasource = datasource;
    frame = new JFrame("Explorer");
    leftPane = new JScrollPane(tree);
    topRightPane = new JScrollPane();
    bottomRightPane = new JScrollPane();
    dockingArea = new DockingArea();
    bottomRightPane.setViewportView(dockingArea);
    rightPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topRightPane,
        bottomRightPane);
    rightPane.setDividerSize(5);
    rightPane.setResizeWeight(0.8);
    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, rightPane);
    splitPane.setDividerSize(5);
    splitPane.setResizeWeight(0.3);
    frame.getContentPane().add(BorderLayout.CENTER, splitPane);
    statusBar = new StatusBar();
    frame.getContentPane().add(BorderLayout.SOUTH, statusBar);
    frame.addWindowListener(new ExplorerWindowListener(this));
  }

  /**
   * Returns the user interface node type factory of the explorer.
   * @return the factory of the explorer.
   */
  public UiNodeTypeFactory getNodeTypeFactory() {
    return factory;
  }

  /**
   * Returns the data object manager used in the explorer.
   * @return the data object manager used in the explorer.
   */
  public DataObjectManager getManager() {
    return manager;
  }

  /**
   * Returns the datasource of the tree of data objects displayed in the
   * explorer.
   * @return the data source used in the explorer.
   */
  public String getDatasource() {
    return datasource;
  }

  /**
   * Sets the title of the explorer frame.
   * @param title new title of the explorer.
   * @pre title != null
   */
  public void setTitle(String title) {
    assert (title != null);
    frame.setTitle(title);
  }

  /**
   * Returns the docking area of the explorer.
   * @return the docking area of the explorer.
   */
  public DockingArea getDockingArea() {
    return dockingArea;
  }
  
  /**
   * Returns the menu bar of the explorer.
   * @return the menu bar of the explorer.
   * @see #setMenuBar(ExplorerMenuBar)
   */
  public ExplorerMenuBar getMenuBar() {
    return menuBar;
  }

  /**
   * Returns the status bar of the explorer.
   * @return the status bar of the explorer.
   */
  public StatusBar getStatusBar() {
    return statusBar;
  }

  /**
   * Sets the menu bar of the explorer.
   * @param menuBar the new menu bar of the explorer.
   * @see #getMenuBar()
   */
  public void setMenuBar(ExplorerMenuBar menuBar) {
    this.menuBar = menuBar;
    frame.setJMenuBar(menuBar);
  }

  /**
   * Returns the frame containing the explorer.
   * @return the frame containing the explorer panel.
   */
  public JFrame getFrame() {
    return frame;
  }

  /**
   * Returns the tree of the explorer.
   * @return the graphical tree component of the explorer.
   */
  public JTree getTree() {
    return tree;
  }

  /**
   * Returns the root of the explorer tree.
   * @return root of the explorer if defined otherwise null.
   * @see #setRoot(ExplorerNode)
   */
  public ExplorerNode getRoot() {
    return root;
  }

  /**
   * Sets the root of the explorer tree.
   * @param root new root of the explorer tree.
   * @see #getRoot()
   */
  public void setRoot(ExplorerNode root) {
    ExplorerNode currentRoot = getRoot();
    if (currentRoot != null) {
      currentRoot.setExplorer(null);
      selectedNodeForPopup = null;
    }
    this.root = root;
    root.setExplorer(this);
    initializeRoot();
    displayCurrentView(root);
  }

  /**
   * Displays the explorer window.
   */
  void display() {
    frame.setLocation(100, 100);
    frame.pack();
    frame.setSize(800, 600);
    splitPane.setDividerLocation(-1);
    frame.setVisible(true);
  }

  /**
   * Displays the frame and its child.
   */
  protected void initializeRoot() {
    treeModel = new DefaultTreeModel(root);
    tree = new JTree(treeModel);
    tree.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setCellRenderer(new ExplorerTreeCellRenderer());
    tree.addTreeSelectionListener(new ExplorerViewTreeSelectionListener());
    tree.addMouseListener(new ExplorerMouseListener());
    leftPane.setViewportView(tree);
  }

  /**
   * Called each type a node is inserted.
   * @param node inserted child node in this node.
   * @param index index of the inserted child node.
   */
  public void nodeInserted(ExplorerNode node, int index) {
    int[] childIndices = new int[1];
    childIndices[0] = index;
    treeModel.nodesWereInserted(node, childIndices);
    if (nodeView != null) {
      nodeView.nodeInserted(node, index);
      getTree().expandRow(0);
    }
  }

  /**
   * Called each time a node is removed.
   * @param parent parent node of the removed node.
   * @param node removed child node in this node.
   * @param index index of the removed child node.
   */
  public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
    int[] childIndices = new int[1];
    Object[] removedChildren = new Object[1];
    childIndices[0] = index;
    removedChildren[0] = node;
    treeModel.nodesWereRemoved(parent, childIndices, removedChildren);
    if (nodeView != null) {
      nodeView.nodeRemoved(parent, node, index);
    }
  }

  /**
   * Called each type this node has changed.
   * @param node the modified node.
   */
  public void nodeChanged(ExplorerNode node) {
    treeModel.nodeChanged(node);
    if (nodeView != null) {
      nodeView.nodeChanged(node);
    }
  }

  /**
   * Displays the data object member of the node with the provided view type.
   * @param type type of the node view to display.
   * @param node node which data object should be displayed.
   */
  public void displayView(UiNodeViewType type, ExplorerNode node) {
    nodeView = factory.getNodeViewFor(node, type);
    topRightPane.setViewportView((Component) nodeView);
  }

  /**
   * The method is called when the user tries to close the window. The subclass
   * can redefine the method to perform house keeping activities or veto the
   * close of the window.
   * @return true if the window can be closed, otherwise false.
   */
  public boolean shouldClose() {
    return true;
  }

  /**
   * The initialize method is called after the construction and before the
   * explorer is displayed on the screen.
   */
  protected abstract void initialize();

  /**
   * Displays the data object member of the node with the current view.
   * @param node node which data object should be displayed.
   */
  protected void displayCurrentView(ExplorerNode node) {
    if (node != null) {
      UiNodeViewType type = node.getNodeType().getCurrentView();
      if (type != null) {
        displayView(type, node);
      }
    }
  }

  /**
   * Creates the navigator tree representing the lightweight data object graph.
   * @param root root of the data object graph.
   * @return the root node of the navigator tree.
   */
  protected ExplorerNode createTree(LightweightObject root) {
    DataObjectType type = manager.getType(root);
    AbstractVisitorFunctor visitor = type.createLwVisitorInstance();
    TreeConstructFunctor functor = new TreeConstructFunctor(factory, visitor);
    visitor.setFunctor(functor);
    DataObjectType.invokeVisitor(root.getClass(), root, visitor);
    return functor.retrieveNode(root);
  }

  /**
   * Implements the tree selection listener for the explorer view.
   */
  private final class ExplorerViewTreeSelectionListener implements
      TreeSelectionListener {

    /**
     * Reacts on a tree selection event. Each time a node is selected its
     * children are displayed in the table view. Care is taken to save the
     * header visual structure onto the node type and to avoid unnecessary
     * updates.
     * @param event tree selection event sent by the JTree component.
     */
    public void valueChanged(TreeSelectionEvent event) {
      ExplorerNode node = (ExplorerNode) getTree()
          .getLastSelectedPathComponent();
      displayCurrentView(node);
    }
  }

  /**
   * Implements the mouse listener for the explorer view.
   */
  private final class ExplorerMouseListener extends MouseAdapter {

    /**
     * Handles the mouse clicked event.
     * @param e mouse event to handle
     */
    public void mouseClicked(MouseEvent e) {
      displayPopup(e);
    }

    /**
     * Handles the mouse pressed event.
     * @param e mouse event to handle
     */
    public void mousePressed(MouseEvent e) {
      displayPopup(e);
    }

    /**
     * Handles the mouse released event.
     * @param e mouse event to handle
     */
    public void mouseReleased(MouseEvent e) {
      displayPopup(e);
    }

    /**
     * Checks if the mouse event should activate the display of the popup menu.
     * If yes displays the popup menu.
     * @param e mouse event to handle
     */
    private void displayPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        TreePath path = getTree().getPathForLocation(e.getX(), e.getY());
        if (path != null) {
          getTree().setSelectionPath(path);
          ExplorerNode node = (ExplorerNode) path.getLastPathComponent();
          JPopupMenu menu = node.getNodeType().getFactory().getPopupFactory()
              .getMenu(node);
          if (menu != null) {
            setSelectedNodeForPopup(node);
            menu.show(getTree(), e.getX(), e.getY());
          }
        }
      }
    }
  }

  /**
   * The listener of the window.
   * @author marcelbaumann
   * @version $Revision$
   */
  public static class ExplorerWindowListener extends WindowAdapter {
    /**
     * The explorer listened to.
     */
    private Explorer explorer;

    /**
     * Constructor of the class.
     * @param explorer the explorer the listener should listen.
     */
    public ExplorerWindowListener(Explorer explorer) {
      this.explorer = explorer;
    }

    public void windowClosing(WindowEvent e) {
      if (explorer.shouldClose()) {
        Window window = e.getWindow();
        window.setVisible(false);
        window.dispose();
        System.exit(0);
      }
    }
  }
}