/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.ResourceBundle;

import ch.bbv.utilities.ReflectUtilities;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;

/**
 * The rule verifies that the property is not null.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class NotNullRule extends ValidationRuleImpl implements ValidationRule {
  /**
   * Key for validation result message.
   */
  private static final String IS_NULL = "ch.bbv.dog.is-null-error";

  /**
   * The resource bundle containing the messages for the validation messages.
   */
  private static ResourceBundle resources;

  static {
    resources = ResourceBundle.getBundle("ch.bbv.mda.validation.RulesMessages");

  }

  /**
   * Constructor of the class.
   * @param immediate true if the rule is immediate otherwise false
   * @param severity the severity level of the rule
   */
  public NotNullRule(boolean immediate, Severity severity) {
    super(immediate, severity);
  }

  @Override
  public boolean execute(Object bean, String property, ValidationResult results) {
    Object value = ReflectUtilities.getProperty(bean, property);
    if (value == null) {
      addMessage(results, resources, IS_NULL, getSeverity(), bean, property,
          null, null);
      return false;
    }
    return true;
  }

  @Override
  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, ValidationResult results) {
    Object value = ReflectUtilities.getProperty(bean, property);
    if (value == null) {
      addMessage(results, resources, IS_NULL, getSeverity(), bean, property,
          oldValue, newValue);
      return false;
    }
    return true;
  }
}