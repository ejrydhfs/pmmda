/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.derived;

import java.util.List;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ValidationRule implements Rule {
  private String name;
  private String description;

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean evaluate(Object bean, String property, List messages) {
    PropertyIterator iterator = new PropertyIterator(bean, property);
    return false;
  }
  
  protected boolean apply(Object bean, Object property, List messages) {
  	return true;
  }

  public static class RuleIsNull {
  }

  public static class RuleIsNotNull {
  }

  public static class RuleIsInNaturalRange {
    private Integer low;
    private Integer high;

    public RuleIsInNaturalRange(Integer low, Integer high) {
      this.low = low;
      this.high = high;
    }
  }

  public static class RuleIsInFloatRange {
    private Double low;
    private Double high;

    public RuleIsInFloatRange(Double low, Double high) {
      this.low = low;
      this.high = high;
    }
  }

  public static class RuleHasStringRange {
    private Integer low;
    private Integer high;

    public RuleHasStringRange(Integer low, Integer high) {
      this.low = low;
      this.high = high;
    }
  }

  public static class RuleMatchRegExp {
    private String regExp;

    public RuleMatchRegExp(String regExp) {
      this.regExp = regExp;
    }
  }
}
