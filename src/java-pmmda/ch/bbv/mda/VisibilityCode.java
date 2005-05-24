/* 
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.bbv.dog.reference.ReferenceCodeImpl;

/**
 * The visibility reference code defines the available visibility levels in UML
 * as business codes. These codes covers all the visibility values for common
 * languages such as Java or C++.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class VisibilityCode extends ReferenceCodeImpl {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The code for the public visibility.
   */
  public static final int PUBLIC_CODE = 1;

  /**
   * The code for the protected visibility.
   */
  public static final int PROTECTED_CODE = 2;

  /**
   * The code for the package visibility.
   */
  public static final int PACKAGE_CODE = 3;

  /**
   * The code for the private visibility.
   */
  public static final int PRIVATE_CODE = 4;

  /**
   * List of all reference codes of this type.
   */
  private static Map<Integer, VisibilityCode> codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new HashMap<Integer, VisibilityCode>();
    codes.put(PUBLIC_CODE, new VisibilityCode(
        PUBLIC_CODE, 1, "public", "public visibility"));
    codes.put(PROTECTED_CODE, new VisibilityCode(
        PROTECTED_CODE, 2, "protected", "protected visibility"));
    codes.put(PACKAGE_CODE, new VisibilityCode(
        PACKAGE_CODE, 3, "package", "package visibility"));
    codes.put(PRIVATE_CODE, new VisibilityCode(
        PRIVATE_CODE, 4, "private", "private visibility"));
  }

  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   */
  private VisibilityCode(int code, int sortOrder, String shortDescription,
      String description) {
    super(code, sortOrder, shortDescription, description, true);
  }

  /**
   * Returns the list of all reference codes.
   * @return list of all reference codes.
   */
  public static List<VisibilityCode> getCodes() {
    return new ArrayList<VisibilityCode>(codes.values());
  }

  /**
   * Returns the business code associated with the given code.
   * @param value value of the requested code.
   * @return the requested reference code.
   */
  public static VisibilityCode getCode(int value) {
    return codes.get(value);
  }
}