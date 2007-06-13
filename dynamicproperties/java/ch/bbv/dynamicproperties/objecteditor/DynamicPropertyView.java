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

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.ComplexType;
import ch.bbv.dynamicproperties.core.DynamicProperty;
import ch.bbv.dynamicproperties.core.ListType;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyType;
import ch.bbv.dynamicproperties.core.SimpleType;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.ExplorerNodeOwner;
import ch.bbv.explorer.GeneralExplorerTree;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.UiNodeTypeFactory;
import ch.bbv.explorer.GeneralExplorerTree.TreeElement;
import ch.bbv.explorer.components.SimpleDialog;

public class DynamicPropertyView extends SimpleDialog implements ExplorerNodeOwner, TreeSelectionListener {

    public static final String ROOT_TYPES = "Types";

    public static final String ROOT_PROPERTIES = "Properties";

    public DynamicPropertyView(JFrame owner, PropertyInstanceManager manager) {
        super(owner);
        init();
    }

    public DynamicPropertyView(PropertyInstanceManager manager) {
        this(null, manager);
    }

    public DynamicPropertyView() {
        this((JFrame) null);
    }

    public DynamicPropertyView(JFrame owner) {
        this(owner, PropertyInstanceManager.instance());
    }

    private DynamicPropertyTree dynamicPropertyTree;

    private DynamicTypeTree dynamicTypeTree;

    private UiNodeTypeFactory uiNodeTypeFactory;

    public static void main(String[] args) {
        new DynamicPropertyView().init();
    }

    private void init() {

        uiNodeTypeFactory = new UiNodeTypeFactory();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Properties", createDynamicPropertyTree());
        tabbedPane.add("Types", createDynamicTypeTree());
        tabbedPane.setPreferredSize(new Dimension(350, 500));
        JPanel panel = new JPanel();
        panel.add(tabbedPane);

        setView(panel);
        setModal(true);
        setVisible(true);
    }

    public JComponent createDynamicPropertyTree() {
        dynamicPropertyTree = new DynamicPropertyTree(uiNodeTypeFactory);
        dynamicPropertyTree.setRoot(createPropertyTree(ROOT_PROPERTIES), this);

        return dynamicPropertyTree.getPane();
    }

    public JComponent createDynamicTypeTree() {
        dynamicTypeTree = new DynamicTypeTree(uiNodeTypeFactory);
        dynamicTypeTree.setRoot(createTypeTree(ROOT_TYPES), this);
        return dynamicTypeTree.getPane();
    }

    private ExplorerNode createPropertyTree(String rootName) {
        GeneralExplorerTree.TreeElement treeElement = new GeneralExplorerTree.TreeElement();
        treeElement.setName(rootName);

        PropertyInstanceManager pim = PropertyInstanceManager.instance();

        UiNodeType rootNode = dynamicPropertyTree.createOrGetNodeTypeForAnObject(treeElement);
        rootNode.getDataObjectType().setRoot(true);

        ExplorerNode root = new ExplorerNode(rootNode, treeElement, treeElement);

        for (Class<? extends DynamicProperty> dynamicProperty : pim.getDataObjects()) {
            ExplorerNode dpRoot = createNode(dynamicProperty, dynamicProperty.getName());
            for (Property property : pim.getProperties(dynamicProperty)) {
                dpRoot.addChild(createNode(property, property.getName()));
            }
            root.addChild(dpRoot);
        }
        return root;
    }

    private ExplorerNode createTypeTree(String rootName) {
        GeneralExplorerTree.TreeElement treeElement = new GeneralExplorerTree.TreeElement();
        treeElement.setName(rootName);

        PropertyInstanceManager pim = PropertyInstanceManager.instance();

        UiNodeType rootNode = dynamicTypeTree.createOrGetNodeTypeForAnObject(treeElement);
        rootNode.getDataObjectType().setRoot(true);

        ExplorerNode root = new ExplorerNode(rootNode, treeElement, treeElement);

        for (PropertyType propertyType : pim.getTypes()) {
            root.addChild(createTypeNode(propertyType));
        }
        return root;
    }

    private ExplorerNode createTypeNode(PropertyType type) {
        if (type instanceof SimpleType) {
            return createTypeNode((SimpleType) type);
        } else if (type instanceof ListType) {
            return createTypeNode((ListType) type);
        } else if (type instanceof ComplexType) {
            return createTypeNode((ComplexType) type);
        }
        return null;
    }

    private ExplorerNode createTypeNode(SimpleType type) {
        ExplorerNode node = createTypeNode(type, type.getName());
        return node;
    }

    private ExplorerNode createTypeNode(ListType type) {
        ExplorerNode node = createTypeNode(type, type.getName());
        return node;
    }

    private ExplorerNode createTypeNode(ComplexType type) {
        ExplorerNode node = createTypeNode(type, type.getName());
        for (Property property : type.getProperties()) {
            node.addChild(createTypeNode(property));
        }
        return node;
    }

    private ExplorerNode createTypeNode(Property property) {
        ExplorerNode node = createTypeNode(property, property.getName());

        return node;
    }

    private ExplorerNode createTypeNode(Object obj, String displayName) {
        return dynamicTypeTree.createExplorerNode(obj, displayName);
    }

    private ExplorerNode createNode(Object obj, String displayName) {
        return dynamicPropertyTree.createExplorerNode(obj, displayName);
    }

    public DefaultTreeModel getPropertyTreeModel() {
        return dynamicPropertyTree.getTreeModel();
    }

    public DefaultTreeModel getTypeTreeModel() {
        return dynamicTypeTree.getTreeModel();
    }

    public ExplorerNode createTypeExplorerNode(Class clazz, Object content, String displayName) {
        return createExplorerNode(dynamicTypeTree.createOrGetNodeTypeForAnObject(clazz), displayName, content);
    }

    public ExplorerNode createPropertyExplorerNode(Class clazz, Object content, String displayName) {
        return createExplorerNode(dynamicPropertyTree.createOrGetNodeTypeForAnObject(clazz), displayName, content);
    }

    public ExplorerNode createExplorerNode(UiNodeType nodeType, String displayName, Object content) {
        TreeElement treeElement = new TreeElement();
        treeElement.setName(displayName);
        treeElement.setContent(content);
        return new ExplorerNode(nodeType, treeElement, treeElement);
    }

    public DataObjectMgr getManager() {
        // TODO Auto-generated method stub
        return null;
    }

    public UiNodeTypeFactory getNodeTypeFactory() {
        return uiNodeTypeFactory;
    }

    public JTree getTree() {
        return dynamicPropertyTree.getTree();
    }

    public void nodeChanged(ExplorerNode node) {
        // TODO Auto-generated method stub

    }

    public void nodeInserted(ExplorerNode node, int index) {
        // TODO Auto-generated method stub

    }

    public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
        // TODO Auto-generated method stub

    }

    public void valueChanged(TreeSelectionEvent arg0) {
        // TODO Auto-generated method stub

    }
}
