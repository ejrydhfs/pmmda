/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.*;

import ch.bbv.utilities.ReflectUtilities;

/**
 * The collection rule applies the given rules to a property of all objects part
 * of a collection.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CollectionRule implements ValidationRule {
  /**
   * Indicates if the rule should be applied immediately or only
   */
  private boolean immediate;

  /**
   * The property which value should be unique. Uniqueness is verified with the
   * equals operator.
   */
  private String indexedProperty;

  /**
   * Validation rule to apply to the property of all items in the collection.
   */
  private ValidationRule rule;

  /**
   * Constructor of the class.
   * @param indexedProperty property which should be unique for all items in the
   *        indexed property to evaluate.
   * @param rule rule to apply.
   */
  public CollectionRule(String indexedProperty, ValidationRule rule) {
    this.indexedProperty = indexedProperty;
    this.rule = rule;
  }

  public boolean execute(Object bean, String property, List results) {
    boolean sucess = true;
    Collection objects = (Collection) ReflectUtilities.getProperty(bean,
        property);
    for (Iterator i = objects.iterator(); i.hasNext();) {
      sucess |= rule.execute(i.next(), indexedProperty, results);
    }
    return sucess;
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List results) {
    return execute(bean, property, results);
  }

  public boolean isImmediate() {
    return immediate;
  }
}