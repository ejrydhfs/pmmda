/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.util.List;

import ch.bbv.dog.DataObjectManager;

/**
 * The abstract type factory provides an infrastructure to define a set of user
 * interface nodes with their relations and fields.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class AbstractTypeFactory {
  /**
   * The user interface node type factory used to register the created types.
   */
  protected UiNodeTypeFactory factory;

  /**
   * The data object manager to retrieve data.
   */
  protected DataObjectManager manager;

  /**
   * The icon manager for the created types.
   */
  protected IconFactory iconFactory;

  /**
   * Constructor of the class.
   * @param factory node type factory used to register the created type.
   * @param manager the data object manager used to retrieve data.
   */
  protected AbstractTypeFactory(UiNodeTypeFactory factory,
      DataObjectManager manager) {
    this.factory = factory;
    this.manager = manager;
    iconFactory = factory.getIconFactory();
  }

  /** 
   * Creates and adds a user interface field to the list.
   * @param list list to which the field should be added.
   * @param property name of the property visualize through the field.
   * @param clazz class containing the property.
   * @param isWritable flag indicating if the field can be written.
   */
  protected void addField(List list, String property, Class clazz,
      boolean isWritable) {
    UiField uiProperty = new UiField(property, clazz, isWritable);
    list.add(uiProperty);
  }

  /** 
   * Creates and adds a user interface relation to the list.
   * @param type list to which the field should be added.
   * @param property name of the property visualize through the field.
   * @param clazz class containing the property.
   * @param isWritable flag indicating if the field can be written.
   * @param isMultiple flag indicating if the relation has multiple values.
   */
  protected void addRelation(UiNodeTypeImpl type, String property, Class clazz,
      boolean isWritable, boolean isMultiple) {
    UiRelation relation = new UiRelation(property, clazz, isWritable,
        isMultiple);
    type.addRelation(relation);
  }
}