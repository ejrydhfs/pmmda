/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id: RemoveNodeAction.java,v 1.1 2004/08/10 17:36:18 marcelbaumann
 * Exp $
 */
package ch.bbv.explorer.actions;

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
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Constructor of the class.
   * @param type user interface node type on which the operation will be
   *          performed.
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
            if (parent.getDataObject() != null && node.getDataObject() != null
                && parent.getLightweightObject() != parent.getDataObject()) {
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

  /**
   * Retrieves the type identifier of the dataObject visualized by the
   * ExplorerNode. The type identifier or property is then used to obtain the
   * UiRelation between parent and child node to be deleted.
   * @param node ExplorerNode to be obtained the dataObject type identifier
   *          (property)
   * @return String property.
   */
  private String findProperty(ExplorerNode node) {
    ExplorerNode parentExplorerNode = (ExplorerNode) node.getParent();
    UiNodeType type = parentExplorerNode.getNodeType();
    Object object = node.getLightweightObject();
    Object parent = parentExplorerNode.getLightweightObject();
    String property = type.findProperty(parent, object);
    return property;
  }
}