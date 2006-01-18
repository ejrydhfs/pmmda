/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.bbv.utilities.ReflectUtilities;

/**
 * The format rule defines the input format of allowed inputs. The format is
 * defined as a regular expression.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class FormatRule extends ValidationRuleImpl implements ValidationRule {
  /**
   * Key for validation result message.
   */
  private static final String NO_FORMAT_COMPLIANCE = "ch.bbv.dog.no-format-compliance-error";

  /**
   * The resource bundle containing the messages for the validation messages.
   */
  private ResourceBundle resources;

  /**
   * Holds the key for the key for the message from the ResourceBundle
   */
  private String resourceKey;

  /**
   * The pattern to validate.
   */
  private Pattern pattern;

  /**
   * The matcher of the pattern;
   */
  private Matcher matcher;

  /**
   * Constructor of the class.
   * @param immediate indicates if the rule should be checked immediately
   * @param severity the severity level of the rule.
   * @param format format to validate.
   */
  public FormatRule(boolean immediate, int severity, String format) {
    this(immediate, severity, format, ResourceBundle
        .getBundle("ch.bbv.dog.validation.RulesMessages"), NO_FORMAT_COMPLIANCE);
  }

  public FormatRule(boolean immediate, int severity, String format,
      ResourceBundle resourceBundle, String resourceKey) {
    super(immediate, severity);
    pattern = Pattern.compile(format);
    matcher = pattern.matcher("");
    resources = resourceBundle;
    this.resourceKey = resourceKey;
  }

  @Override
  public boolean execute(Object bean, String property,
      List<ValidationRuleResult> results) {
    Object value = ReflectUtilities.getProperty(bean, property);
    if (!validate(value, results)) {
      addMessage(results, resources, resourceKey, getSeverity(), bean,
          property, null, null);
      return false;
    }
    return true;
  }

  @Override
  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List<ValidationRuleResult> results) {
    if (!validate(newValue, results)) {
      addMessage(results, resources, resourceKey, getSeverity(), bean,
          property, oldValue, newValue);
      return false;
    }
    return true;
  }

  /**
   * Validates the rule agains the data.
   * @param value data to validate.
   * @param results list of validation rule results.
   * @return true if sucessful otherwise false.
   */
  private boolean validate(Object value, List results) {
    if (value != null) {
      matcher.reset((String) value);
      if (!matcher.matches()) {
        return false;
      }
    }
    return true;
  }
}