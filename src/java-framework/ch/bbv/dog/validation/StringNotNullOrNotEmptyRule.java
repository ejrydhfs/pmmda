/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.List;
import java.util.ResourceBundle;

import ch.bbv.utilities.ReflectUtilities;

/**
 * The rule verifies that the property is a String type and not empty.
 * @author Adrian Bachofen
 * @version $Revision$
 */
public class StringNotNullOrNotEmptyRule extends ValidationRuleImpl implements ValidationRule {
  /**
   * Key for validation result message.
   */
  private static final String IS_STRING_NULL_OR_EMPTY = "ch.bbv.dog.is-string-null-or-empty-error";

  /**
   * The resource bundle containing the messages for the validation messages.
   */
  private static ResourceBundle resources;

  static {
    resources = ResourceBundle.getBundle("ch.bbv.dog.validation.RulesMessages");

  }

  /**
   * Constructor of the class.
   * @param immediate true if the rule is immediate otherwise false.
   * @param severity the severity level of the rule.
   */
  public StringNotNullOrNotEmptyRule(boolean immediate, int severity) {
    super(immediate, severity);
  }

  public boolean execute(Object bean, String property,
      List<ValidationRuleResult> results) {
    String value = (String)ReflectUtilities.getProperty(bean, property);
    if (value == null || value.equals("")) {
      addMessage(results, resources, IS_STRING_NULL_OR_EMPTY, getSeverity(), bean, property,
          null, null);
      return false;
    }
    return true;
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List<ValidationRuleResult> results) {
    Object value = ReflectUtilities.getProperty(bean, property);
    if (value == null || value.equals("")) {
      addMessage(results, resources, IS_STRING_NULL_OR_EMPTY, getSeverity(), bean, property,
          oldValue, newValue);
      return false;
    }
    return true;
  }
}