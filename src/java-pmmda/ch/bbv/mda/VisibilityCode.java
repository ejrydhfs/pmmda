/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.bbv.dog.reference.ReferenceCodeImpl;

/**
 * The visibility reference code defines the available visibility levels in Java
 * as business codes.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class VisibilityCode extends ReferenceCodeImpl {
  /**
   * The code for the public visibility.
   */
  public static final int PUBLIC_VISIBILITY_CODE = 1;

  /**
   * The code for the protected visibility.
   */
  public static final int PROTECTED_VISIBILITY_CODE = 2;

  /**
   * The code for the package visibility.
   */
  public static final int PACKAGE_VISIBILITY_CODE = 3;

  /**
   * The code for the private visibility.
   */
  public static final int PRIVATE_VISIBILITY_CODE = 4;

  /**
   * List of all reference codes of this type.
   */
  private static List codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new ArrayList();
    codes.add(new VisibilityCode(PUBLIC_VISIBILITY_CODE, 1, "public",
        "public visibility"));
    codes.add(new VisibilityCode(PROTECTED_VISIBILITY_CODE, 2, "protected",
        "protected visibility"));
    codes.add(new VisibilityCode(PACKAGE_VISIBILITY_CODE, 3, "package",
        "package visibility"));
    codes.add(new VisibilityCode(PRIVATE_VISIBILITY_CODE, 4, "private",
        "private visibility"));
  }

  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   */
  public VisibilityCode(int code, int sortOrder, String shortDescription,
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
   * Returns the business code associated with the given code.
   * @param value value of the requested code.
   * @return the requested reference code.
   */
  public static VisibilityCode getCode(int value) {
    VisibilityCode visibility = null;
    for (Iterator i = codes.iterator(); visibility == null && i.hasNext();) {
      visibility = (VisibilityCode) i.next();
      if (visibility.getCode() != value) {
        visibility = null;
      }
    }
    return visibility;
  }
}