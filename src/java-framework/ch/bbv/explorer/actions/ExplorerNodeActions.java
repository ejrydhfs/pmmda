/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.actions;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import ch.bbv.dog.*;
import ch.bbv.explorer.*;

/**
 * The explorer node actions provides a set of standard actions used in almost
 * all explorer based applications. The class provides methods to create these
 * standard actions. The design is functor oriented. The actions are the
 * following.
 * <ul>
 * <li>View the properties of a node as read-only.</li>
 * <li>Edit the properties of a node.</li>
 * <li>Display a right pane view for a node. The view is identified through the
 * node type and name passed in the constructor.</li>
 * <li>Create a new node and data object. The node type is identified through
 * the node type passed in the constructor. The data model method responsible to
 * insert the new data object in the model upon sucessful completion is infered
 * from the property.</li>
 * <li>Remove a node from the explorer and the underlying data model. The data
 * model method responsible to remove the data object from the model is infered
 * from the property.</li>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ExplorerNodeActions extends ExplorerNodeActionImpl {
  private static final int VIEW_PROPERTIES = 0;

  private static final int EDIT_PROPERTIES = 1;

  private static final int DISPLAY_NODE_VIEW_ACTION = 2;

  private static final int ADD_ACTION = 3;

  private static final int INSERT_ACTION = 4;

  private static final int SAVE_ACTION = 5;

  /**
   * The action which should be executed.
   */
  protected int action;

  protected ExplorerNode before;

  /**
   * Constructor of the class.
   * @param type node type for which the action is defined.
   * @param name name of the action. If the action is an add, the name must be
   *        the name of the property.
   */
  private ExplorerNodeActions(UiNodeType type, String name) {
    super(name, type);
  }

  /**
   * Creates an action displaying the properties view with the given name for
   * the node type.
   * @param type node type for which the action is defined.
   * @param name the name of the action.
   * @return a new explorer node action with the above aspects.
   */
  public static ExplorerNodeActions createViewNode(UiNodeType type, String name) {
    ExplorerNodeActions action = new ExplorerNodeActions(type, name);
    action.action = VIEW_PROPERTIES;
    return action;
  }

  /**
   * Creates an action responsible to edit the properties view of the given node
   * type.
   * @param type node type for which the action is defined.
   * @param name the name of the action
   * @return a new explorer node action with the above aspects.
   */
  public static ExplorerNodeActions createEditNode(UiNodeType type, String name) {
    ExplorerNodeActions action = new ExplorerNodeActions(type, name);
    action.action = EDIT_PROPERTIES;
    return action;
  }

  /**
   * Creates an action responsible to display a node right view of the given
   * node type. If the data object is not available it is loaded with the help
   * of the DOG framework.
   * @param type node type for which the action is defined.
   * @param name the name of the action.
   * @return a new explorer node action with the above aspects.
   */
  public static ExplorerNodeActions createDisplayNode(UiNodeType type,
      String name) {
    ExplorerNodeActions action = new ExplorerNodeActions(type, name);
    action.action = DISPLAY_NODE_VIEW_ACTION;
    return action;
  }

  /**
   * Creates an action responsible to add a new node instance.
   * @param type node type for which the action is defined.
   * @param name the name of the action.
   * @return a new explorer node action with the above aspects.
   */
  public static ExplorerNodeActions createAddNode(UiNodeType type, String name) {
    ExplorerNodeActions action = new ExplorerNodeActions(type, name);
    action.action = ADD_ACTION;
    return action;
  }

  /**
   * Creates an action responsible to insert a new node instance before the
   * selected one.
   * @param type node type for which the action is defined.
   * @param name the name of the action.
   * @return a new explorer node action with the above aspects.
   */
  public static ExplorerNodeActions createInsertNode(UiNodeType type,
      String name) {
    ExplorerNodeActions action = new ExplorerNodeActions(type, name);
    action.action = INSERT_ACTION;
    return action;
  }

  /**
   * Creates an action responsible to save a new node instance graph.
   * @param type node type for which the action is defined.
   * @param name the name of the action.
   * @return a new explorer node action with the above aspects.
   */
  public static ExplorerNodeActions createSaveNode(UiNodeType type, String name) {
    ExplorerNodeActions action = new ExplorerNodeActions(type, name);
    action.action = SAVE_ACTION;
    return action;
  }

  /**
   * Creates an action responsible to remove a node instance.
   * @param type node type for which the action is defined.
   * @param name the name of the action.
   * @return a new explorer node action with the above aspects.
   */
  public static ExplorerNodeAction createRemoveNode(UiNodeType type, String name) {
    return new RemoveNodeAction(type, name);
  }

  public void execute(ExplorerNode node) {
    switch (action) {
    case VIEW_PROPERTIES:
      displayView(node, null);
      break;
    case EDIT_PROPERTIES:
      displayView(node, this);
      break;
    case DISPLAY_NODE_VIEW_ACTION:
      displayNodeView(node);
    case ADD_ACTION:
      createNode(node, getNodeType());
      break;
    case INSERT_ACTION:
      before = node;
      createNode((ExplorerNode) node.getParent(), node.getNodeType());
      break;
    case SAVE_ACTION:
      saveNode(node);
      break;
    }
  }

  public void postExecute(ExplorerNode node, boolean shouldUndo) {
    switch (action) {
    case VIEW_PROPERTIES:
      break;
    case EDIT_PROPERTIES:
      if (!shouldUndo) {
        node.nodeChanged();
      }
      break;
    case DISPLAY_NODE_VIEW_ACTION:
      break;
    case ADD_ACTION:
      if (shouldUndo) {
        ((ExplorerNode) node.getParent()).remove(node);
      } else {
        ExplorerNode parent = (ExplorerNode) node.getParent();
        UiNodeType type = parent.getNodeType();
        UiRelation relation = type.getRelation(getName());
        relation.addProperty(parent.getLightweightObject(), node
            .getLightweightObject());
        if (parent.getLightweightObject() != parent.getDataObject()) {
          relation.addProperty(parent.getDataObject(), node.getDataObject());
        }
        node.nodeChanged();
      }
      break;
    case INSERT_ACTION:
      if (shouldUndo) {
        ((ExplorerNode) node.getParent()).remove(node);
      } else {
        ExplorerNode parent = (ExplorerNode) node.getParent();
        UiNodeType type = parent.getNodeType();
        int index = parent.getIndex(node);
        UiRelation relation = type.getRelation(getName());
        relation.setProperty(parent.getLightweightObject(), index, node
            .getLightweightObject());
        if (parent.getLightweightObject() != parent.getDataObject()) {
          relation.setProperty(parent.getDataObject(), index, node
              .getDataObject());
        }
        node.nodeChanged();
      }
      break;
    }
  }

  private void displayView(ExplorerNode node, ExplorerNodeAction action) {
    Object boObject = node.getDataObject();
    ExplorerNode newNode = node;
    if (boObject == null) {
      newNode = updateNodeWithBo(node);
      boObject = (DataObject) newNode.getDataObject();
    }

    UiNodeTypeFactory factory = newNode.getExplorer().getNodeTypeFactory();
    NodePropertiesDialog dialog = factory.getPropertiesDialog();
    dialog.setModel(newNode, isEditable(), isNew(), getViewLabel(node), action);
    dialog.setVisible(true);
  }

  private void displayNodeView(ExplorerNode node) {
    Object boObject = node.getDataObject();
    if (boObject == null) {
      ExplorerNode newNode = updateNodeWithBo(node);
      boObject = (DataObject) newNode.getDataObject();
    }

    UiNodeViewType type = node.getNodeType().getView(getName());
    node.getExplorer().displayView(type, node);
  }

  /**
   * Creates a new explorer node as child of the one passed as parameter. Based
   * on the node type associated with the action, a new data object and
   * lightweight object are created. The default property view is displayed to
   * fill the data of the data object. The explorer node is available to the
   * view. It is guaranteed that the explorer node has a parent. If the class of
   * the lightweight object and of the data object is the same only one object
   * is created.
   * @param node node which should have a new child.
   * @param nodeType type of the node to be created.
   */
  private void createNode(ExplorerNode node, UiNodeType nodeType) {
    ExplorerNode newNode = node;
    Object boObject = node.getDataObject();
    if (boObject == null) {
      newNode = updateNodeWithBo(node);
      boObject = (DataObject) newNode.getDataObject();
    }

    DataObjectType type = nodeType.getDataObjectType();
    Class boClass = type.getBoClass();
    Class lwClass = type.getLwClass();
    boObject = null;

    Object lwObject = null;
    if (boClass != null) {
      boObject = type.createBoInstance();
      if ((lwClass != null) && (lwClass != boClass)) {
        lwObject = type.createLwInstance();
      } else {
        lwObject = boObject;
      }
    }

    ExplorerNode childNode = new ExplorerNode(nodeType, lwObject, boObject);
    childNode.setParent(newNode);
    displayView(childNode, this);
    newNode.addChild(childNode);
  }

  private void saveNode(ExplorerNode node) {
    DataObject object = (DataObject) node.getDataObject();
    Explorer explorer = node.getExplorer();
    if ((object == null) || !object.isModified()) {
      JOptionPane.showMessageDialog(explorer.getFrame(),
          "Object was not modified.", "Save Operation",
          JOptionPane.WARNING_MESSAGE);
    } else if (object.isNew() && hasRootOwner(node)) {
      JOptionPane.showMessageDialog(explorer.getFrame(),
          "New Object must be saved from owner object.", "Save Operation",
          JOptionPane.WARNING_MESSAGE);
    } else {
      DataObjectType type = node.getNodeType().getDataObjectType();
      DataObjectHandler handler = node.getExplorer().getManager()
          .getBoHandler();
      handler.store(node.getExplorer().getDatasource(), type.getBoClass(),
          object);
    }
  }

  /**
   * Returns true if the node is editable otherwise false.
   * @return true if the node is editable otherwise false.
   */
  private boolean isEditable() {
    return (action == EDIT_PROPERTIES) || (action == ADD_ACTION);
  }

  /**
   * Returns true if the node is new otherwise false.
   * @return true if the node is new otherwise false.
   */
  private boolean isNew() {
    return action == ADD_ACTION;
  }

  /**
   * Returns the name for the view. The name is constructed with the view name
   * and the name of the displayed node if defined.
   * @param node node to be displayed.
   * @return label of the view.
   */
  private String getViewLabel(ExplorerNode node) {
    StringBuffer viewName = new StringBuffer();
    viewName.append(getName());

    String name = node.getName();
    if (name != null) {
      viewName.append(" ").append(node.getName());
    }
    return viewName.toString();
  }

  /**
   * Retrieves the data object graph associated with the node. The root of the
   * tree is inferred and the graph of data objects is retrieved. To insure
   * consistency the lightweight representation is build again and replaces the
   * old representation of the navigator. The explorer node containing the data
   * object and its lightweight representation passed as parameter is returned.
   * @param node node which data object should be retrieved.
   * @return the node containing the data object and lightweight representation
   *         of the searched data object.
   */
  private ExplorerNode updateNodeWithBo(ExplorerNode node) {
    ExplorerNode newNode = node;
    Explorer explorer = node.getExplorer();
    DataObjectManager manager = explorer.getManager();
    UiNodeTypeFactory factory = explorer.getNodeTypeFactory();
    ExplorerNode root = node;
    while ((root != null) && !root.getNodeType().getDataObjectType().isRoot()) {
      root = (ExplorerNode) root.getParent();
    }
    assert (root.getDataObject() == null);

    DataObjectType type = root.getNodeType().getDataObjectType();
    DataObject boGraph = manager.getBoHandler().retrieve(
        explorer.getDatasource(), type.getBoClass(),
        ((LightweightObject) root.getLightweightObject()).getId());
    if (boGraph != null) {
      LightweightObject lwGraph = manager.getLwHandler().createGraph(boGraph);
      AbstractVisitorFunctor lwVisitor = type.createLwVisitorInstance();
      AbstractVisitorFunctor boVisitor = type.createVisitorInstance();
      TreeConstructFunctor functor = new TreeConstructFunctor(factory,
          lwVisitor);
      lwVisitor.setFunctor(functor);
      boVisitor.setFunctor(functor);
      DataObjectType.invokeVisitor(lwGraph.getClass(), lwGraph, lwVisitor);
      DataObjectType.invokeVisitor(boGraph.getClass(), boGraph, boVisitor);

      ExplorerNode newRoot = functor.retrieveNode(lwGraph);
      newNode = functor
          .retrieveNodeById((HasId) newNode.getLightweightObject());

      int index = root.getParent().getIndex(root);
      ExplorerNode parent = ((ExplorerNode) root.getParent());
      parent.remove(index);
      parent.insert(newRoot, index);
      explorer.getTree().expandPath(new TreePath(newRoot.getPath()));
    }
    return newNode;
  }

  /**
   * Returns true if the node has an owning root which is not not new.
   * @param node node to infer.
   * @return true if a root node was found otherwise false.
   */
  private boolean hasRootOwner(ExplorerNode node) {
    ExplorerNode root = node;
    boolean continues = true;
    while (continues && (root != null)) {
      DataObject boOwner = (DataObject) root.getDataObject();
      if (root.getNodeType().getDataObjectType().isRoot() && !boOwner.isNew()) {
        continues = false;
      } else {
        root = (ExplorerNode) root.getParent();
      }
    }
    return (root != null);
  }
}