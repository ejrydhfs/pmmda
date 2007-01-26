/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.nodeactions;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.UiRelation;
import ch.bbv.explorer.resources.Messages;

/**
 * The node action adds a new explorer node as last child of the node on which
 * the action is performed.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class AddNodeAction extends AbstractNodeAction {
  /**
   * Unique serial version identifier for the instances of the class.
   */
  private static final long serialVersionUID = 1349625017048612654L;

  /**
   * Constructor of the class.
   * @param type node type for which the action is defined
   * @param name name of the action. If the action is an add, the name must be
   *          the name of the property
   */
  public AddNodeAction(UiNodeType type, String name) {
    super(type, name, true);
  }

  @Override
  public void execute(ExplorerNode node) {
    createNode(node, getNodeType());
  }

  @Override
  public void postExecute(ExplorerNode node, boolean shouldUndo) {
    if (shouldUndo) {
      ((ExplorerNode) node.getParent()).remove(node);
    } else {
      ExplorerNode parent = (ExplorerNode) node.getParent();
      UiNodeType type = parent.getNodeType();
      UiRelation relation = type.getRelation(getName());
      relation.addProperty(parent.getLightweightObject(), node
          .getLightweightObject());
      if (parent.getLightweightObject() != parent.getDataObject()) {
        relation.addProperty(parent.getDataObject(), node.getDataObject());
      }
      node.nodeChanged();
      ExpandNodeAction expandAction = new ExpandNodeAction(type, Messages.actionExpandAll);
      expandAction.execute(parent);
    }
  }
}