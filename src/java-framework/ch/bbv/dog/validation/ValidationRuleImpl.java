/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.List;
import java.util.ResourceBundle;

/**
 * The default implementation of the validation rule interface provides neutral
 * behavior for all methods.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ValidationRuleImpl implements ValidationRule {
  /**
   * Indicates if the rule should be applied immediately or only.
   */
  private boolean immediate;

  /**
   * Indicates the severity of the rule.
   */
  private int severity;

  /**
   * Returns the immediate flag.
   * @return the immediate flag.
   */
  public boolean isImmediate() {
    return immediate;
  }

  /**
   * Returns the severity level.
   * @return the severity level.
   */
  public int getSeverity() {
    return severity;
  }

  public boolean execute(Object bean, List<ValidationRuleResult> results) {
    return true;
  }

  public boolean execute(Object bean, String property,
      List<ValidationRuleResult> results) {
    return true;
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List<ValidationRuleResult> results) {
    return true;
  }

  /**
   * Constructor of the class.
   * @param immediate flag indicating if the rule is immediate or not.
   * @param severity the severity level of the rule.
   */
  protected ValidationRuleImpl(boolean immediate, int severity) {
    this.immediate = immediate;
    this.severity = severity;
  }

  /**
   * Adds an error message to the list of results.
   * @param results results containing the messages.
   * @param resources resource bundle of messages.
   * @param key key of the message.
   * @param severity severity of the message.
   * @param bean bean origin of the message.
   * @param property property origin of the message.
   * @param oldValue old value of the property.
   * @param newValue new value of the property.
   * @pre (results != null) && (resources != null) && (key != null)
   */
  protected void addMessage(List<ValidationRuleResult> results,
      ResourceBundle resources, String key, int severity, Object bean,
      String property, Object oldValue, Object newValue) {
    ValidationRuleResult result = new ValidationRuleResult();
    result.setSeverity(severity);
    result.setKey(key);
    Object[] parameters = { bean.toString(), property, oldValue, newValue };
    try {
      result.setText(resources.getString(key), parameters);
    } catch (java.util.MissingResourceException e) {
      result.setText("Sorry, no message yet defined. Message key: " + key
          + "; parameters {0}, {1}, {2}, {3}", parameters);
    }
    results.add(result);
  }
  
  public void validationStart(List<ValidationRuleResult> results) {
  }
  
  public void validationEnd(List<ValidationRuleResult> results) {
  }
}