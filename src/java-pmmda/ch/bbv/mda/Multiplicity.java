/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;


/**
 * The multiplicity class defines the multiplicity of a property or an
 * indexed property.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class Multiplicity {
	/** Constant for a standard multiplicity value.
	 */
  public static final Multiplicity MULTIPLICITY_0_1;

	/** Constant for a standard multiplicity value.
	 */
  public static final Multiplicity MULTIPLICITY_0_N;

	/** Constant for a standard multiplicity value.
	 */
  public static final Multiplicity MULTIPLICITY_1_1;

	/** Constant for a standard multiplicity value.
	 */
  public static final Multiplicity MULTIPLICITY_1_N;

	/** Constant for a standard multiplicity value.
	 */
  public static final Multiplicity MULTIPLICITY_N_M;

	/** Constant for a standard multiplicity bound value.
	 */
  public static final int N_MULTIPLICITY = -1;

	/** UML representation of a standard multiplicity bound value.
	 */
  private static final String MULTIPLICITY_N_STRING = "N";

	/** UML representation of a standard multiplicity bound value.
	 */
  private static final String MULTIPLICITY_0_STRING = "0";

	/** UML representation of a standard multiplicity bound value.
	 */
  private static final String MULTIPLICITY_1_STRING = "1";

  static {
    MULTIPLICITY_0_1 = new Multiplicity(0, 1);
    MULTIPLICITY_0_N = new Multiplicity(0, N_MULTIPLICITY);
    MULTIPLICITY_1_1 = new Multiplicity(1, 1);
    MULTIPLICITY_1_N = new Multiplicity(1, N_MULTIPLICITY);
    MULTIPLICITY_N_M = new Multiplicity(N_MULTIPLICITY, N_MULTIPLICITY);
  }

	/** Lower bound of the multiplicity.
	 */
  private int lowerBound;

	/** Upper bound of the multiplicity.
	 */
  private int upperBound;

	/** Constructor of the class.
	 * @param lowerBound lower bound value.
	 * @param upperBound upper bound value.
	 */
  public Multiplicity(int lowerBound, int upperBound) {
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  /** 
   * Returns the lower bound of the multiplicity.
   * @return the lower bound of the multiplicity.
   * @see #setLowerBound(int)
   */
  public int getLowerBound() {
    return lowerBound;
  }

  /**
   * Sets the lower bound of the multiplicity.
   * @param lowerBound new lower bound of the multiplicity.
   * @see #getLowerBound() 
   */
  public void setLowerBound(int lowerBound) {
    this.lowerBound = lowerBound;
  }

  /** 
   * Returns the upper bound of the multiplicity.
   * @return the upper bound of the multiplicity.
   * @see #setUpperBound(int)
   */
  public int getUpperBound() {
    return upperBound;
  }

  /**
   * Sets the upper bound of the multiplicity.
   * @param upperBound new upper bound of the multiplicity.
   * @see #getUpperBound() 
   */
  public void setUpperBound(int upperBound) {
    this.upperBound = upperBound;
  }

  /**
   * Computes the string value of the bound.
   * @param bound bound to convert.
   * @return the string value of the bound.
   * @see #intValue(String)
   */
  public static final String stringValue(int bound) {
    String value;
    switch (bound) {
    case N_MULTIPLICITY:
      value = MULTIPLICITY_N_STRING;
      break;
    case 0:
      value = MULTIPLICITY_0_STRING;
    case 1:
      value = MULTIPLICITY_1_STRING;
    default:
      value = Integer.toString(bound);
    }
    return value;
  }
  
  /** 
   * Computes the integer value of the string representation of a bound.
   * @param bound bound to convert.
   * @return the integer value of the bound.
   * @see #stringValue(int)
   */
  public static final int intValue(String bound) {
  	int value = 0;
  	if (MULTIPLICITY_0_STRING.equals(bound)) {
  		value = N_MULTIPLICITY;
  	} else {
  		value = Integer.parseInt(bound);
  	}
  	return value;
  }
}
