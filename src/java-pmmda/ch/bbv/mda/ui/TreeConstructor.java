/* copyright 2000-2004 Marcel Baumann
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
  /** Logger of the class.
   */
  private static Log log = LogFactory.getLog(TreeConstructor.class);
  private ExplorerNode root;
  private Map nodes;
  private UiNodeTypeFactory factory;

  public TreeConstructor(UiNodeTypeFactory factory) {
    nodes = new HashMap();
    this.factory = factory;
  }

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

  public void visit(TaggedValue taggedValue) {
  }

  private void constructNode(MetaElement element) {
    UiNodeType nodeType = factory.getNodeTypeForBo(element.getClass());
    ExplorerNode node = new ExplorerNode(nodeType, element, element);
    nodes.put(element, node);

    ExplorerNode context = (ExplorerNode) nodes.get(element.getContext());
    if ((context == null) && log.isDebugEnabled()) {
      log.debug("Not context found for " + element + " context " +
        element.getContext());
    }
    context.addChild(node);
  }
}
