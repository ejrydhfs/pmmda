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

import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.objecteditor.dialog.PropertyDialog;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.GeneralExplorerTree.TreeElement;

public class PropertyEditAction extends DefaultAction {

    private Property property;

    private ExplorerNode node;

    public PropertyEditAction(ExplorerNode node, Property property) {
        super("Edit");
        this.property = property;
        this.node = node;
    }

    public void actionPerformed(ActionEvent event) {
        PropertyDialog dialog = new PropertyDialog(property);
        dialog.setModal(true);
        dialog.setVisible(true);
        ((TreeElement) node.getDataObject()).setName(property.getName());
    }
}
