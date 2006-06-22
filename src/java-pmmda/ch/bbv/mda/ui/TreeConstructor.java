/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.ui;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.UiNodeTypeFactory;
import ch.bbv.mda.*;

/**
 * The tree constructor constructs a navigation tree representation of a MDA
 * meta model.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class TreeConstructor implements Visitor {
  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(TreeConstructor.class);

  /**
   * The root node of the tree.
   */
  private ExplorerNode root;

  /**
   * The data object nodes of the graph to construct.
   */
  private Map<MetaElement, ExplorerNode> nodes;

  /**
   * The UI node type factory used to create the explorer nodes.
   */
  private UiNodeTypeFactory factory;

  /**
   * Constructor of the class.
   * @param factory factory used to create the explorer nodes.
   */
  public TreeConstructor(UiNodeTypeFactory factory) {
    nodes = new HashMap<MetaElement, ExplorerNode>();
    this.factory = factory;
  }

  /**
   * Returns the root of the explorer node graph.
   * @return the root of the graph to display.
   */
  public ExplorerNode getRoot() {
    return root;
  }

  public void visit(MetaModel metaModel) {
    UiNodeType nodeType = factory.getNodeTypeForBo(metaModel.getClass());
    root = new ExplorerNode(nodeType, metaModel, metaModel);
    nodes.put(metaModel, root);
  }

  public void visit(MetaPackage metaPackage) {
    constructNode(metaPackage);
  }

  public void visit(MetaClass metaClass) {
    constructNode(metaClass);
  }

  public void visit(MetaProperty metaProperty) {
    constructNode(metaProperty);
  }

  public void visit(MetaIndexedProperty metaIndexedProperty) {
    constructNode(metaIndexedProperty);
  }

  public void visit(MetaDatatype datatype) {
    assert datatype != null;
    constructNode(datatype);
  }

  public void visit(Stereotype stereotype) {
    assert stereotype != null;
    constructNode(stereotype);
  }

  public void visit(TaggedValue taggedValue) {
    assert taggedValue != null;
  }

  /**
   * Constructs an explorer node.
   * @param item item which explorer node should be constructed
   */
  private void constructNode(MetaElement item) {
    UiNodeType nodeType = factory.getNodeTypeForBo(item.getClass());
    ExplorerNode node = new ExplorerNode(nodeType, item, item);
    nodes.put(item, node);
    ExplorerNode context = nodes.get(item.getContext());
    if ((context == null) && log.isDebugEnabled()) {
      log.debug("Not context found for " + item + " context "
          + item.getContext());
    }
    if (context != null)
      context.addChild(node);
  }
}
