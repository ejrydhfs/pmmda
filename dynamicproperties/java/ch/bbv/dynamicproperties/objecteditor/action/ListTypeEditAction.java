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

import ch.bbv.dynamicproperties.core.ListType;
import ch.bbv.dynamicproperties.objecteditor.dialog.ListTypeDialog;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.GeneralExplorerTree.TreeElement;

public class ListTypeEditAction extends DefaultAction {

    private static final String EDIT = "Edit";

    private ListType listType;

    private ExplorerNode node;

    public ListTypeEditAction(ExplorerNode node, ListType listType) {
        super(EDIT);
        this.listType = listType;
        this.node = node;
    }

    public void actionPerformed(ActionEvent event) {
        ListTypeDialog dialog = new ListTypeDialog(listType);
        dialog.setModal(true);
        dialog.setVisible(true);
        ((TreeElement) node.getDataObject()).setName(listType.getName());
        node.removeAllChildren();
    }
}
