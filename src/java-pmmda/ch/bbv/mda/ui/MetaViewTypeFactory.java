/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id: 
 */
package ch.bbv.mda.ui;

import java.util.Comparator;

import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.DataObjectType;
import ch.bbv.explorer.*;
import ch.bbv.mda.*;

/**
 * MetaViewTypeFactory defines how model elements are displayed within property selection and selected properties tree.
 * It also defines pop-up menus for these trees.
 *  
 * @author AdrianBachofen
 *
 */
public class MetaViewTypeFactory  extends AbstractTypeFactory {

  /**
   * The comparator for all elements of the MDA model.6Fs
   */
  private static Comparator<ExplorerNode> comparator = new MetaElementComparator();

  /**
   * Constructor of the class.
   * @param factory the user interface node type factory to use
   * @param manager the data object manager to use
   */
  public MetaViewTypeFactory(UiNodeTypeFactory factory, DataObjectMgr manager) {
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
    createNodeTypeForMetaView();
  }
  
  public void createNodeTypeForMetaView(){
    DataObjectType type = new DataObjectType(MetaView.class,
        MetaView.class, MetaView.class, VisitorFunctor.class,
        VisitorFunctor.class, null);
    type.setRoot(true);

    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name",
        MetaViewView.class);
    nodeType.setComparator(comparator);
    register(type, nodeType, new MetaViewPopupSelector(nodeType),
        new MetaClassIconSelector(getIconFactory(), "view"));
  }

  /**
   * Creates the node type for a bean type with fields and relations.
   */
  public void createNodeTypeForMetaModel() {
    DataObjectType type = new DataObjectType(MetaModel.class,
        MetaModel.class, MetaModel.class, VisitorFunctor.class,
        VisitorFunctor.class, null);
    type.setRoot(true);

    UiNodeTypeImpl nodeType = new UiNodeTypeImpl(type, "name",
        MetaModelView.class);
    nodeType.setComparator(comparator);
    register(type, nodeType, new MetaViewPopupSelector(nodeType),
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
    nodeType.setComparator(comparator);
    register(type, nodeType, new MetaViewPopupSelector(nodeType),
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
    nodeType.setComparator(comparator);
    register(type, nodeType, new MetaViewPopupSelector(nodeType),
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
    register(type, nodeType, new MetaViewPopupSelector(nodeType),
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
    register(type, nodeType, new MetaViewPopupSelector(nodeType),
        new FieldVisibilityIconSelector(getIconFactory(), "indexed-field"));
  }

}
