/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.util.List;

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

  public boolean isImmediate() {
    return immediate;
  }

  /**
   * Sets the immediate flag.
   * @param immediate new value of the flag. #see #isImmediate()
   */
  protected void setImmediate(boolean immediate) {
    this.immediate = immediate;
  }

  public boolean execute(Object bean, List results) {
    return true;
  }

  public boolean execute(Object bean, String property, List results) {
    return true;
  }

  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List results) {
    return true;
  }
}