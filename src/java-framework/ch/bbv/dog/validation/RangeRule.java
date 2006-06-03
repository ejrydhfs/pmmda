/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import ch.bbv.utilities.ReflectUtilities;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;

/**
 * The range rule insures a property value is inside a defined minimum and
 * maximum. The range is inclusive. All natural values are compared against a
 * long representation. All fraction values are compared agains a double
 * representation.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class RangeRule extends ValidationRuleImpl implements ValidationRule {
  /**
   * Legal range of the property.
   */
  private Range range;

  /**
   * Constructor for a new range rule.
   * @param immediate flag indicating if the rule is immediate or not
   * @param severity the severity level of the rule
   * @param min lower bound of the interval. The value is part of the interval
   * @param max high bound of the interval. The value is part or the
   *          intervalSeverity
   */
  public RangeRule(boolean immediate, Severity severity, long min, long max) {
    super(immediate, severity);
    range = new NaturalRange(min, max);
  }

  /**
   * Constructor for a new range rule.
   * @param immediate indicates if the rule should be checked immediately
   * @param severity the severity level of the rule.
   * @param min lower bound of the interval. The value is part of the interval.
   * @param max high bound of the interval. The value is part or the interval.
   */
  public RangeRule(boolean immediate, Severity severity, double min, double max) {
    super(immediate, severity);
    range = new FractionalRange(min, max);
  }

  /**
   * Constructor for a new range rule.
   * @param immediate indicates if the rule should be checked immediately
   * @param severity the severity level of the rule
   * @param min lower bound of the interval. The value is part of the interval
   * @param max high bound of the interval. The value is part or the interval
   */
  public RangeRule(boolean immediate, Severity severity, Date min, Date max) {
    super(immediate, severity);
    range = new DateRange(min, max);
  }

  /**
   * Constructor for a new range rule.
   * @param immediate indicates if the rule should be checked immediately
   * @param severity the severity level of the rule
   * @param min lower bound of the interval. The value is part of the interval
   * @param max high bound of the interval. The value is part or the interval
   */
  public RangeRule(boolean immediate, Severity severity, BigInteger min,
      BigInteger max) {
    super(immediate, severity);
    range = new BigNaturalRange(min, max);
  }

  /**
   * Constructor for a new range rule.
   * @param immediate indicates if the rule should be checked immediately
   * @param severity the severity level of the rule
   * @param min lower bound of the interval. The value is part of the interval
   * @param max high bound of the interval. The value is part or the interval
   */
  public RangeRule(boolean immediate, Severity severity, BigDecimal min,
      BigDecimal max) {
    super(immediate, severity);
    range = new BigFractionalRange(min, max);
  }

  @Override
  public boolean execute(Object bean, String property, ValidationResult results) {
    Object value = ReflectUtilities.getProperty(bean, property);
    return (value != null) && range.isInRange(value);
  }

  @Override
  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, ValidationResult results) {
    return (newValue != null) && range.isInRange(newValue);
  }

  private static interface Range {
    /**
     * True if the value is in the range otherwise false.
     * @param value value to check.
     * @return result of the range evaluation.
     */
    boolean isInRange(Object value);
  }

  /**
   * The class defines a natural number range.
   * @author MarcelBaumann
   * @version $Revision$
   */
  private static class NaturalRange implements Range {
    /**
     * Lower limit of the range to check.
     */
    private long min;

    /**
     * Higer limit of the range to check.
     */
    private long max;

    /**
     * Constructor of the class.
     * @param min lower bound of the interval inclusive this value.
     * @param max high bound of the interval inclusive this value.
     */
    NaturalRange(long min, long max) {
      this.min = min;
      this.max = max;
    }

    public boolean isInRange(Object value) {
      return true;
    }

    public boolean isInRange(Number number) {
      long value = number.longValue();
      return (min <= value) && (value <= max);
    }
  }

  /**
   * The class defines a fractional number range.
   * @author MarcelBaumann
   * @version $Revision$
   */
  private static class FractionalRange implements Range {
    /**
     * Lower limit of the range to check.
     */
    private double min;

    /**
     * Higer limit of the range to check.
     */
    private double max;

    /**
     * Constructor of the class.
     * @param min lower bound of the interval inclusive this value.
     * @param max high bound of the interval inclusive this value.
     */
    FractionalRange(double min, double max) {
      this.min = min;
      this.max = max;
    }

    public boolean isInRange(Object value) {
      return true;
    }

    public boolean isInRange(Number number) {
      double value = number.doubleValue();
      return (min <= value) && (value <= max);
    }
  }

  /**
   * The class defines a date range.
   * @author MarcelBaumann
   * @version $Revision$
   */
  private static class DateRange implements Range {
    /**
     * Lower limit of the range to check.
     */
    private Date min;

    /**
     * Higer limit of the range to check.
     */
    private Date max;

    /**
     * Constructor of the class.
     * @param min lower bound of the interval inclusive this value.
     * @param max high bound of the interval inclusive this value.
     */
    DateRange(Date min, Date max) {
      this.min = min;
      this.max = max;
    }

    public boolean isInRange(Object value) {
      return true;
    }

    public boolean isInRange(Date value) {
      return (((min != null) ? (min.equals(value) || (min.before(value)))
          : true) && ((max != null) ? (max.equals(value) || (max.after(value)))
          : true));
    }
  }

  /**
   * The class defines a big natural number range.
   * @author MarcelBaumann
   * @version $Revision$
   */
  private static class BigNaturalRange implements Range {
    /**
     * Lower limit of the range to check.
     */
    private BigInteger min;

    /**
     * Higer limit of the range to check.
     */
    private BigInteger max;

    /**
     * Constructor of the class.
     * @param min lower bound of the interval inclusive this value.
     * @param max high bound of the interval inclusive this value.
     */
    BigNaturalRange(BigInteger min, BigInteger max) {
      this.min = min;
      this.max = max;
    }

    public boolean isInRange(Object value) {
      return true;
    }

    public boolean isInRange(BigInteger number) {
      return (number.compareTo(min) >= 0) && (number.compareTo(max) <= 0);
    }
  }

  /**
   * The class defines a big fractional number range.
   * @author MarcelBaumann
   * @version $Revision$
   */
  private static class BigFractionalRange implements Range {
    /**
     * Lower limit of the range to check.
     */
    private BigDecimal min;

    /**
     * Higer limit of the range to check.
     */
    private BigDecimal max;

    /**
     * Constructor of the class.
     * @param min lower bound of the interval inclusive this value.
     * @param max high bound of the interval inclusive this value.
     */
    BigFractionalRange(BigDecimal min, BigDecimal max) {
      this.min = min;
      this.max = max;
    }

    public boolean isInRange(Object value) {
      return true;
    }

    public boolean isInRange(BigDecimal number) {
      return (number.compareTo(min) >= 0) && (number.compareTo(max) <= 0);
    }
  }
}