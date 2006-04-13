/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer;

import javax.swing.JTree;

import ch.bbv.dog.DataObjectMgr;

/**
 * The class defines the interface of the owner of an explorer node tree. All
 * mechanisms to manage and expand the explorer node tree are defined in the
 * interface.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface ExplorerNodeOwner {
  /**
   * Called each type a node is inserted.
   * @param node inserted child node in this node
   * @param index index of the inserted child node
   */
  void nodeInserted(ExplorerNode node, int index);

  /**
   * Called each time a node is removed.
   * @param parent parent node of the removed node
   * @param node removed child node in the parent node
   * @param index index of the removed child node
   */
  void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index);

  /**
   * Called each type this node has changed. The menu bar is updated to handle
   * the modification of the displayed model.
   * @param node the modified node
   */
  void nodeChanged(ExplorerNode node);

  /**
   * Returns the user interface node type factory of the owner. The factory
   * provides the type information and visual aspects of the node types.
   * @return the user interface node type factory of the owner
   */
  UiNodeTypeFactory getNodeTypeFactory();

  /**
   * Returns the data object manager used in the owner. The manager provides
   * mechanisms to retrieve data objects if requested.
   * @return the data object manager used in the owner
   */
  DataObjectMgr getManager();

  /**
   * Returns the visual tree of the owner.
   * @return the graphical tree component of the owner
   */
  JTree getTree();
}
