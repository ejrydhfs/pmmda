/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog.reference;

import java.util.ArrayList;
import java.util.List;

/**
 * The boolean reference code implements the boolean values as business code.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class BooleanCode extends ReferenceCodeImpl {
  /**
   * Code object for the code value false.
   */
  public static final BooleanCode FALSE;

  /**
   * Code object for the code value true.
   */
  public static final BooleanCode TRUE;

  /**
   * Code for the code value false.
   */
  public static final int FALSE_CODE = 0;

  /**
   * Code for the code value true.
   */
  public static final int TRUE_CODE = 1;

  /**
   * List of all reference codes of this type.
   */
  private static List codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new ArrayList();
    TRUE = new BooleanCode(TRUE_CODE, 0, "true", "true");
    FALSE = new BooleanCode(FALSE_CODE, 1, "false", "false");
    codes.add(TRUE);
    codes.add(FALSE);
  }

  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   */
  public BooleanCode(int code, int sortOrder, String shortDescription,
      String description) {
    super(code, sortOrder, shortDescription, description, true);
  }

  /**
   * Returns the list of all reference codes.
   * @return list of all reference codes.
   */
  public static List getCodes() {
    return codes;
  }

  /**
   * Returns the boolean code associated with the given value.
   * @param value boolean value to convert.
   * @return the requested boolean code.
   */
  public static BooleanCode getCode(boolean value) {
    BooleanCode bool = null;
    if (value) {
      bool = (BooleanCode) codes.get(0);
    } else {
      bool = (BooleanCode) codes.get(1);
    }
    return bool;
  }

  /**
   * Returns the string representation of a boolean code as defined in Java.
   * @return the string representation of the boolean code.
   */
  public String toString() {
    return getShortDescription();
  }
}