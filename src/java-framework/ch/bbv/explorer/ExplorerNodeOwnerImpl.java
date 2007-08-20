/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id:  
 */
package ch.bbv.explorer;

import javax.swing.JTree;
import ch.bbv.dog.DataObjectMgr;

/**
 * 
 * @author AdrianBachofen
 * Most simple implementation of a explorer node owner. If you provide this implementation then no manipulation of a tree is possible (because insert, remove, etc. is not implemented).
 * But the tree can be visualized and popups may be available depending on the UiNodeTypeFactory.
 */
public class ExplorerNodeOwnerImpl implements ExplorerNodeOwner {

  private UiNodeTypeFactory factory;
  
  public ExplorerNodeOwnerImpl(UiNodeTypeFactory factory){
    this.factory = factory;
  }

  public void nodeInserted(ExplorerNode node, int index) {
    assert false : "not implemented yet";
  }
  
  public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
    assert false : "not implemented yet";
  }
  
  public void nodeChanged(ExplorerNode node) {
    assert false : "not implemented yet";
  }
  
  public UiNodeTypeFactory getNodeTypeFactory() {
    return factory; 
  }

  public DataObjectMgr getManager() {
    assert false : "not implemented yet";
    return null;
  }

  public JTree getTree() {
    assert false : "not implemented yet";
    return null;
  }

}
