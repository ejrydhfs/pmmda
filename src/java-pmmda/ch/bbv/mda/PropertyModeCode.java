/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.reference.ReferenceCodeImpl;

/**
 * The property mode code implements the access mode for property as business
 * code.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class PropertyModeCode extends ReferenceCodeImpl {
  /**
   * Code for the read access mode reference code.
   */
  public static final int READ = 1;

  /**
   * Code for the write access mode reference code.
   */
  public static final int WRITE = 2;

  /**
   * Code for the read and write access mode reference code.
   */
  public static final int READ_WRITE = 3;

  /**
   * List of all reference codes of this type.
   */
  private static List codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new ArrayList();
    codes.add(new PropertyModeCode(READ, 1, "READ", "READ"));
    codes.add(new PropertyModeCode(WRITE, 2, "WRITE", "WRITE"));
    codes.add(new PropertyModeCode(READ_WRITE, 3, "READ_WRITE", "READ_WRITE"));
  }

  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   */
  public PropertyModeCode(int code, int sortOrder, String shortDescription, String description) {
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