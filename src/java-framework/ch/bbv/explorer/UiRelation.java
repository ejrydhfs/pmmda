/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import ch.bbv.utilities.ReflectUtilities;
import ch.bbv.utilities.TextUtilities;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

/**
 * The class defines the user interface characteristics of a displayed
 * relation. A relation is a visible link in the tree view displayed in the left
 * navigator pane. The following conventions are used.
 * <ul>
 * <li>The name of the displayed node is inferred from the name property of the
 * node type describing the data objec type displayed.</li>
 * <li>The relation can have a cardinality of one or of N. If the relation is
 * multiple and editable, a context menu is provided to add, insert and remove
 * nodes.</li>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class UiRelation {
  public static final String ADD_PREFIX = "add";

  public static final String REMOVE_PREFIX = "remove";

  public static final String GET_ALL_PREFIX = "get";

  /**
   * Name of the indexed or mapped property.
   */
  private String property;

  private String addMethodName;

  private String removeMethodName;

  private String getAllMethodName;

  /**
   * Class of the lightweight data object stored in the property. If the
   * property contains heteregeneous instances the value should be null.
   */
  private Class lwClass;

  /**
   * Flag indicating if the property is editable.
   */
  private boolean isEditable;

  /**
   * Flag indicating if the property has multiple values.
   */
  private boolean isMultiple;

  public UiRelation(String property, Class lwClass, boolean isEditable,
      boolean isMultiple) {
    this.property = property;
    this.lwClass = lwClass;
    this.isEditable = isEditable;
    this.isMultiple = isMultiple;

    addMethodName = ADD_PREFIX
        + TextUtilities.identifierSingular(TextUtilities
            .identifierFirstUpper(TextUtilities.removeSpecialCharacters(property)));

    removeMethodName = REMOVE_PREFIX
        + TextUtilities.identifierSingular(TextUtilities
            .identifierFirstUpper(TextUtilities.removeSpecialCharacters(property)));

    getAllMethodName = GET_ALL_PREFIX
        + TextUtilities.identifierPlural(TextUtilities
            .identifierFirstUpper(TextUtilities.removeSpecialCharacters(property)));
  }

  public String getProperty() {
    return property;
  }

  public Class getLwClass() {
    return lwClass;
  }

  public boolean isEditable() {
    return isEditable;
  }

  public boolean isMultiple() {
    return isMultiple;
  }

  /**
   * Gets the indexed property of a data object described in the node type.
   * @param bean object which property should be retrieved.
   * @param index index of the requested value.
   * @return the value of the requested property.
   * @pre bean != null
   */
  public Object getProperty(Object bean, int index) {
    assert ((bean != null) && isMultiple);

    Object value = null;
    try {
      value = PropertyUtils.getIndexedProperty(bean, property, index);
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
   * Sets the indexed property of a data object described in the node type.
   * @param bean object which property should be retrieved.
   * @param index index of the requested value. If the value is higher than the
   *        size of the collection when the value is inserted at the end. If the
   *        value is negative the value is inserted at the beginning.
   * @param value value to set.
   * @pre bean != null
   */
  public void setProperty(Object bean, int index, Object value) {
    assert ((bean != null) && isMultiple);
    try {
      PropertyUtils.setProperty(bean, property, value);
    } catch (InvocationTargetException ite) {
      ite.printStackTrace();
    } catch (NoSuchMethodException nsmi) {
      nsmi.printStackTrace();
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
    }
  }

  public void addProperty(Object bean, Object value) {
    assert ((bean != null) && isMultiple && (value != null));

    Class[] types = { value.getClass() };
    Object[] params = { value };
    ReflectUtilities.execute(bean, addMethodName, types, params);
  }

  public void removeProperty(Object bean, Object value) {
    assert ((bean != null) && (value != null));

    Class[] types = { value.getClass() };
    Object[] params = { value };
    ReflectUtilities.execute(bean, removeMethodName, types, params);
  }

  public Collection getValues(Object bean) {
    assert ((bean != null) && isMultiple);
    return (Collection) ReflectUtilities.execute(bean, getAllMethodName, null,
        null);
  }
}