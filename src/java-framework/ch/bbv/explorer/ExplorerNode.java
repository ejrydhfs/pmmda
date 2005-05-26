/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.util.*;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.LightweightObject;

/**
 * The explorer node defines a node displayed in the navigator pane of the tree
 * list user interface.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ExplorerNode implements MutableTreeNode {
  /**
   * List of all children of the node.
   */
  private List<ExplorerNode> children;

  /**
   * The parent node of this node if defined otherwise null.
   */
  private ExplorerNode parent;

  /**
   * The data object of this node if loaded otherwise null.
   */
  private DataObject boObject;

  /**
   * The lightweight data object of this node.
   */
  private LightweightObject lwObject;

  /**
   * The node type describing the user interface aspect of the data object type
   * to which the data and lightweight objects belongs to.
   */
  private UiNodeType nodeType;

  /**
   * The explorer owning the node.
   */
  private Explorer explorer;

  /**
   * Default constructor of the class.
   */
  public ExplorerNode() {
    children = new ArrayList<ExplorerNode>();
  }

  /**
   * Constructor of the class.
   * @param nodeType type of the node displayed.
   * @param lwObject lightweight object to display.
   * @param boObject data object to display.
   * @pre (nodeType != null) && (lwObject != null)
   */
  public ExplorerNode(UiNodeType nodeType, LightweightObject lwObject,
      DataObject boObject) {
    this();
    this.nodeType = nodeType;
    setLightweightObject(lwObject);
    setDataObject(boObject);
  }

  /**
   * Returns the name of the node. The name is retrieved from the lightweight
   * object.
   * @return the name of the node.
   */
  public String getName() {
    return nodeType.getNameFor(lwObject);
  }

  /**
   * Returns the node type of the explorer node.
   * @return node type of the explorer node.
   */
  public UiNodeType getNodeType() {
    return nodeType;
  }

  /**
   * Returns the data object associated with the explorer node.
   * @return the data object of the explorer node.
   * @see #setDataObject(DataObject)
   */
  public DataObject getDataObject() {
    return boObject;
  }

  /**
   * Sets the data object of the explorer node.
   * @param boObject data object of the explorer node.
   * @see #getDataObject()
   */
  public void setDataObject(DataObject boObject) {
    this.boObject = boObject;
  }

  /**
   * Returns the lightweight object associated with the explorer node.
   * @return the lightweight object of the explorer node.
   * @see #setLightweightObject(LightweightObject)
   */
  public Object getLightweightObject() {
    return lwObject;
  }

  /**
   * Sets the lightweight object of the explorer node.
   * @param lwObject lightweight object of the explorer node.
   * @see #getLightweightObject()
   */
  public void setLightweightObject(LightweightObject lwObject) {
    this.lwObject = lwObject;
  }

  public void removeFromParent() {
    parent.remove(this);
  }

  public void setUserObject(Object lwObject) {
    this.lwObject = (LightweightObject) lwObject;
  }

  public void setParent(MutableTreeNode parent) {
    this.parent = (ExplorerNode) parent;
  }

  /**
   * Adds a child at the end of the list of children of this node.
   * @param child child to add.
   */
  public void addChild(ExplorerNode child) {
    child.setParent(this);
    children.add(child);

    Explorer explorer = getExplorer();
    if (explorer != null) {
      explorer.nodeInserted(this, children.size() - 1);
    }
  }

  /**
   * Inserts the child node at the given index in this node.
   * @param child child node to insert.
   * @param index index at which the child node is inserted.
   */
  public void insert(MutableTreeNode child, int index) {
    child.setParent(this);
    children.add(index, (ExplorerNode) child);

    Explorer explorer = getExplorer();
    if (explorer != null) {
      explorer.nodeInserted(this, index);
    }
  }

  /**
   * Handles the event that the node has changed.
   */
  public void nodeChanged() {
    Explorer explorer = getExplorer();
    if (explorer != null) {
      explorer.nodeChanged(this);
    }
  }

  /**
   * Removes the child node at the given index.
   * @param index index of the child node to remove.
   */
  public void remove(int index) {
    ExplorerNode removedChild = children.get(index);
    children.remove(index);
    removedChild.setParent(null);
    getExplorer().nodeRemoved(this, removedChild, index);
  }

  /**
   * Removes the given child node.
   * @param child child node to remove.
   */
  public void remove(MutableTreeNode child) {
    int index = children.indexOf(child);
    if (index != -1) {
      remove(index);
    }
  }

  public int getChildCount() {
    return children.size();
  }

  public boolean getAllowsChildren() {
    return true;
  }

  public boolean isLeaf() {
    return children.size() == 0;
  }

  /**
   * Returns the list of children of the node as enumeration.
   * @return the list of children as enumeration.
   */
  public Enumeration children() {
    return Collections.enumeration(children);
  }

  /**
   * Returns the list of children of the node as unmodifiable list.
   * @return the list of children.
   */
  public List<ExplorerNode> getChildren() {
    return Collections.unmodifiableList(children);
  }

  public TreeNode getParent() {
    return parent;
  }

  public TreeNode getChildAt(int index) {
    return children.get(index);
  }

  public int getIndex(TreeNode node) {
    return children.indexOf(node);
  }

  /**
   * Returns the path from the root, to get to this node. The last element in
   * the path is this node.
   * @return an array of TreeNode objects giving the path, where the first
   *         element in the path is the root and the last element is this node.
   */
  public ExplorerNode[] getPath() {
    Stack<ExplorerNode> stack = new Stack<ExplorerNode>();
    ExplorerNode node = this;
    while (node != null) {
      stack.push(node);
      node = (ExplorerNode) node.getParent();
    }
    ExplorerNode[] path = new ExplorerNode[stack.size()];
    for (int i = 0; i < path.length; i++) {
      path[i] = stack.pop();
    }
    return path;
  }

  /**
   * Returns the root of the tree view to which this node belongs.
   * @return The root of the tree hierarchy.
   */
  public ExplorerNode getRoot() {
    ExplorerNode root = this;
    while (root.parent != null) {
      root = root.parent;
    }
    return root;
  }

  /**
   * Returns the explorer to whicht the node belongs.
   * @return the explorer owning the node.
   * @see #setExplorer
   */
  public Explorer getExplorer() {
    return getRoot().explorer;
  }

  /**
   * Sets the explorer to which the node belongs.
   * @param explorer new explorer owning the node.
   * @see #getExplorer
   */
  public void setExplorer(Explorer explorer) {
    this.explorer = explorer;
  }

  /**
   * @inheritDoc
   */
  public String toString() {
    return getName();
  }
}