/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.explorer;


/**
 * The node view defines the interface for all views used to display as
 * navigator views. A view implements a graphical representation of an explorer
 * node and its children. Each subclass of node view must provide a static
 * create method with a first parameter of type user interface node type and one
 * parameter of type object. These parameters are available to create a new
 * instance of the view. The developer is responsible to provide this method
 * because Java does not support definition of static method in interfaces. The
 * framework guarantees that the user interface node type of the explorer nodes
 * to display is always the same.
 * @author marcelbaumann
 * @version $Revision$
 */
public interface NodeView {
  /**
   * Sets the node which should be displayed in the view.
   * @param node new explorer node displayed in the view.
   */
  void setModel(ExplorerNode node);

  /**
   * Called when the view is closed.
   */
  void viewClosed();

  /**
   * Called when a node is inserted at the given position.
   * @param node node being inserted.
   * @param index index where the new node is inserted.
   */
  void nodeInserted(ExplorerNode node, int index);

  /**
   * Called when a node is removed.
   * @param parent parent of the removed node.
   * @param node node being removed.
   * @param index position of the removed node.
   */
  void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index);

  /**
   * Called when the node state changes.
   * @param node node being changed.
   */
  void nodeChanged(ExplorerNode node);
}