/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import ch.bbv.dog.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * The tree construct functor constructs the graph of explorer nodes
 * representing a graph of lightweight objects. No constraints exist on the
 * tree.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class TreeConstructFunctor implements DoFunctor, LwFunctor {
  /**
   * Map containing all the explorer nodes created during the traversal of the
   * data object tree. The key is the data object identifier of the data object,
   * the value is the explorer node containing it.
   */
  private Map nodes;

  /**
   * Map containing all the explorer nodes created during the traversal of the
   * data object tree. The key is the data object, the value is the explorer
   * node containing it.
   */
  private Map doNodes;

  /**
   * Map containing all the explorer nodes created during the traversal of the
   * data object tree. The key is the lightweight data object, the value is the
   * explorer node containing it.
   */
  private Map lwNodes;

  /**
   * Stack containing the ancestors of the currently visited data object.
   */
  private Stack ancestors;

  private int depth;

  private UiNodeTypeFactory factory;

  private AbstractVisitorFunctor visitor;

  /**
   * Constructor of the class.
   * @param factory factory managing the user interfacer node type.
   * @param visitor visitor to apply.
   */
  public TreeConstructFunctor(UiNodeTypeFactory factory,
      AbstractVisitorFunctor visitor) {
    nodes = new HashMap();
    doNodes = new HashMap();
    lwNodes = new HashMap();
    ancestors = new Stack();
    this.factory = factory;
    this.visitor = visitor;
    reset();
  }

  /**
   * Resets the functor. The method should be called before traversing a tree if
   * it was applied before to another tree.
   */
  public void reset() {
    depth = 0;
  }

  /**
   * Returns the explorer node associated with the lightweigtht data object.
   * @param object lightweight object which node is requested.
   * @return the requested node if found otherwise null.
   * @pre object != null
   */
  public ExplorerNode retrieveNode(LightweightObject object) {
    assert (object != null);
    return (ExplorerNode) lwNodes.get(object);
  }

  /**
   * Returns the explorer node associated with the data object.
   * @param object lightweight object which node is requested.
   * @return the requested node if found otherwise null.
   * @pre object != null
   */
  public ExplorerNode retrieveNode(DataObject object) {
    assert (object != null);
    return (ExplorerNode) doNodes.get(object);
  }

  public void execute(DataObject object) {
    UiNodeType nodeType = factory.getNodeTypeForBo(object.getClass());
    Class lwClass = nodeType.getDataObjectType().getLwClass();

    if (object.getId() != null) {
      DataObjectId id = new DataObjectId(lwClass, object.getId());
      ExplorerNode node = (ExplorerNode) nodes.get(id);
      if (node != null) {
        doNodes.put(object, node);
        node.setDataObject(object);
      }
    }
  }

  public void execute(LightweightObject object) {
    Class clazz = object.getClass();
    UiNodeType nodeType = factory.getNodeTypeForLw(clazz);
    ExplorerNode node = new ExplorerNode(nodeType, object, null);
    lwNodes.put(object, node);
    if (object.getId() != null) {
      DataObjectId id = new DataObjectId(clazz, object.getId());
      nodes.put(id, node);
    }

    int currentDepth = visitor.getDepth();
    if (ancestors.size() >= currentDepth) {
      while (ancestors.size() >= currentDepth) {
        ancestors.pop();
      }
      depth = ancestors.size();
    }
    if (visitor.getDepth() > 1) {
      ExplorerNode context = retrieveNode((LightweightObject) ancestors.peek());
      context.addChild(node);
    }

    if (currentDepth == (depth + 1)) {
      depth++;
      ancestors.push(object);
    }
  }
}