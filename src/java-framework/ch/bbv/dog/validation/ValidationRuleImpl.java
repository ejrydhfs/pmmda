/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationMessage;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.SimpleValidationMessage;

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
  private Severity severity;

  /**
   * Returns the immediate flag.
   * @return the immediate flag
   */
  public boolean isImmediate() {
    return immediate;
  }

  /**
   * Returns the severity level.
   * @return the severity level
   */
  public Severity getSeverity() {
    return severity;
  }

  public boolean execute(Object bean, ValidationResult results) {
    return true;
  }

  public boolean execute(Object bean, String property, ValidationResult results) {
    return true;
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, ValidationResult results) {
    return true;
  }

  /**
   * Constructor of the class.
   * @param immediate flag indicating if the rule is immediate or not
   * @param severity the severity level of the rule
   */
  protected ValidationRuleImpl(boolean immediate, Severity severity) {
    this.immediate = immediate;
    this.severity = severity;
  }

  /**
   * Adds an error message to the list of results.
   * @param results results containing the messages
   * @param resources resource bundle of messages
   * @param key key of the message
   * @param severity severity of the message
   * @param bean bean origin of the message
   * @param property property origin of the message
   * @param oldValue old value of the property
   * @param newValue new value of the property
   * @pre (results != null) && (resources != null) && (key != null)
   */
  protected void addMessage(ValidationResult results, ResourceBundle resources,
      String key, Severity severity, Object bean, String property,
      Object oldValue, Object newValue) {
    Object[] parameters = { bean.toString(), property, oldValue, newValue };
    String text;
    try {
      text = MessageFormat.format(resources.getString(key), parameters);
    } catch (java.util.MissingResourceException e) {
      text = MessageFormat.format(
          "Sorry, no message yet defined. Message key: " + key
              + "; parameters {0}, {1}, {2}, {3}", parameters);
    }
    ValidationMessage message = new SimpleValidationMessage(text, severity,
        bean);
    results.add(message);
  }

  public void validationStart(ValidationResult results) {
  }

  public void validationEnd(ValidationResult results) {
  }
}