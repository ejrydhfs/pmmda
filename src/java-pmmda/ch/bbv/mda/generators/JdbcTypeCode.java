/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.generators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.bbv.dog.reference.ReferenceCodeImpl;

/**
 * The JDBC type reference code implements the JDBC type values as business
 * code.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class JdbcTypeCode extends ReferenceCodeImpl {
  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int STRING = 1;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int BIGDECIMAL = 2;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int BOOLEAN = 3;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int BYTE = 4;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int SHORT = 5;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int INTEGER = 6;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int LONG = 7;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int FLOAT = 8;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int DOUBLE = 9;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int DATE = 10;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int TIME = 11;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int TIMESTAMP = 12;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int BINARY = 13;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int BLOB = 14;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int CLOB = 15;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int STRUCT = 16;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int REF = 17;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int DATALINK = 18;

  /**
   * Code for the associated JDBC type reference code.
   */
  public static final int JAVA_OBJECT = 19;

  /**
   * List of all reference codes of this type.
   */
  private static List codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new ArrayList();
    codes.add(new JdbcTypeCode(STRING, 1, "String",
        "CHAR, VARCHAR, LONGVARCHAR"));
    codes
        .add(new JdbcTypeCode(BIGDECIMAL, 2, "BigDecimal", "NUMERIC, DECIMAL"));
    codes.add(new JdbcTypeCode(BOOLEAN, 3, "Boolean", "BIT, BOOLEAN"));
    codes.add(new JdbcTypeCode(BYTE, 4, "Byte", "TINYINT"));
    codes.add(new JdbcTypeCode(SHORT, 5, "Short", "SMALLINT"));
    codes.add(new JdbcTypeCode(INTEGER, 6, "Integer", "Integer"));
    codes.add(new JdbcTypeCode(LONG, 7, "Long", "Long"));
    codes.add(new JdbcTypeCode(FLOAT, 8, "Float", "REAL"));
    codes.add(new JdbcTypeCode(DOUBLE, 9, "Double", "FLOAT, DOUBLE"));
    codes.add(new JdbcTypeCode(DATE, 10, "Date", "DATE"));
    codes.add(new JdbcTypeCode(TIME, 11, "Time", "TIME"));
    codes.add(new JdbcTypeCode(TIMESTAMP, 12, "Timestamp", "TIMESTAMP"));
    codes.add(new JdbcTypeCode(BINARY, 13, "byte []",
        "BINARY, VARBINARY, LONGVARBINARY"));
    codes.add(new JdbcTypeCode(BLOB, 14, "Blob", "BLOB"));
    codes.add(new JdbcTypeCode(CLOB, 15, "Clob", "CLOB"));
    codes.add(new JdbcTypeCode(STRUCT, 16, "Struct", "STRUCT"));
    codes.add(new JdbcTypeCode(REF, 17, "Ref", "REF"));
    codes.add(new JdbcTypeCode(DATALINK, 18, "URL", "DATALINK"));
    codes.add(new JdbcTypeCode(JAVA_OBJECT, 19, "Java Object", "JAVA_OBJECT"));
  }

  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   */
  public JdbcTypeCode(int code, int sortOrder, String shortDescription,
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
   * Returns the code instance associated with the given identifier.
   * @param code identifier of the requested code.
   * @return the requested code if found otherwise null.
   */
  public static JdbcTypeCode getCode(int code) {
    JdbcTypeCode visibility = null;
    for (Iterator i = codes.iterator(); visibility == null && i.hasNext();) {
      visibility = (JdbcTypeCode) i.next();
      if (visibility.getCode() != code) {
        visibility = null;
      }
    }
    return visibility;
  }
}