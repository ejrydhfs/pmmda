/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.ui;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.DataObjectManager;
import ch.bbv.dog.DataObjectType;
import ch.bbv.explorer.AbstractTypeFactory;
import ch.bbv.explorer.ChildrenIconSelector;
import ch.bbv.explorer.NodeTableView;
import ch.bbv.explorer.PopupSelectorDefault;
import ch.bbv.explorer.SimpleIconSelector;
import ch.bbv.explorer.UiField;
import ch.bbv.explorer.UiNodeTypeFactory;
import ch.bbv.explorer.UiNodeTypeImpl;
import ch.bbv.explorer.UiNodeViewType;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.VisitorFunctor;
import ch.bbv.mda.graphs.ClassGraphView;

/**
 * The type factory provides services to create the node types used in the MDA
 * user interface.
 * @author marcelbaumann
 * @version $Revision$
 */
public class TypeFactory extends AbstractTypeFactory {
  /**
   * Constructor of the class.
   * @param factory the user interface node type factory to use.
   * @param manager the data object manager to use.
   */
  public TypeFactory(UiNodeTypeFactory factory, DataObjectManager manager) {
    super(factory, manager);
  }

  /**
   * Registers all data object types defined in the package.
   */
  public void registerAllTypes() {
    createNodeTypeForMetaProject();
    createNodeTypeForMetaPackage();
    createNodeTypeForMetaClass();
    createNodeTypeForMetaProperty();
    createNodeTypeForMetaIndexedProperty();
  }

  /**
   * Creates the node type for a bean type with fields and relations.
   */
  public void createNodeTypeForMetaProject() {
    DataObjectType type = new DataObjectType(MetaModel.class, MetaModel.class,
        MetaModel.class, VisitorFunctor.class, VisitorFunctor.class, null);
    type.setRoot(true);

    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name",
        MetaModelView.class);
    addRelation(nodeType, "packages", MetaPackage.class, true, true);

    List<UiField> params = new ArrayList<UiField>();
    addField(params, "name", String.class, true);
    addField(params, "system", Boolean.TYPE, false);
    addField(params, "description", String.class, true);
    nodeType.registerView(new UiNodeViewType("default", nodeType,
        NodeTableView.class, params, true));
    register(type, nodeType, new PopupSelectorDefault(nodeType),
        new ChildrenIconSelector(getIconFactory(), "package"));
  }

  /**
   * Creates the node type for a bean type with fields and relations.
   */
  public void createNodeTypeForMetaPackage() {
    DataObjectType type = new DataObjectType(MetaPackage.class,
        MetaPackage.class, MetaPackage.class, VisitorFunctor.class,
        VisitorFunctor.class, null);
    type.setRoot(true);
    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name",
        MetaPackageView.class);
    addRelation(nodeType, "classes", MetaClass.class, true, true);

    List<UiField> params = new ArrayList<UiField>();
    addField(params, "name", String.class, true);
    addField(params, "description", String.class, true);
    nodeType.registerView(new UiNodeViewType("default", nodeType,
        NodeTableView.class, params, true));
    nodeType.registerView(new UiNodeViewType("class diagram", nodeType,
        ClassGraphView.class, null, false));
    register(type, nodeType, new PopupSelectorDefault(nodeType),
        new ChildrenIconSelector(getIconFactory(), "package"));
  }

  /**
   * Creates the node type for a bean type with fields and relations.
   */
  public void createNodeTypeForMetaClass() {
    DataObjectType type = new DataObjectType(MetaClass.class, MetaClass.class,
        MetaClass.class, VisitorFunctor.class, VisitorFunctor.class, null);
    type.setRoot(true);
    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name",
        MetaClassView.class);
    addRelation(nodeType, "properties", MetaProperty.class, true, true);
    addRelation(nodeType, "indexedProperties", MetaIndexedProperty.class, true,
        true);
    addRelation(nodeType, "classes", MetaClass.class, true, true);

    List<UiField> params = new ArrayList<UiField>();
    addField(params, "name", String.class, true);
    addField(params, "description", String.class, true);
    nodeType.registerView(new UiNodeViewType("default", nodeType,
        NodeTableView.class, params, true));
    nodeType.registerView(new UiNodeViewType("class diagram", nodeType,
        ClassGraphView.class, null, false));
    register(type, nodeType, new PopupSelectorDefault(nodeType),
        new SimpleIconSelector("class-regular"));
  }

  /**
   * Creates the node type for a bean type with fields and relations.
   */
  public void createNodeTypeForMetaProperty() {
    DataObjectType type = new DataObjectType(MetaProperty.class,
        MetaProperty.class, MetaProperty.class, VisitorFunctor.class,
        VisitorFunctor.class, null);
    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name",
        MetaPropertyView.class);
    nodeType.registerView(new UiNodeViewType("default", nodeType,
        NodeTableView.class, null, true));
    register(type, nodeType, new PopupSelectorDefault(nodeType),
        new FieldVisibilityIconSelector(getIconFactory(), "field"));
  }

  /**
   * Creates the node type for a bean type with fields and relations.
   */
  public void createNodeTypeForMetaIndexedProperty() {
    DataObjectType type = new DataObjectType(MetaIndexedProperty.class,
        MetaIndexedProperty.class, MetaIndexedProperty.class,
        VisitorFunctor.class, VisitorFunctor.class, null);
    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name",
        MetaPropertyView.class);
    nodeType.registerView(new UiNodeViewType("default", nodeType,
        NodeTableView.class, null, true));
    register(type, nodeType, new PopupSelectorDefault(nodeType),
        new FieldVisibilityIconSelector(getIconFactory(), "indexed-field"));
  }
}