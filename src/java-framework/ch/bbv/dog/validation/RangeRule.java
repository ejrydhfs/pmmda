/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.validation;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Date;
import java.util.List;

/**
 * The range rule insures a property value is inside a defined minimum and
 * maximum. The range is inclusive. All natural values are compared against
 * a long representation. All fraction values are compared agains a double
 * representation.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class RangeRule implements ValidationRule {
  private boolean isImmediate;
  private Range range;

  public RangeRule(boolean isImmediate, long min, long max) {
    this.isImmediate = isImmediate;
    range = new NaturalRange(min, max);
  }

  public RangeRule(boolean isImmediate, double min, double max) {
    this.isImmediate = isImmediate;
    range = new FractionalRange(min, max);
  }

  public RangeRule(boolean isImmediate, Date min, Date max) {
    this.isImmediate = isImmediate;
    range = new DateRange(min, max);
  }

  public RangeRule(boolean isImmediate, BigInteger min, BigInteger max) {
    this.isImmediate = isImmediate;
    range = new BigNaturalRange(min, max);
  }

  public RangeRule(boolean isImmediate, BigDecimal min, BigDecimal max) {
    this.isImmediate = isImmediate;
    range = new BigFractionalRange(min, max);
  }

  public boolean execute(Object bean, String property, List results) {
    Object value = null;
    return (value != null) && range.isInRange(value);
  }

  public boolean execute(Object bean, String property, Object oldValue,
    Object newValue, List results) {
    return false;
  }

  public boolean isImmediate() {
    return isImmediate;
  }

  private static interface Range {
    public boolean isInRange(Object value);
  }

  private static class NaturalRange implements Range {
    /** Lower limit of the range to check.
     */
    private long min;

    /** Higer limit of the range to check.
     */
    private long max;

    public NaturalRange(long min, long max) {
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

  private static class FractionalRange implements Range {
    /** Lower limit of the range to check.
     */
    private double min;

    /** Higer limit of the range to check.
     */
    private double max;

    public FractionalRange(double min, double max) {
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

  private static class DateRange implements Range {
    /** Lower limit of the range to check.
     */
    private Date min;

    /** Higer limit of the range to check.
     */
    private Date max;

    public DateRange(Date min, Date max) {
      this.min = min;
      this.max = max;
    }

    public boolean isInRange(Object value) {
      return true;
    }

    public boolean isInRange(Date value) {
      return (((min != null) ? (min.equals(value) || (min.before(value))) : true) &&
      ((max != null) ? (max.equals(value) || (max.after(value))) : true));
    }
  }

  private static class BigNaturalRange implements Range {
    /** Lower limit of the range to check.
     */
    private BigInteger min;

    /** Higer limit of the range to check.
     */
    private BigInteger max;

    public BigNaturalRange(BigInteger min, BigInteger max) {
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

  private static class BigFractionalRange implements Range {
    /** Lower limit of the range to check.
     */
    private BigDecimal min;

    /** Higer limit of the range to check.
     */
    private BigDecimal max;

    public BigFractionalRange(BigDecimal min, BigDecimal max) {
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
