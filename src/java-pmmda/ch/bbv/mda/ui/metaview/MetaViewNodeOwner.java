/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id: 
 */
package ch.bbv.mda.ui.metaview;

import javax.swing.JTree;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.ExplorerNodeOwner;
import ch.bbv.explorer.ExplorerTree;
import ch.bbv.explorer.UiNodeTypeFactory;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaView;

public class MetaViewNodeOwner implements ExplorerNodeOwner{

  private MetaViewView metaViewView;
  private UiNodeTypeFactory factory;
  private DataObjectMgr manager;
  private ExplorerTree tree;
  
  public MetaViewNodeOwner(MetaViewView metaViewView, UiNodeTypeFactory factory, DataObjectMgr manager, ExplorerTree tree) {
    this.metaViewView = metaViewView;
    this.factory = factory;
    this.manager = manager;
    this.tree = tree;
  }
  
  @Override
  public DataObjectMgr getManager() {
    return manager;
  }

  @Override
  public UiNodeTypeFactory getNodeTypeFactory() {
    return factory;
  }

  @Override
  public JTree getTree() {
    return tree.getTree();
  }

  @Override
  public void nodeChanged(ExplorerNode node) {
    // TODO Auto-generated method stub

  }

  @Override
  public void nodeInserted(ExplorerNode node, int index) {
    int[] childIndices = new int[1];
    childIndices[0] = index;
    DataObject rootObject = node.getRoot().getDataObject();
    if(rootObject == null || rootObject instanceof MetaView) {
      if(metaViewView.getViewTree() != null && metaViewView.getViewTree().getTree() != null) {
        metaViewView.getViewTree().getTreeModel().nodesWereInserted(node, childIndices);
      }
    }
    if(rootObject == null || rootObject instanceof MetaModel) {
      if(metaViewView.getModelTree() != null && metaViewView.getModelTree().getTreeModel() != null) {
        metaViewView.getModelTree().getTreeModel().nodesWereInserted(node, childIndices);
      }
    }
  }

  @Override
  public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
    // TODO Auto-generated method stub
  }

}
