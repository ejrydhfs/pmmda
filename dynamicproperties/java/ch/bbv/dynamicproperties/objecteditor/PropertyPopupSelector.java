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

package ch.bbv.dynamicproperties.objecteditor;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ch.bbv.dynamicproperties.core.ComplexType;
import ch.bbv.dynamicproperties.core.ListType;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.SimpleType;
import ch.bbv.dynamicproperties.objecteditor.action.AddAction;
import ch.bbv.dynamicproperties.objecteditor.action.ComplexTypeEditAction;
import ch.bbv.dynamicproperties.objecteditor.action.DeleteAction;
import ch.bbv.dynamicproperties.objecteditor.action.ListTypeEditAction;
import ch.bbv.dynamicproperties.objecteditor.action.PropertyDeleteAction;
import ch.bbv.dynamicproperties.objecteditor.action.PropertyEditAction;
import ch.bbv.dynamicproperties.objecteditor.action.SimpleTypeEditAction;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.PopupSelector;
import ch.bbv.explorer.GeneralExplorerTree.TreeElement;

public class PropertyPopupSelector implements PopupSelector {

    public JPopupMenu select(ExplorerNode node) {
        TreeElement treeElement = (TreeElement) node.getDataObject();

        JPopupMenu popupMenu = new JPopupMenu();

        if (treeElement.getContent() instanceof Property
                && node.getRoot().toString().equals(DynamicPropertyView.ROOT_PROPERTIES)) {
            Property property = (Property) treeElement.getContent();
            popupMenu.add(new JMenuItem(new PropertyEditAction(node, property)));
            popupMenu.add(new JMenuItem(new PropertyDeleteAction(node, property)));

        } else if (treeElement.getContent() instanceof Property
                && node.getRoot().toString().equals(DynamicPropertyView.ROOT_TYPES)) {
            Property property = (Property) treeElement.getContent();
            popupMenu.add(new JMenuItem(new PropertyEditAction(node, property)));
            popupMenu.add(new JMenuItem(new DeleteAction(node)));

        } else if (treeElement.getContent() instanceof Class) {
            popupMenu.add(new JMenuItem(new AddAction(node, Property.class)));

        } else if (node.toString().equals(DynamicPropertyView.ROOT_TYPES)) {
            popupMenu.add(new JMenuItem(new AddAction(node, SimpleType.class)));
            popupMenu.add(new JMenuItem(new AddAction(node, ListType.class)));
            popupMenu.add(new JMenuItem(new AddAction(node, ComplexType.class)));

        } else if (treeElement.getContent() instanceof SimpleType) {
            SimpleType simpleType = (SimpleType) treeElement.getContent();
            popupMenu.add(new JMenuItem(new SimpleTypeEditAction(node, simpleType)));
            popupMenu.add(new JMenuItem(new DeleteAction(node)));

        } else if (treeElement.getContent() instanceof ListType) {
            ListType listType = (ListType) treeElement.getContent();
            popupMenu.add(new JMenuItem(new ListTypeEditAction(node, listType)));
            popupMenu.add(new JMenuItem(new DeleteAction(node)));

        } else if (treeElement.getContent() instanceof ComplexType) {
            ComplexType complexType = (ComplexType) treeElement.getContent();
            popupMenu.add(new JMenuItem(new AddAction(node, Property.class)));
            popupMenu.add(new JMenuItem(new ComplexTypeEditAction(node, complexType)));
            popupMenu.add(new JMenuItem(new DeleteAction(node)));
        }

        return popupMenu;
    }
}
