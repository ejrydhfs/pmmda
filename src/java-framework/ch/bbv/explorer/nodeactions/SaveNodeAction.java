/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.nodeactions;

import javax.swing.JOptionPane;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.dog.DataObjectType;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.actions.ExplorerNodeActionImpl;

/**
 * The save action the data object graph, which root is in the explorer node.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class SaveNodeAction extends ExplorerNodeActionImpl {
  /**
   * Constructor of the class.
   * @param type node type for which the action is defined.
   * @param name name of the action. If the action is an add, the name must be
   *        the name of the property.
   */
  public SaveNodeAction(UiNodeType type, String name) {
    super(name, type);
  }

  public void execute(ExplorerNode node) {
    DataObject object = (DataObject) node.getDataObject();
    Explorer explorer = node.getExplorer();
    if ((object == null) || !object.isModified()) {
      JOptionPane.showMessageDialog(explorer.getFrame(),
          "Object was not modified.", "Save Operation",
          JOptionPane.WARNING_MESSAGE);
    } else if (object.isNew() && hasRootOwner(node)) {
      JOptionPane.showMessageDialog(explorer.getFrame(),
          "New Object must be saved from owner object.", "Save Operation",
          JOptionPane.WARNING_MESSAGE);
    } else {
      DataObjectType type = node.getNodeType().getDataObjectType();
      DataObjectHandler handler = explorer.getManager().getDataObjectHandler();
      handler.store(explorer.getDatasource(), type.getBoClass(), object);
    }
  }

  /**
   * Returns true if the node has an owning root which is not not new.
   * @param node node to infer.
   * @return true if a root node was found otherwise false.
   */
  private boolean hasRootOwner(ExplorerNode node) {
    ExplorerNode root = node;
    boolean continues = true;
    while (continues && (root != null)) {
      DataObject boOwner = (DataObject) root.getDataObject();
      if (root.getNodeType().getDataObjectType().isRoot() && !boOwner.isNew()) {
        continues = false;
      } else {
        root = (ExplorerNode) root.getParent();
      }
    }
    return (root != null);
  }

}