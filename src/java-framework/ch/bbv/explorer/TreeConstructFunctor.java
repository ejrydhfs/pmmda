/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import ch.bbv.dog.*;

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
  private Map<DataObjectId, ExplorerNode> nodes;

  /**
   * Map containing all the explorer nodes created during the traversal of the
   * data object tree. The key is the data object, the value is the explorer
   * node containing it.
   */
  private Map<DataObject, ExplorerNode> doNodes;

  /**
   * Map containing all the explorer nodes created during the traversal of the
   * data object tree. The key is the lightweight data object, the value is the
   * explorer node containing it.
   */
  private Map<LightweightObject, ExplorerNode> lwNodes;

  /**
   * Stack containing the ancestors of the currently visited data object.
   */
  private Stack<LightweightObject> ancestors;

  /**
   * Depth of the tree traversal.
   */
  private int depth;

  /**
   * User interface node type factory used to create nodes during traversal.
   */
  private UiNodeTypeFactory factory;

  /**
   * The visitor functor using the functor to process the graph.
   */
  private AbstractVisitorFunctor visitor;

  /**
   * Constructor of the class.
   * @param factory factory managing the user interfacer node type.
   * @param visitor visitor applying the functor.
   */
  public TreeConstructFunctor(UiNodeTypeFactory factory,
      AbstractVisitorFunctor visitor) {
    nodes = new HashMap<DataObjectId, ExplorerNode>();
    doNodes = new HashMap<DataObject, ExplorerNode>();
    lwNodes = new HashMap<LightweightObject, ExplorerNode>();
    ancestors = new Stack<LightweightObject>();
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
    assert object != null;
    return lwNodes.get(object);
  }

  /**
   * Returns the explorer node associated with the data object.
   * @param object lightweight object which node is requested.
   * @return the requested node if found otherwise null.
   * @pre object != null
   */
  public ExplorerNode retrieveNode(DataObject object) {
    assert object != null;
    return doNodes.get(object);
  }

  /**
   * Returns the explorer node associated with the object identity.
   * @param object lightweight object which node is requested.
   * @return the requested node if found otherwise null.
   * @pre object != null
   * @see DataObjectId
   */
  public ExplorerNode retrieveNodeById(HasId object) {
    assert object != null;
    DataObjectId id = new DataObjectId(object.getClass(), object.getId());
    return nodes.get(id);
  }

  public void execute(DataObject object) {
    assert object != null;
    UiNodeType nodeType = factory.getNodeTypeForBo(object.getClass());
    Class lwClass = nodeType.getDataObjectType().getLwClass();

    if (object.getId() != null) {
      DataObjectId id = new DataObjectId(lwClass, object.getId());
      ExplorerNode node = nodes.get(id);
      if (node != null) {
        doNodes.put(object, node);
        node.setDataObject(object);
      }
    }
  }

  public void execute(LightweightObject object) {
    assert object != null;
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
      ExplorerNode context = retrieveNode(ancestors.peek());
      context.addChild(node);
    }
    if (currentDepth == (depth + 1)) {
      depth++;
      ancestors.push(object);
    }
  }
}