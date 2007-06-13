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

import ch.bbv.dynamicproperties.core.SimpleType;
import ch.bbv.dynamicproperties.objecteditor.dialog.SimpleTypeDialog;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.GeneralExplorerTree.TreeElement;

public class SimpleTypeEditAction extends DefaultAction {

    private static final String EDIT = "Edit";

    private SimpleType simpleType;

    private ExplorerNode node;

    public SimpleTypeEditAction(ExplorerNode node, SimpleType simpleType) {
        super(EDIT);
        this.simpleType = simpleType;
        this.node = node;
    }

    public void actionPerformed(ActionEvent event) {
        SimpleTypeDialog dialog = new SimpleTypeDialog(simpleType);
        dialog.setModal(true);
        dialog.setVisible(true);
        ((TreeElement) node.getDataObject()).setName(simpleType.getName());
    }
}
