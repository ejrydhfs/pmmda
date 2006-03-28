/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DefaultDockingPort;

import ch.bbv.application.Library;
import ch.bbv.application.VersionId;
import ch.bbv.dog.*;
import ch.bbv.explorer.components.ExplorerWindowAdapter;
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
 * The right pane provide two panels. The top panel provides detailed views of
 * the selected node in the navigator. The bottom panel is a docking panel where
 * additional utilities views can be displayed.
 * </p>
 * @author Marcel Baumann
 * @version $Revision$
 */
public abstract class Explorer implements ExplorerNodeOwner {
  /**
   * The frame of the explorer view.
   */
  private JFrame frame;

  /**
   * Docking port for the explorer.
   */
  private DefaultDockingPort viewPort;

  /**
   * Docking port for the explorer.
   */
  private DefaultDockingPort navigationPort;

  /**
   * Docking port for the explorer.
   */
  private DefaultDockingPort utilitiesPort;

  /**
   * The split pane for the vertical seperation between navigation and the
   * views.
   */
  private JSplitPane treeViewPane;

  /**
   * The split pane for the horizontal seperation between the views and the
   * utilities.
   */
  private JSplitPane viewUtilitiesPane;

  /**
   * Swing component of the explorer view.
   */
  private JScrollPane treePane;

  /**
   * Status bar of the explorer view.
   */
  private StatusBar statusBar;

  /**
   * Explorer node tree view used to display the tree model.
   */
  private ExplorerTree tree;

  /**
   * Current node
   */
  private NodeView nodeView;

  /**
   * The selection listener for the explorer frame.
   */
  private ExplorerViewTreeSelectionListener selectionListener;

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
    /**
     * The explorer which runs in this thread.
     */
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
   * Constructor of the class.
   * @param factory node type factory of the explorer.
   * @param manager data object manager of the explorer.
   */
  public Explorer(UiNodeTypeFactory factory, DataObjectManager manager) {
    this.factory = factory;
    this.manager = manager;
    tree = new ExplorerTree();
    // create the explorer docking structure
    viewPort = new DefaultDockingPort();
    navigationPort = new DefaultDockingPort();
    utilitiesPort = new DefaultDockingPort();
    viewUtilitiesPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, viewPort,
        utilitiesPort);
    viewUtilitiesPane.setDividerSize(5);
    viewUtilitiesPane.setResizeWeight(0.8);
    treeViewPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPort,
        viewUtilitiesPane);
    treeViewPane.setDividerSize(5);
    treeViewPane.setResizeWeight(0.3);
    treePane = new JScrollPane(tree.getTree());
    treePane.setName("Tree");
    Dockable tree = DockingManager.registerDockable(treePane, treePane
        .getName());
    navigationPort.dock(tree, DockingConstants.CENTER_REGION);

    // create the status bar and construct the frame
    frame = new JFrame("Explorer");
    frame.add(treeViewPane, BorderLayout.CENTER);
    statusBar = new StatusBar();
    frame.add(statusBar, BorderLayout.SOUTH);
    frame.addWindowListener(new ExplorerWindowAdapter(this));
    selectionListener = new ExplorerViewTreeSelectionListener();
  }

  /**
   * Returns the document displayed in the explorer.
   * @return the document displayed in the explorer if defined otherwise null.
   */
  public abstract ExplorerDocument getDocument();

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
   * Sets the title of the explorer frame.
   * @param title new title of the explorer.
   * @pre title != null
   */
  public void setTitle(String title) {
    assert title != null;
    frame.setTitle(title);
  }

  /**
   * Returns the main port in the docking framework defined for the explorer.
   * @return the requested docking port.
   */
  public DockingPort getMainPort() {
    return viewPort;
  }

  /**
   * Returns the navigation port in the docking framework defined for the
   * explorer.
   * @return the requested docking port.
   */
  public DockingPort getNavigationPort() {
    return navigationPort;
  }

  /**
   * Returns the utilities port in the docking framework defined for the
   * explorer.
   * @return the requested docking port.
   */
  public DockingPort getUtilitiesPort() {
    return utilitiesPort;
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
    return tree.getTree();
  }

  /**
   * Returns the root of the explorer tree.
   * @return root of the explorer if defined otherwise null.
   * @see #setRoot(ExplorerNode)
   */
  public ExplorerNode getRoot() {
    return tree.getRoot();
  }

  /**
   * Sets the root of the explorer tree.
   * @param root new root of the explorer tree.
   * @see #getRoot()
   */
  public void setRoot(ExplorerNode root) {
    tree.setRoot(root, this);
    JTree view = tree.getTree();
    view.addTreeSelectionListener(selectionListener);
    view.addMouseListener(new ExplorerMouseListener());
    treePane.setViewportView(view);
  }

  /**
   * Displays the explorer window.
   */
  void display() {
    frame.setLocation(100, 100);
    frame.pack();
    frame.setSize(800, 600);
    frame.setVisible(true);
  }

  public void nodeInserted(ExplorerNode node, int index) {
    int[] childIndices = new int[1];
    childIndices[0] = index;
    tree.getTreeModel().nodesWereInserted(node, childIndices);
    if (nodeView != null) {
      nodeView.nodeInserted(node, index);
      tree.getTree().expandRow(0);
    }
  }

  public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
    ExplorerDocument document = getDocument();
    document.setModified(true);
    getMenuBar().documentChanged(document);
    int[] childIndices = new int[1];
    Object[] removedChildren = new Object[1];
    childIndices[0] = index;
    removedChildren[0] = node;
    tree.getTreeModel().nodesWereRemoved(parent, childIndices, removedChildren);
    if (nodeView != null) {
      nodeView.nodeRemoved(parent, node, index);
    }
  }

  public void nodeChanged(ExplorerNode node) {
    ExplorerDocument document = getDocument();
    document.setModified(true);
    getMenuBar().documentChanged(document);
    tree.getTreeModel().nodeChanged(node);
    if (nodeView != null) {
      nodeView.nodeChanged(node);
    }
  }

  /**
   * The method is called when the user tries to close the window. The subclass
   * can redefine the method to perform house keeping activities or veto the
   */
  public void close() {
    frame.setVisible(false);
    frame.dispose();
  }

  /**
   * Retursn the user interface node type factory of the explorer.
   * @return the node type factory.
   */
  protected UiNodeTypeFactory getFactory() {
    return factory;
  }

  /**
   * Creates the navigator tree representing the lightweight data object graph.
   * @param root root of the data object graph.
   * @return the root node of the navigator tree.
   */
  protected ExplorerNode createTree(LightweightObject root) {
    return createTree(root, null);
  }

  /**
   * Creates the navigator tree representing the lightweight data object graph.
   * The data objects of the corresponding tree are set in the explorer nodes.
   * @param lwRoot root of the lightweight data object graph.
   * @param doRoot root of the data object graph.
   * @return the root node of the navigator tree.
   */
  protected ExplorerNode createTree(LightweightObject lwRoot, DataObject doRoot) {
    DataObjectType type = manager.getType(lwRoot);
    AbstractVisitorFunctor visitor = type.createLwVisitorInstance();
    TreeConstructFunctor functor = new TreeConstructFunctor(factory, visitor);
    DataObjectType.invokeVisitor(lwRoot.getClass(), lwRoot, visitor);
    if (doRoot != null) {
      visitor = type.createVisitorInstance();
      functor.reset(visitor);
      DataObjectType.invokeVisitor(doRoot.getClass(), doRoot, visitor);
    }
    return functor.retrieveNode(lwRoot);

  }

  /**
   * The initialize method is called after the construction and before the
   * explorer is displayed on the screen.
   */
  protected abstract void initialize();

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
     * @param event tree selection event sent by the JTree component.
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
     * @param node node which data object should be displayed.
     */
    public void displayViews(ExplorerNode node) {
      List<NodeView> newViews = getFactory().getNodeViews(node);
      if ((views != null) && !views.containsAll(newViews)) {
        for (NodeView view : views) {
          DockingManager.undock((JComponent) view);
        }
      }
      views = newViews;
      for (NodeView view : newViews) {
        Dockable dockable = DockingManager.registerDockable((JComponent) view,
            view.getName());
        DockingManager.dock(dockable, getMainPort(),
            DockingConstants.CENTER_REGION);
      }
    }
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
     * @param event mouse event to handle.
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
            ExplorerTree.setSelectedNodeForPopup(node);
            menu.show(getTree(), event.getX(), event.getY());
          }
        }
      }
    }
  }

  /**
   * Returns the libraries used in the explorer framework.
   * @return the libraries the framework depends on
   */
  protected List<Library> getLibraries() {
    List<Library> libraries = new ArrayList<Library>();
    libraries.add(new Library("commons-beanutils", new VersionId(1, 7, 0),
        "Apache License", "http://jakarta.apache.org/commons"));
    libraries.add(new Library("commons-cli", new VersionId(1, 0, 0),
        "Apache License", "http://jakarta.apache.org/cli"));
    libraries.add(new Library("commons-collections", new VersionId(3, 1, 0),
        "Apache License", "http://jakarta.apache.org/commons"));
    libraries.add(new Library("commons-io", new VersionId(1, 2, 0),
        "Apache License", "http://jakarta.apache.org/commons"));
    libraries.add(new Library("commons-logging", new VersionId(1, 0, 4),
        "Apache License", "http://jakarta.apache.org/commons"));
    libraries.add(new Library("commons-logging-api", new VersionId(1, 0, 4),
        "Apache License", "http://jakarta.apache.org/commons"));
    libraries.add(new Library("flexdock", new VersionId(0, 4, 0),
        "MIT License", "flexdock.dev.java"));
    libraries.add(new Library("forms", new VersionId(1, 0, 6), "OBSD License",
        "www.jgoodies.com"));
    libraries.add(new Library("glazedlists", new VersionId(1, 5, 0), "LGPL",
        "www.publicobject.com/glazedlists"));
    libraries.add(new Library("jcalendar", new VersionId(1, 2, 1), "LGPL",
        "www.toedter.com/jcalendar"));
    return libraries;
  }
}