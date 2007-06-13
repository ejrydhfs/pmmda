/*********************************************************************** 
 * Copyright 2007 BBV Software Services AG (http://www.bbv.ch)
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 * 
 * $Id$
 * 
 * History:
 * 2007/06/06      Ueli Kurmann     Initial Release
 ***********************************************************************/

package ch.bbv.dynamicproperties.objecteditor.action;

import java.awt.event.ActionEvent;

import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyType;
import ch.bbv.dynamicproperties.objecteditor.DynamicPropertyView;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.GeneralExplorerTree.TreeElement;

public class DeleteAction extends DefaultAction {

    ExplorerNode node;

    public DeleteAction(ExplorerNode node) {
        super("Delete");
        this.node = node;
    }

    public void actionPerformed(ActionEvent event) {
        if (((TreeElement) node.getDataObject()).getContent() instanceof PropertyType) {
            deletePropertyType();
        } else if (((TreeElement) node.getDataObject()).getContent() instanceof Property) {
            deleteProperty();
        }
    }

    private void deletePropertyType() {
        /**
         * Testen, ob der Type noch irgendwo verwendet wird
         */

        PropertyType type = (PropertyType) ((TreeElement) node.getDataObject()).getContent();
        if (PropertyInstanceManager.instance().removeType(type)) {
            ((DynamicPropertyView) node.getNodeOwner()).getTypeTreeModel().removeNodeFromParent(node);
        }
    }

    private void deleteProperty() {
        ((DynamicPropertyView) node.getNodeOwner()).getTypeTreeModel().removeNodeFromParent(node);
    }

}