/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui;

import java.util.*;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.UiNodeTypeFactory;
import ch.bbv.mda.*;

/**
 * Constructs a tree such as TreeConstructor but using one or several filters To
 * set filter use addFilter before applying this visitor to the model.
 * @author AdrianBachofen
 * @version $revision$
 */
public class FilterTreeConstructor implements Visitor {

  private ExplorerNode root;

  private Map nodes;

  private UiNodeTypeFactory factory;

  private Vector filters;

  /**
   * Constructor for the FilterTreeConstructor
   * @param factory used by the TreeConstructor to create tree nodes.
   */
  public FilterTreeConstructor(UiNodeTypeFactory factory) {
    nodes = new HashMap();
    filters = new Vector();
    this.factory = factory;
    root = null;
  }

  /**
   * Adds filters based on which a tree shall be constructed. Only if filter
   * criteria are fullfilled a tree node will be created. The filter rules are
   * ored. Only one of the filter criteria has to match.
   * @param tvf TaggedValueFilter declaring a filter rule
   */
  public void addFilter(TaggedValueFilter tvf) {
    filters.add(tvf);
  }

  /**
   * Returns the root of the constructed tree (after visiting the model).
   * @return null if not yet visited, root of tree after visiting.
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
    constructNodeIfFilterRuleFullfilled(metaPackage);
  }

  public void visit(MetaClass metaClass) {
    constructNodeIfFilterRuleFullfilled(metaClass);
  }

  public void visit(MetaProperty metaProperty) {
    constructNodeIfFilterRuleFullfilled(metaProperty);
  }

  public void visit(MetaIndexedProperty metaIndexedProperty) {
    constructNodeIfFilterRuleFullfilled(metaIndexedProperty);
  }

  public void visit(TaggedValue taggedValue) {
  }

  /**
   * Constructs a node if at least one filter rule is fullfilled.
   * @param element MetaElement to investigate and if filter rule ok then it
   *        constructs a tree node.
   */
  private void constructNodeIfFilterRuleFullfilled(MetaElement element) {
    if (!filterRuleFullfilled(element))
      return;
    constructNode(element);
  }

  /**
   * Constructs a tree node for the element passed
   * @param element MetaModel element to construct a tree node for.
   */
  private void constructNode(MetaElement element) {
    UiNodeType nodeType = factory.getNodeTypeForBo(element.getClass());
    ExplorerNode node = new ExplorerNode(nodeType, element, element);
    nodes.put(element, node);

    ExplorerNode context = (ExplorerNode) nodes.get(element.getContext());
    if (context == null) {
      constructNode(element.getContext());
      context = (ExplorerNode) nodes.get(element.getContext());
    }
    if (context != null)
      context.addChild(node);
  }

  /**
   * Tests whether one of the filter rules match
   * @param element MetaElement to investigate
   * @return true if at least one filter rule match, otherwise false
   */
  private boolean filterRuleFullfilled(MetaElement element) {
    boolean fullfilled = false;
    for (Iterator i = filters.iterator(); i.hasNext();) {
      TaggedValueFilter tvf = (TaggedValueFilter) i.next();
      fullfilled |= tvf.filterRuleFullfilled(element);
      if (fullfilled)
        break;
    }
    return fullfilled;
  }
}