/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.validation;

import java.io.Serializable;

/**
 * The validation rule result is the result of the execution of a validation
 * rule.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ValidationRuleResult implements Serializable {
  public static final int INFO = 0;
  public static final int WARNING = 1;
  public static final int ERROR = 2;
  private int severity;
  private String key;
  private String text;
  private Object[] parameters;

  public String getKey() {
    return key;
  }

  public Object[] getParameters() {
    return parameters;
  }

  public int getSeverity() {
    return severity;
  }

  public String getText() {
    return text;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setParameters(Object[] parameters) {
    this.parameters = parameters;
  }

  public void setSeverity(int severity) {
    this.severity = severity;
  }

  public void setText(String text) {
    this.text = text;
  }
}
