/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.validation;

import java.io.Serializable;

/**
 * The validation rule result is the result of the execution of a validation
 * rule. Information and warnings are available to the user but does not hinder
 * the application. Errors should be corrected before the user can execute other
 * operations.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ValidationRuleResult implements Serializable {
  /**
   * The validation rule result is an information message.
   */
  public static final int INFO = 0;

  /**
   * The validation rule result is a warning message.
   */
  public static final int WARNING = 1;

  /**
   * The validation rule result is an error message.
   */
  public static final int ERROR = 2;

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
    this.text = text;
  }
}