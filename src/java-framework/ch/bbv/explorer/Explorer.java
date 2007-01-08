/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.defaults.DefaultDockingPort;

import ch.bbv.application.Library;
import ch.bbv.application.VersionId;
import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.DataObjectType;
import ch.bbv.dog.LightweightObject;
import ch.bbv.explorer.components.ExplorerWindowAdapter;
import ch.bbv.explorer.components.ValidationPanel;
import ch.bbv.explorer.resources.Messages;
import ch.bbv.utilities.ReflectUtilities;
import ch.bbv.utilities.ResourceUtilities;

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
 * the ancestors are called</li>
 * <li>The initialize method is called starting from the leaf descendent. Each
 * subclass, which overwrites the method must call the parent method.</li>
 * <li>The display method shows the explorer on the screen</li>
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
   * Docking port for the explorer views.
   */
  private DefaultDockingPort viewsPort;

  /**
   * Docking port for the explorer trees.
   */
  private DefaultDockingPort treesPort;

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
   * Status bar of the explorer view.
   */
  private StatusBar statusBar;

  /**
   * Explorer node tree view used to display the tree model.
   */
  private ExplorerTreeView tree;

  /**
   * List of all tree views defined in the explorer.
   */
  private List<ExplorerTreeView> treeViews;

  /**
   * All the views of the current node.
   */
  private List<NodeView> nodeViews;

  /**
   * The factory of user interface type used to create all nodes in this
   * explorer.
   */
  private UiNodeTypeFactory factory;

  /**
   * The handler of the lightweight and data objects.
   */
  private DataObjectMgr manager;

  /**
   * The menu bar of the explorer frame.
   */
  private ExplorerMenuBar menuBar;

  /**
   * Validation panel of the explorer.
   */
  private ValidationPanel validationPanel;

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
     * @param clazz class of the explorer to instantiate
     * @param types signature of the constructur to call
     * @param parameters parameters of the constructor to call
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
     * @return the newly created explorer
     */
    public Explorer getExplorer() {
      return explorer;
    }
  }

  /**
   * Instantiates a new explorer and runs it in the context of the AWT event
   * processing thread as requested in Swing best practices.
   * @param clazz class of the explorer to create
   * @param types signature of the constructur to call
   * @param parameters parameters of the constructor to call
   * @return the newly created explorer
   */
  public static Explorer createAndShow(Class clazz, Class[] types,
      Object[] parameters) {
    GuiTask task = new GuiTask(clazz, types, parameters);
    SwingUtilities.invokeLater(task);
    return task.getExplorer();
  }

  /**
   * Constructor of the class.
   * @param factory node type factory of the explorer
   * @param manager data object manager of the explorer
   */
  public Explorer(UiNodeTypeFactory factory, DataObjectMgr manager) {
    // loads the resource bundle for the locale
    ResourceUtilities.loadMessages("ch.bbv.explorer.resources.Explorer",
        Messages.class);
    this.factory = factory;
    this.manager = manager;
    treeViews = new ArrayList<ExplorerTreeView>();
    nodeViews = new ArrayList<NodeView>();
    tree = new ExplorerTreeView("Tree");
    // create the explorer docking structure
    viewsPort = new DefaultDockingPort();
    treesPort = new DefaultDockingPort();
    utilitiesPort = new DefaultDockingPort();
    viewUtilitiesPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, viewsPort,
        utilitiesPort);
    viewUtilitiesPane.setDividerSize(5);
    viewUtilitiesPane.setResizeWeight(0.8);
    treeViewPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treesPort,
        viewUtilitiesPane);
    treeViewPane.setDividerSize(5);
    treeViewPane.setResizeWeight(0.3);
    addExplorerTree(tree);
    // create the status bar and construct the frame
    frame = new JFrame(Messages.titleExplorerFrame);
    frame.add(treeViewPane, BorderLayout.CENTER);
    statusBar = new StatusBar();
    frame.add(statusBar, BorderLayout.SOUTH);
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new ExplorerWindowAdapter(this));
  }

  /**
   * Returns the document displayed in the explorer.
   * @return the document displayed in the explorer if defined otherwise null
   */
  public abstract ExplorerDocument getDocument();

  public UiNodeTypeFactory getNodeTypeFactory() {
    return factory;
  }

  public DataObjectMgr getManager() {
    return manager;
  }

  /**
   * Returns the validation panel of the explorer.
   * @return the requested validation panel
   */
  public ValidationPanel getValidationPanel() {
    return validationPanel;
  }

  protected void setValidationPanel(ValidationPanel validationPanel) {
    this.validationPanel = validationPanel;

  }

  /**
   * Sets the title of the explorer frame.
   * @param title new title of the explorer
   * @pre title != null
   */
  public void setTitle(String title) {
    assert title != null;
    frame.setTitle(title);
  }

  /**
   * Returns the main port in the docking framework defined for the explorer.
   * @return the requested docking port
   */
  public DockingPort getMainPort() {
    return viewsPort;
  }

  /**
   * Returns the navigation port in the docking framework defined for the
   * explorer.
   * @return the requested docking port
   */
  public DockingPort getNavigationPort() {
    return treesPort;
  }

  /**
   * Returns the utilities port in the docking framework defined for the
   * explorer.
   * @return the requested docking port
   */
  public DockingPort getUtilitiesPort() {
    return utilitiesPort;
  }

  /**
   * Returns the menu bar of the explorer.
   * @return the menu bar of the explorer
   * @see #setMenuBar(ExplorerMenuBar)
   */
  public ExplorerMenuBar getMenuBar() {
    return menuBar;
  }

  /**
   * Returns the status bar of the explorer.
   * @return the status bar of the explorer
   */
  public StatusBar getStatusBar() {
    return statusBar;
  }

  /**
   * Sets the menu bar of the explorer.
   * @param menuBar the new menu bar of the explorer
   * @see #getMenuBar()
   */
  public void setMenuBar(ExplorerMenuBar menuBar) {
    this.menuBar = menuBar;
    frame.setJMenuBar(menuBar);
  }

  /**
   * Returns the frame containing the explorer.
   * @return the frame containing the explorer panel
   */
  public JFrame getFrame() {
    return frame;
  }

  public void addExplorerTree(ExplorerTreeView tree) {
    Dockable treeDock = DockingManager.registerDockable(tree.getPane(), tree
        .getName());
    treesPort.dock(treeDock, DockingConstants.CENTER_REGION);
  }

  public void removeExplorerTree(ExplorerTreeView tree) {
    DockingManager.undock(tree.getPane());
  }

  public JTree getTree() {
    return tree.getTree();
  }

  /**
   * Returns the root of the explorer tree.
   * @return root of the explorer if defined otherwise null
   * @see #setRoot(ExplorerNode)
   */
  public ExplorerNode getRoot() {
    return tree.getRoot();
  }

  /**
   * Sets the root of the explorer tree.
   * @param root new root of the explorer tree
   * @see #getRoot()
   */
  public void setRoot(ExplorerNode root) {
    tree.setRoot(root, this);
  }

  /**
   * Displays the explorer window.
   */
  protected void display() {
    frame.setLocation(100, 100);
    frame.pack();
    frame.setSize(800, 600);
    frame.setVisible(true);
  }

  public void nodeInserted(ExplorerNode node, int index) {
    int[] childIndices = new int[1];
    childIndices[0] = index;
    tree.getTreeModel().nodesWereInserted(node, childIndices);
    for (NodeView nodeView : nodeViews) {
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
    for (NodeView nodeView : nodeViews) {
      nodeView.nodeRemoved(parent, node, index);
    }
  }

  public void nodeChanged(ExplorerNode node) {
    ExplorerDocument document = getDocument();
    document.setModified(true);
    getMenuBar().documentChanged(document);
    tree.getTreeModel().nodeChanged(node);
    for (NodeView nodeView : nodeViews) {
      nodeView.nodeChanged(node);
    }
  }

  /**
   * The method is called when the user tries to close the window. The subclass
   * can redefine the method to perform house keeping activities or veto it.
   */
  public void close() {
    frame.setVisible(false);
    frame.dispose();
  }

  /**
   * Returns the user interface node type factory of the explorer.
   * @return the node type factory
   */
  protected UiNodeTypeFactory getFactory() {
    return factory;
  }

  /**
   * Creates the navigator tree representing the lightweight data object graph.
   * @param root root of the data object graph
   * @return the root node of the navigator tree
   */
  protected ExplorerNode createTree(LightweightObject root) {
    return createTree(root, null);
  }

  /**
   * Creates the navigator tree representing the lightweight data object graph.
   * The data objects of the corresponding tree are set in the explorer nodes.
   * @param lwRoot root of the lightweight data object graph
   * @param doRoot root of the data object graph
   * @return the root node of the navigator tree
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
    libraries.add(new Library("forms", new VersionId(1, 0, 6), "BSD License",
        "www.jgoodies.com"));
    libraries.add(new Library("validation", new VersionId(1, 1, 0),
        "BSD License", "www.jgoodies.com"));
    libraries.add(new Library("glazedlists", new VersionId(1, 5, 0), "LGPL",
        "www.publicobject.com/glazedlists"));
    libraries.add(new Library("jcalendar", new VersionId(1, 2, 1), "LGPL",
        "www.toedter.com/jcalendar"));
    return libraries;
  }
}