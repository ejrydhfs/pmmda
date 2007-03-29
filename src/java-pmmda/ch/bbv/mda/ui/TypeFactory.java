/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.DataObjectType;
import ch.bbv.explorer.AbstractTypeFactory;
import ch.bbv.explorer.ChildrenIconSelector;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.PopupSelectorDefault;
import ch.bbv.explorer.SimpleIconSelector;
import ch.bbv.explorer.UiField;
import ch.bbv.explorer.UiNodeTypeFactory;
import ch.bbv.explorer.UiNodeTypeImpl;
import ch.bbv.explorer.UiNodeViewType;
import ch.bbv.explorer.views.NodeTableView;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaDatatype;
import ch.bbv.mda.MetaElementComparator;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.MetaView;
import ch.bbv.mda.Stereotype;
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
   * The comparator for all elements of the MDA model.6Fs
   */
  private static Comparator<ExplorerNode> comparator = new MetaElementComparator();

  /**
   * Constructor of the class.
   * @param factory the user interface node type factory to use
   * @param manager the data object manager to use
   */
  public TypeFactory(UiNodeTypeFactory factory, DataObjectMgr manager) {
    super(factory, manager);
  }

  /**
   * Registers all data object types defined in the package.
   */
  public void registerAllTypes() {
    createNodeTypeForMetaModel();
    createNodeTypeForMetaPackage();
    createNodeTypeForMetaClass();
    createNodeTypeForMetaProperty();
    createNodeTypeForMetaIndexedProperty();
    createNodeTypeForMetaDataType();
    createNodeTypeForStereotype();
    createNodeTypeForMetaView();
  }
  
  public void createNodeTypeForMetaView(){
	  DataObjectType type = new DataObjectType(MetaView.class,
				MetaView.class, MetaView.class, VisitorFunctor.class,
				VisitorFunctor.class, null);
		type.setRoot(true);

		UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name",
				MetaViewView.class);
		
		List<UiField> params = new ArrayList<UiField>();
		addField(params, "name", String.class, true);
		addField(params, "system", Boolean.TYPE, false);
		addField(params, "description", String.class, true);
		nodeType.registerView(new UiNodeViewType("default", nodeType,
				NodeTableView.class, params, true));
		register(type, nodeType, new PopupSelectorDefault(nodeType),
				new MetaClassIconSelector(getIconFactory(), "view"));
		
  }

  /**
   * Creates the node type for a bean type with fields and relations.
   */
  public void createNodeTypeForMetaModel() {
    DataObjectType type = new DataObjectType(MetaModel.class, MetaModel.class,
        MetaModel.class, VisitorFunctor.class, VisitorFunctor.class, null);
    type.setRoot(true);

    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name",
        MetaModelView.class);
    addRelation(nodeType, "packages", MetaPackage.class, true, true);
    addRelation(nodeType, "classes", MetaClass.class, true, true);
    addRelation(nodeType, "datatypes", MetaDatatype.class, true, true);
    addRelation(nodeType, "stereotypes", Stereotype.class, true, true);
    addRelation(nodeType, "view", MetaView.class,true,true);

    List<UiField> params = new ArrayList<UiField>();
    addField(params, "name", String.class, true);
    addField(params, "system", Boolean.TYPE, false);
    addField(params, "description", String.class, true);
    nodeType.setComparator(comparator);
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
    addRelation(nodeType, "view", MetaView.class,true,true);
    List<UiField> params = new ArrayList<UiField>();
    addField(params, "name", String.class, true);
    addField(params, "description", String.class, true);
    nodeType.setComparator(comparator);
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
    addRelation(nodeType, "nestedClasses", MetaClass.class, true, true);

    List<UiField> params = new ArrayList<UiField>();
    addField(params, "name", String.class, true);
    addField(params, "description", String.class, true);
    nodeType.registerView(new UiNodeViewType("default", nodeType,
        NodeTableView.class, params, true));
    nodeType.setComparator(comparator);
    nodeType.registerView(new UiNodeViewType("class diagram", nodeType,
        ClassGraphView.class, null, false));
    nodeType.registerView(new UiNodeViewType("code values", nodeType,
        CodesView.class, null, false));
    register(type, nodeType, new PopupSelectorDefault(nodeType),
        new MetaClassIconSelector(getIconFactory(), "class"));
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
    nodeType.setComparator(comparator);
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
    nodeType.setComparator(comparator);
    nodeType.registerView(new UiNodeViewType("default", nodeType,
        NodeTableView.class, null, true));
    register(type, nodeType, new PopupSelectorDefault(nodeType),
        new FieldVisibilityIconSelector(getIconFactory(), "indexed-field"));
  }

  /**
   * Creates the node type for a bean type with fields and relations.
   */
  public void createNodeTypeForMetaDataType() {
    DataObjectType type = new DataObjectType(MetaDatatype.class,
        MetaDatatype.class, MetaDatatype.class, VisitorFunctor.class,
        VisitorFunctor.class, null);

    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name",
        MetaDatatypeView.class);
    nodeType.setComparator(comparator);
    nodeType.registerView(new UiNodeViewType("default", nodeType,
        NodeTableView.class, null, true));
    register(type, nodeType, new PopupSelectorDefault(nodeType),
        new SimpleIconSelector("datatype"));
  }

  /**
   * Creates the node type for a bean type with fields and relations.
   */
  public void createNodeTypeForStereotype() {
    DataObjectType type = new DataObjectType(Stereotype.class,
        Stereotype.class, Stereotype.class, VisitorFunctor.class,
        VisitorFunctor.class, null);

    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name",
        StereotypeView.class);
    nodeType.setComparator(comparator);
    nodeType.registerView(new UiNodeViewType("default", nodeType,
        NodeTableView.class, null, true));
    register(type, nodeType, new PopupSelectorDefault(nodeType),
        new SimpleIconSelector("stereotype"));
  }
}