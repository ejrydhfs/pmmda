/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.reference.ReferenceCodeImpl;

/**
 * The element kind code implements the kind of meta elements supported in the
 * application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ElementKind extends ReferenceCodeImpl {
  private static final long serialVersionUID = 20041201;

  /**
   * Code for package kind code reference code.
   */
  public static final int PACKAGE_CODE = 1;

  /**
   * Code for class kind code reference code.
   */
  public static final int CLASS_CODE = 2;

  /**
   * Code for property kind code reference code.
   */
  public static final int PROPERTY_KIND = 3;

  /**
   * Code for indexed property kind code reference code.
   */
  public static final int INDEXED_PROPERTY_KIND = 4;

  /**
   * List of all reference codes of this type.
   */
  private static List<ElementKind> codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new ArrayList<ElementKind>();
    codes.add(new ElementKind(PACKAGE_CODE, 1, "package", "package"));
    codes.add(new ElementKind(CLASS_CODE, 2, "class", "class"));
    codes.add(new ElementKind(PROPERTY_KIND, 3, "property", "property"));
    codes.add(new ElementKind(INDEXED_PROPERTY_KIND, 4, "indexed property",
        "indexed property"));
  }

  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   */
  public ElementKind(int code, int sortOrder, String shortDescription,
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
}