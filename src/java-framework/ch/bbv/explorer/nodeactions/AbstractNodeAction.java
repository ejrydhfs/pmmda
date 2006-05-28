/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.nodeactions;

import javax.swing.tree.TreePath;

import ch.bbv.dog.*;
import ch.bbv.explorer.*;
import ch.bbv.explorer.actions.ExplorerNodeActionImpl;

/**
 * The abstract node action class provides common operations for complex node
 * actions.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class AbstractNodeAction extends ExplorerNodeActionImpl {
  /**
   * Flag indicating if the node is editable in this action.
   */
  private boolean editable;

  /**
   * Constructor of the class.
   * @param type node type for which the action is defined
   * @param name name of the action. If the action is an add, the name must be
   *          the name of the property
   * @param editable flag indicating if the node is editable in this action
   */
  protected AbstractNodeAction(UiNodeType type, String name, boolean editable) {
    super(name, type);
    this.editable = editable;
  }

  /**
   * Retrieves the data object graph associated with the node. The root of the
   * tree is inferred and the graph of data objects is retrieved. To insure
   * consistency the lightweight representation is build again and replaces the
   * old representation of the navigator. The explorer node containing the data
   * object and its lightweight representation passed as parameter is returned.
   * @param node node which data object should be retrieved
   * @return the node containing the data object and lightweight representation
   *         of the searched data object
   * @pre node != null
   */
  protected ExplorerNode updateNodeWithBo(ExplorerNode node) {
    assert node != null;
    ExplorerNodeOwner owner = node.getNodeOwner();
    DataObjectMgr manager = owner.getManager();
    ExplorerNode root = node;
    while ((root != null) && !root.getNodeType().getDataObjectType().isRoot()) {
      root = (ExplorerNode) root.getParent();
    }
    assert root.getDataObject() == null;
    DataObjectType type = root.getNodeType().getDataObjectType();
    DataObject boGraph = manager.getDataObjectHandler().retrieve(
        owner.getManager().getDatasource(), type.getBoClass(),
        ((LightweightObject) root.getLightweightObject()).getId());
    ExplorerNode newNode = node;
    if (boGraph != null) {
      newNode = updateNavigator(root, boGraph, (HasId) newNode
          .getLightweightObject());
      owner.getTree().expandPath(new TreePath(newNode.getPath()));
    } else {
      // TODO warn user no graph could be loaded
    }
    return newNode;
  }

  /**
   * Updates the navigator subtree from the given node with the provided data
   * object graph. The new explorer node of the data object, which triggered the
   * update is returned.
   * @param node root node of the navigator to update
   * @param root root of the data object graph to map
   * @param id identity of the node in the graph which explorer node we want
   * @return the explorer node containing the data object with the given
   *         identity if defined otherwise null
   * @pre (node != null) && (root != null)
   */
  protected ExplorerNode updateNavigator(ExplorerNode node, DataObject root,
      HasId id) {
    assert (node != null) && (root != null);
    DataObjectType type = node.getNodeType().getDataObjectType();
    ExplorerNodeOwner owner = node.getNodeOwner();
    LightweightObject lwRoot = owner.getManager().getLwHandler().createGraph(
        root);
    AbstractVisitorFunctor lwVisitor = type.createLwVisitorInstance();
    AbstractVisitorFunctor boVisitor = type.createVisitorInstance();
    TreeConstructFunctor functor = new TreeConstructFunctor(owner
        .getNodeTypeFactory(), lwVisitor);
    lwVisitor.setFunctor(functor);
    boVisitor.setFunctor(functor);
    DataObjectType.invokeVisitor(lwRoot.getClass(), lwRoot, lwVisitor);
    DataObjectType.invokeVisitor(root.getClass(), root, boVisitor);

    ExplorerNode newRoot = functor.retrieveNode(lwRoot);
    ExplorerNode newNode = functor.retrieveNodeById((HasId) node
        .getLightweightObject());
    int index = node.getParent().getIndex(node);
    ExplorerNode parent = ((ExplorerNode) node.getParent());
    parent.remove(index);
    parent.insert(newRoot, index);
    return newNode;
  }

  /**
   * Displays the node view for the data object of the node.
   * @param node explorer node which data object should be displayed
   * @param editable indicates if the node is editable or not
   * @param created indicates if the node was created in the action or not
   * @pre node != null
   */
  protected void displayView(ExplorerNode node, boolean editable,
      boolean created) {
    assert node != null;
    DataObject boObject = node.getDataObject();
    ExplorerNode newNode = node;
    if (boObject == null) {
      newNode = updateNodeWithBo(node);
      boObject = newNode.getDataObject();
    }
    ExplorerNodeOwner owner = newNode.getNodeOwner();
    UiNodeTypeFactory factory = owner.getNodeTypeFactory();
    NodePropertiesDialog dialog = factory.getPropertiesDialog((Explorer) owner);
    dialog.setModel(newNode, editable, created, getViewLabel(node), this);
    dialog.setVisible(true);
  }

  /**
   * Creates a new explorer node as child of the one passed as parameter. Based
   * on the node type associated with the action, a new data object and
   * lightweight object are created. The default property view is displayed to
   * fill the data of the data object. The explorer node is available to the
   * view. It is guaranteed that the explorer node has a parent. If the class of
   * the lightweight object and of the data object is the same only one object
   * is created.
   * @param node node which should have a new child
   * @param nodeType type of the node to be created
   * @pre node != null
   */
  protected void createNode(ExplorerNode node, UiNodeType nodeType) {
    assert node != null;
    ExplorerNode newNode = node;
    DataObject boObject = node.getDataObject();
    if (boObject == null) {
      newNode = updateNodeWithBo(node);
      boObject = newNode.getDataObject();
    }

    DataObjectType type = nodeType.getDataObjectType();
    Class boClass = type.getBoClass();
    Class lwClass = type.getLwClass();
    boObject = null;
    LightweightObject lwObject = null;
    if (boClass != null) {
      boObject = type.createBoInstance();
      if ((lwClass != null) && (lwClass != boClass)) {
        lwObject = type.createLwInstance();
      } else {
        lwObject = (LightweightObject) boObject;
      }
    }
    ExplorerNode childNode = new ExplorerNode(nodeType, lwObject, boObject);
    childNode.setParent(newNode);
    displayView(childNode, true, true);
    newNode.addChild(childNode);
  }

  /**
   * Returns true if the node is editable otherwise false.
   * @return true if the node is editable otherwise false
   */
  protected boolean isEditable() {
    return editable;
  }

  /**
   * Returns the name for the view. The name is constructed with the view name
   * and the name of the displayed node if defined.
   * @param node node to be displayed.
   * @return label of the view.
   */
  private String getViewLabel(ExplorerNode node) {
    StringBuilder viewName = new StringBuilder();
    viewName.append(getName());

    String name = node.getName();
    if (name != null) {
      viewName.append(" ").append(node.getName());
    }
    return viewName.toString();
  }
}