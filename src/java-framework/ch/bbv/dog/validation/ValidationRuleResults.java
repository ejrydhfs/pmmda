/* 
 * copyright 2000-2006 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.validation;

import java.util.ArrayList;

/**
 * Wrapper class for validation rules results. Compared to a list it has provides
 * helper members.
 * @author AdrianBachofen
 *
 */
public class ValidationRuleResults extends ArrayList<ValidationRuleResult> {

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20060202;
  
  /**
   * Returns the number of result entries of passed severity
   * @param severity of results to be counted (e.g. ValidationRule.WARNING)
   * @return count of results of given severity.
   */
  public int getCount(int severity) {
    int count = 0;
    for (ValidationRuleResult result : this) {
      if(result.getSeverity() == severity) {
        count++;
      }
    }
    return count;
  }
  
}
