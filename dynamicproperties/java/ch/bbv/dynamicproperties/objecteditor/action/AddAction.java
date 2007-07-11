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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.ComplexType;
import ch.bbv.dynamicproperties.core.DynamicProperty;
import ch.bbv.dynamicproperties.core.ListType;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyType;
import ch.bbv.dynamicproperties.core.SimpleType;
import ch.bbv.dynamicproperties.objecteditor.DynamicPropertyView;
import ch.bbv.dynamicproperties.objecteditor.dialog.BasicDialog;
import ch.bbv.dynamicproperties.objecteditor.dialog.ComplexTypeDialog;
import ch.bbv.dynamicproperties.objecteditor.dialog.ListTypeDialog;
import ch.bbv.dynamicproperties.objecteditor.dialog.PropertyDialog;
import ch.bbv.dynamicproperties.objecteditor.dialog.SimpleTypeDialog;
import ch.bbv.dynamicproperties.visualization.editor.ComplexEditor;
import ch.bbv.dynamicproperties.visualization.editor.TableEditor;
import ch.bbv.dynamicproperties.visualization.viewer.ComplexViewer;
import ch.bbv.dynamicproperties.visualization.viewer.ListViewer;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.GeneralExplorerTree.TreeElement;

public class AddAction extends DefaultAction {

    ExplorerNode node;

    Class<?> clazz;

    public AddAction(ExplorerNode node, Class<?> clazz) {
        super("Add " + clazz.getSimpleName());
        this.node = node;
        this.clazz = clazz;
    }

    public void actionPerformed(ActionEvent event) {
        if (clazz.equals(SimpleType.class)) {
            createType(new SimpleType(), SimpleTypeDialog.class);
        } else if (clazz.equals(ComplexType.class)) {
            createType(new ComplexType("", ComplexViewer.class, ComplexEditor.class), ComplexTypeDialog.class);
        } else if (clazz.equals(ListType.class)) {
            createType(new ListType("", null, ListViewer.class, TableEditor.class), ListTypeDialog.class);
        } else if (clazz.equals(Property.class)) {
            createProperty(new Property(), PropertyDialog.class);
        }
    }

    private void createProperty(Property property, Class<? extends BasicDialog> dialogClass) {

        ExplorerNode newNode;

        if (((TreeElement) node.getDataObject()).getContent() instanceof ComplexType) {
            ComplexType complexType = (ComplexType) ((TreeElement) node.getDataObject()).getContent();
            complexType.addProperty(property);

            newNode = ((DynamicPropertyView) node.getNodeOwner()).createTypeExplorerNode(property.getClass(), property,
                    "null");

            ((DynamicPropertyView) node.getNodeOwner()).getTypeTreeModel().insertNodeInto(newNode, node,
                    node.getChildCount());

        } else {
            Class<? extends DynamicProperty> dynamicProperty = (Class<? extends DynamicProperty>) ((TreeElement) node.getDataObject()).getContent();
            PropertyInstanceManager.instance().addProperty(dynamicProperty, property);

            newNode = ((DynamicPropertyView) node.getNodeOwner()).createPropertyExplorerNode(property.getClass(),
                    property, "null");

            ((DynamicPropertyView) node.getNodeOwner()).getPropertyTreeModel().insertNodeInto(newNode, node,
                    node.getChildCount());
        }

        try {
            Constructor constructor = dialogClass.getConstructor(new Class[] { property.getClass() });
            BasicDialog dialog = (BasicDialog) constructor.newInstance(new Object[] { property });
            dialog.setModal(true);
            dialog.setVisible(true);
            ((TreeElement) newNode.getDataObject()).setName(property.getName());
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void createType(PropertyType type, Class<? extends BasicDialog> dialogClass) {

        PropertyInstanceManager.instance().addType(type);

        ExplorerNode newNode = ((DynamicPropertyView) node.getNodeOwner()).createTypeExplorerNode(type.getClass(),
                type, "null");
        ((DynamicPropertyView) node.getNodeOwner()).getTypeTreeModel().insertNodeInto(newNode, node,
                node.getChildCount());

        try {
            Constructor constructor = dialogClass.getConstructor(new Class[] { type.getClass() });
            BasicDialog dialog = (BasicDialog) constructor.newInstance(new Object[] { type });
            dialog.setModal(true);
            dialog.setVisible(true);
            ((TreeElement) newNode.getDataObject()).setName(type.getName());
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
