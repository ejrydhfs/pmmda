/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * The class defines the user interface characteristics of a property containing
 * simple types, not data object types. The field can have multiple values.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class UiField {
  /**
   * Name of the property.
   */
  private String property;

  /**
   * Type of the property.
   */
  private Class type;

  /**
   * Flag indicating if the field is editable or not.
   */
  private boolean editable;

  /**
   * Constructor of the class.
   * @param property name of the property displayed in the field.
   * @param type the type of the property.
   * @param isEditable indicates if the field is editable or not.
   */
  public UiField(String property, Class type, boolean isEditable) {
    this.property = property;
    this.type = type;
    this.editable = isEditable;
  }

  /**
   * Returns the name of the property.
   * @return name of the property.
   * @see #UiField(String, Class, boolean)
   */
  public String getProperty() {
    return property;
  }

  /**
   * Returns the type of the property.
   * @return the type of the property.
   */
  public Class getType() {
    return type;
  }

  /**
   * Returns the flag indicating if the field is editable or not.
   * @return flag indicating if the field is editable.
   */
  public boolean isEditable() {
    return editable;
  }

  /**
   * Gets the property of a data object described in the node type.
   * @param object object which property should be retrieved.
   * @return the value of the requested property.
   * @pre object != null
   * @see #setProperty(Object, Object)
   */
  public Object getProperty(Object object) {
    assert (object != null);

    Object value = null;
    try {
      value = PropertyUtils.getProperty(object, property);
    } catch (InvocationTargetException ite) {
      ite.printStackTrace();
    } catch (NoSuchMethodException nsmi) {
      nsmi.printStackTrace();
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
    }

    return value;
  }

  /**
   * Sets the property of a data object described in the node type.
   * @param object object which property should be set.
   * @param value new value of the property.
   * @pre object != null
   * @see #getProperty(Object)
   */
  public void setProperty(Object object, Object value) {
    assert (object != null);
    try {
      PropertyUtils.setProperty(object, property, value);
    } catch (InvocationTargetException ite) {
      ite.printStackTrace();
    } catch (NoSuchMethodException nsmi) {
      nsmi.printStackTrace();
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
    }
  }
	}