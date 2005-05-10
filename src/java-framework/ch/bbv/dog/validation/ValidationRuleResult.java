/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.validation;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * The validation rule result is the result of the execution of a validation
 * rule. Information and warnings are available to the user but does not hinder
 * the application. Errors should be corrected before the user can execute other
 * operations. The localization of the message is available through the key
 * property and the text conversion convenience methods.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ValidationRuleResult implements Serializable {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Severity of the validation rule result.
   */
  private int severity;

  /**
   * Identifier of the text of the validation rule result.
   */
  private String key;

  /**
   * Text of the validation message.
   */
  private String text;

  /**
   * The object source of the validation message.
   */
  private transient Object object;

  /**
   * Default constructor of the class.
   */
  public ValidationRuleResult() {
    // default constructor
  }

  /**
   * Constructor of the class.
   * @param severity severity of the validation result.
   * @param origin orgin of the validation result.
   * @param key key to retrieve the validation result message.
   * @param object object origin of the validation rule.
   */
  public ValidationRuleResult(int severity, String origin, String key,
      Object object) {
    this.severity = severity;
    this.key = key;
    this.object = object;

  }

  /**
   * Returns the message key.
   * @return key of the validation rule result message.
   * @see #setKey(String)
   */
  public String getKey() {
    return key;
  }

  /**
   * Sets the message key.
   * @param key new key of the validation rule result message.
   * @see #getKey()
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * Returns the severity of the validation rule result message.
   * @return the severity of the result.
   * @see #setSeverity(int)
   */
  public int getSeverity() {
    return severity;
  }

  /**
   * Sets the severity of the validation rule result message.
   * @param severity new severity of the result.
   * @see #getSeverity()
   */
  public void setSeverity(int severity) {
    this.severity = severity;
  }

  /**
   * Returns the text for the severity level.
   * @return severity level as text.
   */
  public String getSeverityText() {
    String text;
    switch (severity) {
    case ValidationRule.INFO:
      text = "INFO";
      break;
    case ValidationRule.WARNING:
      text = "WARN";
      break;
    case ValidationRule.ERROR:
      text = "ERROR";
      break;
    default:
      text = null;
    }
    return text;
  }

  /**
   * Returns the text of the validation rule result message.
   * @return the text of the result.
   * @see #setText(String)
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the text of the validation rule result message.
   * @param text new text of the validation rule result message.
   * @see #getText()
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Sets the text of the validation rule result message.
   * @param text new text of the validation rule result message.
   * @param parameters the parameters of the message text.
   * @see #getText()
   */
  public void setText(String text, Object[] parameters) {
    this.text = MessageFormat.format(text, parameters);
  }

  /**
   * Returns the object source of the validation rule.
   * @return source of the validation rule result.
   * @see #setObject(Object)
   */
  public Object getObject() {
    return object;
  }

  /**
   * Sets the object containing a validation rule result.
   * @param object source object of the rule result.
   * @see #getObject
   */
  public void setObject(Object object) {
    this.object = object;
  }
}