/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.bbv.utilities.ReflectUtilities;

/**
 * The format rule defines the input format of allowed inputs. The format is
 * defined as a regular expression.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class FormatRule implements ValidationRule {
  /**
   * Cache of format rules available in the application. The same rule object
   * can be used to verify a defined format.
   */
  private static Map rules = new HashMap();

  /**
   * Indicates if the rule should be applied immediately or only
   */
  private boolean immediate;

  /**
   * The pattern to validate.
   */
  private Pattern pattern;

  /**
   * The matcher of the pattern;
   */
  private Matcher matcher;

  /**
   * Create the requested rule. The method returns always the same object for
   * the associated configuration.
   * @param immediate flag indicating if the rule is immediate or not.
   * @param format format to validate.
   * @return the requested format rule.
   */
  public static FormatRule create(boolean immediate, String format) {
    FormatRule rule = (FormatRule) rules.get(format);
    if (rule == null) {
      rule = new FormatRule(immediate, format);
      rules.put(format, rule);
    }
    return rule;
  }

  /**
   * Constructor of the class.
   * @param immediate indicates if the rule should be checked immediately
   * @param format format to validate.
   */
  public FormatRule(boolean immediate, String format) {
    this.immediate = immediate;
    pattern = Pattern.compile(format);
    matcher = pattern.matcher(null);
  }

  public boolean execute(Object bean, String property, List results) {
    Object value = ReflectUtilities.getProperty(bean, property);
    return validate(value, results);
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List results) {
    return validate(newValue, results);
  }

  public boolean isImmediate() {
    return immediate;
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