/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.List;
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
    super(immediate, severity);
    pattern = Pattern.compile(format);
    matcher = pattern.matcher("");
  }

  public boolean execute(Object bean, String property, List results) {
    Object value = ReflectUtilities.getProperty(bean, property);
    return validate(value, results);
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List results) {
    return validate(newValue, results);
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
        // TODO add message
        return false;
      }
    }
    return true;
  }
}