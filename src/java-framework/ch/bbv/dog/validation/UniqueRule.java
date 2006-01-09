/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import ch.bbv.utilities.ReflectUtilities;

/**
 * The rule insures that a property value of a set of objects is unique.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class UniqueRule extends ValidationRuleImpl implements ValidationRule {
  /**
   * Key for validation result message.
   */
  private static final String IS_NOT_UNIQUE = "ch.bbv.dog.is-not-unique";
 

  /**
   * The resource bundle containing the messages for the validation messages.
   */
  private static ResourceBundle resources;

  static {
    resources = ResourceBundle.getBundle("ch.bbv.dog.validation.RulesMessages");

  }

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
   * Constructor of the class where null values are considered.
   * @param indexedProperty property which should be unique for all items in the
   *          indexed property to evaluate.
   */
  public UniqueRule(String indexedProperty) {
    this(indexedProperty, false, ERROR, false);
  }

  /**
   * Constructor of the class.
   * @param immediate flag indicating if the rule is immediate or not.
   * @param severity the severity level of the rule.
   * @param indexedProperty property which should be unique for all items in the
   *          indexed property to evaluate.
   * @param considerNull flag indicating if null values are relevant for the
   *          uniqueness test.
   */
  public UniqueRule(String indexedProperty, boolean immediate, int severity,
      boolean considerNull) {
    super(immediate, severity);
    this.indexedProperty = indexedProperty;
    this.considerNull = considerNull;
  }

  public boolean execute(Object bean, String property,
      List<ValidationRuleResult> results) {
    Collection objects = (Collection) ReflectUtilities.getProperty(bean,
        property);
    return validate(objects, results, bean);
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List<ValidationRuleResult> results) {
    return validate((Collection) newValue, results, bean);
  }

  private boolean validate(Collection objects,
      List<ValidationRuleResult> results, Object bean) {
    Set<Object> values = new HashSet<Object>();
    for (Object object : objects) {
      Object value = ReflectUtilities.getProperty(object, indexedProperty);
      if ((value == null) && !considerNull) {
        continue;
      }
      if (values.contains(value)) {
        addMessage(results, resources, IS_NOT_UNIQUE, getSeverity(), bean, value.toString(),
            null, null);
        return false;
      }
      values.add(value);
    }
    return true;
  }
}