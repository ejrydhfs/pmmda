/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.validation;

import java.util.List;

/**
 * The validation rule defines the interface for all validation formulas. An
 * immediate rule can always also be used in the deferred mode.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface ValidationRule {
  /** Flag indicating if the validation rule should be applied upon change of
   * the property or can be applied later.
   * @return true if the rule is immediate otherwise false.
   */
  boolean isImmediate();

  /** Executes the validation rule on a Java bean.
   * @param bean bean on which to process the rule.
   * @param property property to validate.
   * @param results list where rule results can be added.
   * @return true if the rule did not found any errors otherwise false.
   */
  boolean execute(Object bean, String property, List results);

  /** Executes the validation rule on a Java bean in an immediate mode.
   * @param bean bean on which to process the rule.
   * @param property name of the modified property. 
   * @param oldValue old value of the property if defined.
   * @param newValue new value of the property.
   * @param results list where rule results can be added.
   * @return true if the rule did not found any errors otherwise false.
   */
  boolean execute(Object bean, String property, Object oldValue,
    Object newValue, List results);
}
