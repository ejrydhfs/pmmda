/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.*;

import ch.bbv.utilities.ReflectUtilities;

/**
 * The rule insures that a property value of a set of objects is unique.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class UniqueRule implements ValidationRule {
  /**
   * Indicates if the rule should be applied immediately or only.
   */
  private boolean immediate;

  /**
   * Indicates if null values should be considered for uniqueness check. private
   */
  boolean considerNull;

  /**
   * The property which value should be unique. Uniqueness is verified with the
   * equals operator.
   */
  private String indexedProperty;

  /**
   * Constructor of the class.
   * @param indexedProperty property which should be unique for all items in the
   *        indexed property to evaluate.
   * @param considerNull flag indicating if null values are relevant for the
   *        uniqueness test.
   */
  public UniqueRule(String indexedProperty, boolean considerNull) {
    this.indexedProperty = indexedProperty;
    this.considerNull = considerNull;
  }

  public boolean execute(Object bean, String property, List results) {
    Set values = new HashSet();
    Collection objects = (Collection) ReflectUtilities.getProperty(bean,
        property);
    for (Iterator i = objects.iterator(); i.hasNext();) {
      Object value = ReflectUtilities.getProperty(i.next(), indexedProperty);
      if ((value == null) && !considerNull) {
        continue;
      }
      if (values.contains(value)) {
        return false;
      }
      values.add(value);
    }
    return true;
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List results) {
    return execute(bean, property, results);
  }

  public boolean isImmediate() {
    return immediate;
  }
}