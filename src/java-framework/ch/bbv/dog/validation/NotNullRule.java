/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.List;

import ch.bbv.utilities.ReflectUtilities;

/**
 * The rule verifies that the property is not null.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class NotNullRule implements ValidationRule {
  /**
   * The instance of the immediate rule can be shared for all properties.
   */
  private static NotNullRule immediateRule;

  /**
   * The instance of the not immediate rule can be shared for all properties.
   */
  private static NotNullRule notImmediateRule;

  /**
   * Create the requested rule. The method returns always the same object for
   * the associated configuration.
   * @param immediate flag indicating if the rule is immediate or not.
   * @return the requested validation rule.
   */
  public NotNullRule create(boolean immediate) {
    NotNullRule rule;
    if (immediate) {
      if (immediateRule == null) {
        immediateRule = new NotNullRule(immediate);
      }
      rule = immediateRule;
    } else {
      if (notImmediateRule == null) {
        notImmediateRule = new NotNullRule(immediate);
      }
      rule = notImmediateRule;
    }
    return rule;
  }

  /**
   * Constructor of the class.
   * @param immediate true if the rule is immediate otherwise false.
   */
  public NotNullRule(boolean immediate) {
    this.immediate = immediate;
  }

  /**
   * Indicates if the rule should be applied immediately or only
   */
  private boolean immediate;

  public boolean execute(Object bean, String property, List results) {
    return ReflectUtilities.getProperty(bean, property) != null;
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List results) {
    return ReflectUtilities.getProperty(bean, property) != null;
  }

  public boolean isImmediate() {
    return immediate;
  }
}