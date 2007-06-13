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
import ch.bbv.dynamicproperties.objecteditor.DynamicPropertyView;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.GeneralExplorerTree.TreeElement;

public class PropertyDeleteAction extends DefaultAction {

    private Property property;

    private ExplorerNode node;

    public PropertyDeleteAction(ExplorerNode node, Property property) {
        super("Remove");
        this.property = property;
        this.node = node;
    }

    public void actionPerformed(ActionEvent event) {
        ExplorerNode parent = (ExplorerNode) node.getParent();
        if (parent != null) {
            Class clazz = (Class) ((TreeElement) parent.getDataObject()).getContent();
            PropertyInstanceManager.instance().removeProperty(clazz, property);
            ((DynamicPropertyView) node.getNodeOwner()).getPropertyTreeModel().removeNodeFromParent(node);
        }
    }
}
