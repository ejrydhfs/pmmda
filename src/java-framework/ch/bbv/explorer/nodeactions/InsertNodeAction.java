/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.nodeactions;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.UiRelation;

/**
 * The insert node action inserts a brother node before the selected node.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class InsertNodeAction extends AbstractNodeAction {
  /**
   * Constructor of the class.
   * @param type node type for which the action is defined.
   * @param name name of the action. If the action is an add, the name must be
   *        the name of the property.
   */
  public InsertNodeAction(UiNodeType type, String name) {
    super(type, name, true);
  }

  public void execute(ExplorerNode node) {
    createNode((ExplorerNode) node.getParent(), node.getNodeType());
  }

  public void postExecute(ExplorerNode node, boolean shouldUndo) {
    if (shouldUndo) {
      ((ExplorerNode) node.getParent()).remove(node);
    } else {
      ExplorerNode parent = (ExplorerNode) node.getParent();
      UiNodeType type = parent.getNodeType();
      int index = parent.getIndex(node);
      UiRelation relation = type.getRelation(getName());
      relation.setProperty(parent.getLightweightObject(), index, node
          .getLightweightObject());
      if (parent.getLightweightObject() != parent.getDataObject()) {
        relation.setProperty(parent.getDataObject(), index, node
            .getDataObject());
      }
      node.nodeChanged();
    }
  }
}