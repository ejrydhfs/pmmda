/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.util.*;

import ch.bbv.dog.*;

/**
 * The tree construct functor constructs the graph of explorer nodes
 * representing a graph of lightweight objects. No constraints exist on the
 * tree. The algorithm uses the declarative information of the node type factory
 * to infer if a lightweight node should be mapped to an explorer node or not.
 * Care is taken that only nodes part of the user interface tree are traversed
 * in both cases. Only this approach guarantees that the same topological
 * subtree is traversed for the business object and the lightweight business
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

  private Map<Integer, ExplorerNode> orderedNodes;

  /**
   * Map containing all the explorer nodes created during the traversal of the
   * data object tree. The key is the data object, the value is the explorer
   * node containing it.
   */
  private Map<DataObject, ExplorerNode> doNodes;

  /**
   * The set of all objects which have an explorer node representation. This set
   * is used to filter the objects tree and selects the visible nodes based on
   * the user interface declarative configuration stored in the node type
   * factory.
   */
  private Set<DataObject> explorerDoNodes;

  /**
   * Map containing all the explorer nodes created during the traversal of the
   * data object tree. The key is the lightweight data object, the value is the
   * explorer node containing it.
   */
  private Map<LightweightObject, ExplorerNode> lwNodes;

  /**
   * The set of all lightweight objects which have an explorer node
   * representation. This set is used to filter the lightweight objects tree and
   * selects the visible nodes based on the user interface declarative
   * configuration stored in the node type factory.
   */
  private Set<LightweightObject> explorerLwNodes;

  /**
   * Stack containing the ancestors of the currently visited data object.
   */
  private Stack<LightweightObject> ancestors;

  /**
   * Depth of the tree traversal.
   */
  private int depth;

  /**
   * Running counter used to identify corresponding nodes in homomorphique
   * trees.
   */
  private int counter;

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
    orderedNodes = new HashMap<Integer, ExplorerNode>();
    doNodes = new HashMap<DataObject, ExplorerNode>();
    explorerDoNodes = new HashSet<DataObject>();
    lwNodes = new HashMap<LightweightObject, ExplorerNode>();
    explorerLwNodes = new HashSet<LightweightObject>();
    ancestors = new Stack<LightweightObject>();
    this.factory = factory;
    reset(visitor);
  }

  /**
   * Resets the functor. The method should be called before traversing a tree if
   * it was applied before to another tree.
   */
  public void reset() {
    depth = 0;
    counter = 0;
  }

  /**
   * Resets the functor. The method should be called before traversing a tree if
   * it was applied before to another tree and another visitor is used to
   * traverse a structural equal tree.
   * @param visitor new visitor using the functor.
   */
  public void reset(AbstractVisitorFunctor visitor) {
    this.visitor = visitor;
    visitor.setFunctor(this);
    reset();
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
    inferChildren(object);
    if (explorerDoNodes.contains(object)) {
      counter++;
      UiNodeType nodeType = factory.getNodeTypeForBo(object.getClass());
      if (nodeType != null) {
        Class lwClass = nodeType.getDataObjectType().getLwClass();
        ExplorerNode node = null;
        if (object.getId() != null) {
          node = nodes.get(new DataObjectId(lwClass, object.getId()));
        } else {
          node = orderedNodes.get(counter);
        }
        if (node != null) {
          doNodes.put(object, node);
          node.setDataObject(object);
        }
      }
    }
  }

  public void execute(LightweightObject object) {
    assert object != null;
    ExplorerNode node = null;
    inferChildren(object);
    if (explorerLwNodes.contains(object)) {
      counter++;
      Class clazz = object.getClass();
      UiNodeType nodeType = factory.getNodeTypeForLw(clazz);
      node = new ExplorerNode(nodeType, object, null);
      lwNodes.put(object, node);
      if (object.getId() != null) {
        DataObjectId id = new DataObjectId(clazz, object.getId());
        nodes.put(id, node);
      }
      orderedNodes.put(counter, node);
    }
    int currentDepth = visitor.getDepth();
    if (ancestors.size() >= currentDepth) {
      while (ancestors.size() >= currentDepth) {
        ancestors.pop();
      }
      depth = ancestors.size();
    }
    if ((node != null) && (visitor.getDepth() > 1)) {
      ExplorerNode context = retrieveNode(ancestors.peek());
      context.addChild(node);
    }
    if (currentDepth == (depth + 1)) {
      depth++;
      ancestors.push(object);
    }
  }

  /**
   * Infers the children of the object which are displayed as nodes in the
   * explorer. Only children of relations are displayed and relations have
   * always lightweight objects.
   * @param object object which visible lightweight objects should be infered.
   */
  private void inferChildren(LightweightObject object) {
    UiNodeType nodeType = factory.getNodeTypeForLw(object.getClass());
    if (visitor.getDepth() == 1) {
      // root object is always displayed
      explorerLwNodes.add(object);
    }
    if (explorerLwNodes.contains(object)) {
      // only children of displayed objects are relevant
      for (UiRelation relation : nodeType.getRelations()) {
        for (LightweightObject child : (Collection<LightweightObject>) relation
            .getValues(object)) {
          explorerLwNodes.add(child);
        }
      }
    }
  }

  /**
   * Infers the children of the object which are displayed as nodes in the
   * explorer. Only children of relations are displayed and relations have
   * always data objects.
   * @param object object which visible data objects should be infered.
   */
  private void inferChildren(DataObject object) {
    UiNodeType nodeType = factory.getNodeTypeForBo(object.getClass());
    if (visitor.getDepth() == 1) {
      // root object is always displayed
      explorerDoNodes.add(object);
    }
    if (explorerDoNodes.contains(object)) {
      // only children of displayed objects are relevant
      for (UiRelation relation : nodeType.getRelations()) {
        for (DataObject child : (Collection<DataObject>) relation
            .getValues(object)) {
          explorerDoNodes.add(child);
        }
      }
    }
  }
}