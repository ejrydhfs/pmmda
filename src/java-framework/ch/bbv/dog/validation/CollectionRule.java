/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ch.bbv.utilities.ReflectUtilities;

/**
 * The collection rule applies the given rules to a property of all objects part
 * of a collection.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CollectionRule extends ValidationRuleImpl implements
    ValidationRule {
  /**
   * The property on which the validation rule should be applied.
   */
  private String indexedProperty;

  /**
   * Validation rule to apply to the defined property of all items in the
   * collection.
   */
  private ValidationRule rule;

  /**
   * Constructor of the class.
   * @param immediate flag indicating if the rule is immediate or not.
   * @param severity the severity level of the rule.
   * @param indexedProperty property which should be unique for all items in the
   *        indexed property to evaluate.
   * @param rule rule to apply.
   */
  public CollectionRule(boolean immediate, int severity,
      String indexedProperty, ValidationRule rule) {
    super(immediate, severity);
    this.indexedProperty = indexedProperty;
    this.rule = rule;
  }

  public boolean execute(Object bean, String property,
      List<ValidationRuleResult> results) {
    boolean sucess = true;
    Collection objects = (Collection) ReflectUtilities.getProperty(bean,
        property);
    for (Iterator i = objects.iterator(); i.hasNext();) {
      sucess |= rule.execute(i.next(), indexedProperty, results);
    }
    return sucess;
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List<ValidationRuleResult> results) {
    return execute(bean, property, results);
  }
}