/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.actions;

import ch.bbv.dog.LightweightObject;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.UiRelation;

/**
 * The action implements how an explorer node is removed from the navigator and
 * the associated lightweight object and data objects are removed from the data
 * models.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class RemoveNodeAction extends ExplorerNodeActionImpl {
  /**
   * Constructor of the class.
   * @param type user interface node type on which the operation will be 
   * performed.
   * @param name display name of the action.
   */
  public RemoveNodeAction(UiNodeType type, String name) {
    super(name, type);
  }

  public void execute(ExplorerNode node) {
    UiNodeType nodeType = getNodeType();
    if (nodeType.isStructureEditable(node.getLightweightObject())) {
      ExplorerNode parent = (ExplorerNode) node.getParent();
      if (parent != null) {
        String property = findProperty(node);
        if (property != null) {
          UiNodeType type = parent.getNodeType();
          UiRelation relation = type.getRelation(property);
          if (relation != null) {
            relation.removeProperty(parent.getLightweightObject(), node
                .getLightweightObject());
            if (parent.getDataObject() != null) {
              relation.removeProperty(parent.getDataObject(), node
                  .getDataObject());
            }
            parent.remove(node);
          }
        }
      }
    }
  }

  public void postExecute(ExplorerNode node, boolean shouldUndo) {
  }

  private String findProperty(ExplorerNode node) {
    ExplorerNode parent = (ExplorerNode) node.getParent();
    LightweightObject lwObject = (LightweightObject) node
        .getLightweightObject();
    LightweightObject lwParent = (LightweightObject) parent
        .getLightweightObject();
    UiNodeType type = parent.getNodeType();
    String property = type.findProperty(lwParent, lwObject);
    return property;
  }
}