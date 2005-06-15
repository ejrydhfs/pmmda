/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.utilities;

import java.util.HashSet;
import java.util.Set;

/**
 * The type utilities class provides meta type information about build-in types.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class TypeUtilities {
  /**
   * Default value for the primitive type.
   */
  public static final Byte BYTE_0 = new Byte((byte) 0);

  /**
   * Default value for the primitive type.
   */
  public static final Short SHORT_0 = new Short((short) 0);

  /**
   * Default value for the primitive type.
   */
  public static final Integer INTEGER_0 = new Integer(0);

  /**
   * Default value for the primitive type.
   */
  public static final Long LONG_0 = new Long(0);

  /**
   * Default value for the primitive type.
   */
  public static final Float FLOAT_0 = new Float(0);

  /**
   * Default value for the primitive type.
   */
  public static final Double DOUBLE_0 = new Double(0);

  /**
   * The set of classes representing natural objects.
   */
  private static Set<Class> objectNaturals;

  /**
   * The set of classes representing primitive naturals.
   */
  private static Set<Class> primitiveNaturals;

  /**
   * The set of classes representing real objects.
   */
  private static Set<Class> objectReals;

  /**
   * The set of classes representing primitive reals.
   */
  private static Set<Class> primitiveReals;

  static {
    objectNaturals = new HashSet<Class>();
    objectNaturals.add(Byte.class);
    objectNaturals.add(Short.class);
    objectNaturals.add(Integer.class);
    objectNaturals.add(Long.class);
    primitiveNaturals = new HashSet<Class>();
    primitiveNaturals.add(Byte.TYPE);
    primitiveNaturals.add(Short.TYPE);
    primitiveNaturals.add(Integer.TYPE);
    primitiveNaturals.add(Long.TYPE);
    objectReals = new HashSet<Class>();
    objectReals.add(Float.class);
    objectReals.add(Double.class);
    primitiveReals = new HashSet<Class>();
    primitiveReals.add(Float.TYPE);
    primitiveReals.add(Double.TYPE);
  }

  /**
   * Returns true is the class represents a natural number.
   * @param clazz class to check.
   * @return true if the class represents natural number.
   */
  public static boolean isNaturalNumber(Class clazz) {
    return primitiveNaturals.contains(clazz) || objectNaturals.contains(clazz);
  }

  /**
   * Returns true is the class represents a real number.
   * @param clazz class to check.
   * @return true if the class represents real number.
   */
  public static boolean isRealNumber(Class clazz) {
    return primitiveReals.contains(clazz) || objectNaturals.contains(clazz);
  }

  /**
   * Returns true is the class represents a primitive number.
   * @param clazz class to check.
   * @return true if the class represents primitive number.
   */
  public static boolean isPrimitiveNumber(Class clazz) {
    return primitiveNaturals.contains(clazz) || primitiveReals.contains(clazz);
  }

  /**
   * Returns true is the class represents an object number.
   * @param clazz class to check.
   * @return true if the class represents object number.
   */
  public static boolean isObjectNumber(Class clazz) {
    return objectNaturals.contains(clazz) || objectReals.contains(clazz);
  }

  /**
   * Returns the zero value of the primitive number class.
   * @param clazz class representing primitive number class.
   * @return the zero value instance of the class.
   */
  public static Number getPrimitiveZero(Class clazz) {
    Number zero = null;
    if (isPrimitiveNumber(clazz)) {
      if (Byte.TYPE == clazz) {
        zero = BYTE_0;
      } else if (Short.TYPE == clazz) {
        zero = SHORT_0;
      } else if (Integer.TYPE == clazz) {
        zero = INTEGER_0;
      } else if (Long.TYPE == clazz) {
        zero = LONG_0;
      }
    }
    return zero;
  }
}
