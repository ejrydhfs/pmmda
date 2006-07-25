/* copyright 2000-2006 Marcel Baumann
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
public class ExplorerNode implements MutableTreeNode, Cloneable, Comparable {
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
   * @inv lwObject != null
   */
  private LightweightObject lwObject;

  /**
   * The node type describing the user interface aspect of the data object type
   * to which the data and lightweight objects belongs to.
   * @inv nodeType != null
   */
  private UiNodeType nodeType;

  /**
   * The owner of the node.
   */
  private ExplorerNodeOwner owner;

  /**
   * Constructor of the class.
   * @param nodeType type of the node displayed
   * @param lwObject lightweight object to display
   * @param boObject data object to display
   * @pre (nodeType != null) && (lwObject != null)
   */
  public ExplorerNode(UiNodeType nodeType, LightweightObject lwObject,
      DataObject boObject) {
    assert (nodeType != null) && (lwObject != null);
    children = new ArrayList<ExplorerNode>();
    this.nodeType = nodeType;
    setLightweightObject(lwObject);
    setDataObject(boObject);
  }

  /**
   * Returns the name of the node. The name is retrieved from the lightweight
   * object.
   * @return the name of the node as displayed in the explorer
   */
  public String getName() {
    return nodeType.getNameFor(lwObject);
  }

  /**
   * Returns the node type of the explorer node.
   * @return node type of the explorer node
   */
  public UiNodeType getNodeType() {
    return nodeType;
  }

  /**
   * Returns the data object associated with the explorer node.
   * @return the data object of the explorer node
   * @see #setDataObject(DataObject)
   */
  public DataObject getDataObject() {
    return boObject;
  }

  /**
   * Sets the data object of the explorer node.
   * @param boObject data object of the explorer node
   * @see #getDataObject()
   */
  public void setDataObject(DataObject boObject) {
    this.boObject = boObject;
  }

  /**
   * Returns the lightweight object associated with the explorer node.
   * @return the lightweight object of the explorer node
   * @see #setLightweightObject(LightweightObject)
   */
  public Object getLightweightObject() {
    return lwObject;
  }

  /**
   * Sets the lightweight object of the explorer node.
   * @param lwObject lightweight object of the explorer node
   * @see #getLightweightObject()
   */
  public void setLightweightObject(LightweightObject lwObject) {
    this.lwObject = lwObject;
  }

  public void removeFromParent() {
    parent.remove(this);
  }

  public void setUserObject(Object lwObject) {
    setLightweightObject((LightweightObject) lwObject);
  }

  public void setParent(MutableTreeNode parent) {
    this.parent = (ExplorerNode) parent;
  }

  /**
   * Adds a child at the end of the list of children of this node.
   * @param child child to add.
   * @see #getChildren()
   */
  public void addChild(ExplorerNode child) {
    TreeNode parent = this.getParent();
    if (parent != null && parent instanceof ExplorerNode) {
      remove(this);
    }
    child.setParent(this);
    int index = add(child);
    ExplorerNodeOwner owner = getNodeOwner();
    if (owner != null) {
      owner.nodeInserted(this, index);
    }
  }

  /**
   * Inserts a new node at the correct positon in the children list. Thereof we
   * always has a sorted tree.
   * @param child to be added
   * @return int index position where the new child has been inserted.
   */
  protected int add(ExplorerNode child) {
    int index;
    if (children == null) {
      children = new ArrayList<ExplorerNode>();
      index = 0;
    } else {
      Comparator<ExplorerNode> comparator = getNodeType().getComparator();
      index = Collections.binarySearch(children, child, comparator);
    }
    if (index < 0) {
      index = -index - 1;
    }
    children.add(index, child);
    return index;
  }

  /**
   * Inserts the child node at the given index in this node.
   * @param child child node to insert.
   * @param index index at which the child node is inserted.
   * @see #getChildAt(int)
   */
  public void insert(MutableTreeNode child, int index) {
    child.setParent(this);
    children.add(index, (ExplorerNode) child);
    ExplorerNodeOwner owner = getNodeOwner();
    if (owner != null) {
      owner.nodeInserted(this, index);
    }
  }

  /**
   * Handles the event that the node has changed.
   */
  public void nodeChanged() {
    TreeNode parent = this.getParent();
    if (parent != null && parent instanceof ExplorerNode) {
      ((ExplorerNode) parent).remove(this);
      ((ExplorerNode) parent).addChild(this);
    }
    ExplorerNodeOwner owner = getNodeOwner();
    if (owner != null) {
      owner.nodeChanged(this);
    }
  }

  /**
   * Removes the child node at the given index.
   * @param index index of the child node to remove
   */
  public void remove(int index) {
    ExplorerNode removedChild = children.get(index);
    children.remove(index);
    removedChild.setParent(null);
    ExplorerNodeOwner owner = getNodeOwner();
    if (owner != null) {
      getNodeOwner().nodeRemoved(this, removedChild, index);
    }
  }

  /**
   * Removes the given child node.
   * @param child child node to remove
   * @see #addChild(ExplorerNode)
   */
  public void remove(MutableTreeNode child) {
    int index = children.indexOf(child);
    if (index != -1) {
      remove(index);
    }
  }

  /**
   * @inheritDoc
   */
  public int getChildCount() {
    return children.size();
  }

  /**
   * @inheritDoc
   */
  public boolean getAllowsChildren() {
    return true;
  }

  /**
   * @inheritDoc
   */
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
   * @return the list of children
   */
  public List<ExplorerNode> getChildren() {
    return Collections.unmodifiableList(children);
  }

  /**
   * @inheritDoc
   */
  public TreeNode getParent() {
    return parent;
  }

  /**
   * @inheritDoc
   */
  public TreeNode getChildAt(int index) {
    return children.get(index);
  }

  /**
   * @inheritDoc
   */
  public int getIndex(TreeNode node) {
    return children.indexOf(node);
  }

  /**
   * Returns the path from the root, to get to this node. The last element in
   * the path is this node.
   * @return an array of TreeNode objects giving the path, where the first
   *         element in the path is the root and the last element is this node
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
   * @return The root of the tree hierarchy
   */
  public ExplorerNode getRoot() {
    ExplorerNode root = this;
    while (root.parent != null) {
      root = root.parent;
    }
    return root;
  }

  /**
   * Returns the owner of the node.
   * @return the explorer owning the node
   * @see #setNodeOwner(ExplorerNodeOwner)
   */
  public ExplorerNodeOwner getNodeOwner() {
    return getRoot().owner;
  }

  /**
   * Sets the explorer to which the node belongs.
   * @param owner new explorer owning the node
   * @see #getNodeOwner()
   */
  public void setNodeOwner(ExplorerNodeOwner owner) {
    this.owner = owner;
  }

  /**
   * @inheritDoc
   * @see Object#toString()
   */
  @Override
  public String toString() {
    return getName();
  }

  /**
   * @inheritDoc
   * @see Object#clone()
   */
  @Override
  public ExplorerNode clone() {
    ExplorerNode clone = null;
    try {
      clone = (ExplorerNode) super.clone();
      clone.children = new ArrayList<ExplorerNode>(children.size());
      for (ExplorerNode node : children) {
        clone.addChild(node.clone());
      }
    } catch (CloneNotSupportedException e) {
      // impossible error because the class implements cloneable
    }
    return clone;
  }

  /**
   * Implements the Comparable interface
   * @param o object to compare to
   * @return int - 0 corresponds to equal, -1 corresponds to less, 1 corresponds
   *         to more
   */
  public int compareTo(Object o) {
    if (this.getClass().equals(o.getClass())) {
      if (this.getName() == null)
        return 1;
      if (o instanceof ExplorerNode) {
        if (((ExplorerNode) o).getName() == null)
          return -1;
        return this.getName().compareTo(((ExplorerNode) o).getName());
      }
    }
    return -1;
  }
}
