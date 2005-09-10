/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/
   
package ch.bbv.mda.cartridges;

import ch.bbv.dog.reference.ReferenceCodeImpl;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * The JDBC type reference code implements the JDBC type values as business 
 * code. All values defined in the standard are supported.
 * @version $Revision$
 * @author Marcel Baumann
 */
public class JdbcTypeCode extends ReferenceCodeImpl {
  /**
   * The code value associated with the reference code.
   */
  public static final int ARRAY_ID = 1;
  /**
   * The code value associated with the reference code.
   */
  public static final int BIGINT_ID = 2;
  /**
   * The code value associated with the reference code.
   */
  public static final int BINARY_ID = 3;
  /**
   * The code value associated with the reference code.
   */
  public static final int BIT_ID = 4;
  /**
   * The code value associated with the reference code.
   */
  public static final int BLOB_ID = 5;
  /**
   * The code value associated with the reference code.
   */
  public static final int BOOLEAN_ID = 6;
  /**
   * The code value associated with the reference code.
   */
  public static final int CHAR_ID = 7;
  /**
   * The code value associated with the reference code.
   */
  public static final int CLOB_ID = 8;
  /**
   * The code value associated with the reference code.
   */
  public static final int DATALINK_ID = 9;
  /**
   * The code value associated with the reference code.
   */
  public static final int DATE_ID = 10;
  /**
   * The code value associated with the reference code.
   */
  public static final int DECIMAL_ID = 11;
  /**
   * The code value associated with the reference code.
   */
  public static final int DISTINCT_ID = 12;
  /**
   * The code value associated with the reference code.
   */
  public static final int DOUBLE_ID = 13;
  /**
   * The code value associated with the reference code.
   */
  public static final int FLOAT_ID = 14;
  /**
   * The code value associated with the reference code.
   */
  public static final int INTEGER_ID = 15;
  /**
   * The code value associated with the reference code.
   */
  public static final int JAVA_OBJECT_ID = 16;
  /**
   * The code value associated with the reference code.
   */
  public static final int LONGVARBINARY_ID = 17;
  /**
   * The code value associated with the reference code.
   */
  public static final int LONGVARCHAR_ID = 18;
  /**
   * The code value associated with the reference code.
   */
  public static final int NULL_ID = 19;
  /**
   * The code value associated with the reference code.
   */
  public static final int NUMERIC_ID = 20;
  /**
   * The code value associated with the reference code.
   */
  public static final int OTHER_ID = 21;
  /**
   * The code value associated with the reference code.
   */
  public static final int REAL_ID = 22;
  /**
   * The code value associated with the reference code.
   */
  public static final int REF_ID = 23;
  /**
   * The code value associated with the reference code.
   */
  public static final int SMALLINT_ID = 24;
  /**
   * The code value associated with the reference code.
   */
  public static final int STRUCT_ID = 25;
  /**
   * The code value associated with the reference code.
   */
  public static final int TIME_ID = 26;
  /**
   * The code value associated with the reference code.
   */
  public static final int TIMESTAMP_ID = 27;
  /**
   * The code value associated with the reference code.
   */
  public static final int TINYINT_ID = 28;
  /**
   * The code value associated with the reference code.
   */
  public static final int VAQRBINARY_ID = 29;
  /**
   * The code value associated with the reference code.
   */
  public static final int VARCHAR_ID = 30;

  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode ARRAY;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode BIGINT;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode BINARY;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode BIT;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode BLOB;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode BOOLEAN;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode CHAR;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode CLOB;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode DATALINK;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode DATE;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode DECIMAL;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode DISTINCT;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode DOUBLE;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode FLOAT;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode INTEGER;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode JAVA_OBJECT;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode LONGVARBINARY;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode LONGVARCHAR;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode NULL;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode NUMERIC;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode OTHER;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode REAL;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode REF;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode SMALLINT;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode STRUCT;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode TIME;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode TIMESTAMP;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode TINYINT;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode VAQRBINARY;
  /**
   * The code value associated with the reference code.
   */
  public static final JdbcTypeCode VARCHAR;

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * List of all reference codes of this type.
   */
  private static Map<Integer, JdbcTypeCode> codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new HashMap<Integer, JdbcTypeCode>();
    ARRAY = new JdbcTypeCode(ARRAY_ID, 1, "ARRAY", "ARRAY");
    codes.put(ARRAY_ID, ARRAY);
    BIGINT = new JdbcTypeCode(BIGINT_ID, 2, "BIGINT", "BIGINT");
    codes.put(BIGINT_ID, BIGINT);
    BINARY = new JdbcTypeCode(BINARY_ID, 3, "BINARY", "B INARY");
    codes.put(BINARY_ID, BINARY);
    BIT = new JdbcTypeCode(BIT_ID, 4, "BIT", "BIT");
    codes.put(BIT_ID, BIT);
    BLOB = new JdbcTypeCode(BLOB_ID, 5, "BLOB", "BLOB");
    codes.put(BLOB_ID, BLOB);
    BOOLEAN = new JdbcTypeCode(BOOLEAN_ID, 6, "BOOLEAN", "BOOLEAN");
    codes.put(BOOLEAN_ID, BOOLEAN);
    CHAR = new JdbcTypeCode(CHAR_ID, 7, "CHAR", "CHAR");
    codes.put(CHAR_ID, CHAR);
    CLOB = new JdbcTypeCode(CLOB_ID, 8, "CLOB", "CLOB");
    codes.put(CLOB_ID, CLOB);
    DATALINK = new JdbcTypeCode(DATALINK_ID, 9, "DATALINK", "DATALINK");
    codes.put(DATALINK_ID, DATALINK);
    DATE = new JdbcTypeCode(DATE_ID, 10, "DATE", "DATE");
    codes.put(DATE_ID, DATE);
    DECIMAL = new JdbcTypeCode(DECIMAL_ID, 11, "DECIMAL", "DECIMAL");
    codes.put(DECIMAL_ID, DECIMAL);
    DISTINCT = new JdbcTypeCode(DISTINCT_ID, 12, "DISTINCT", "DISTINCT");
    codes.put(DISTINCT_ID, DISTINCT);
    DOUBLE = new JdbcTypeCode(DOUBLE_ID, 13, "DOUBLE", "DOUBLE");
    codes.put(DOUBLE_ID, DOUBLE);
    FLOAT = new JdbcTypeCode(FLOAT_ID, 14, "FLOAT", "FLOAT");
    codes.put(FLOAT_ID, FLOAT);
    INTEGER = new JdbcTypeCode(INTEGER_ID, 15, "INTEGER", "INTEGER");
    codes.put(INTEGER_ID, INTEGER);
    JAVA_OBJECT = new JdbcTypeCode(JAVA_OBJECT_ID, 16, "JAVA_OBJECT", "JAVA_OBJECT");
    codes.put(JAVA_OBJECT_ID, JAVA_OBJECT);
    LONGVARBINARY = new JdbcTypeCode(LONGVARBINARY_ID, 17, "LONGVARBINARY", "LONGVARBINARY");
    codes.put(LONGVARBINARY_ID, LONGVARBINARY);
    LONGVARCHAR = new JdbcTypeCode(LONGVARCHAR_ID, 18, "LONGVARCHAR", "LONGVARCHAR");
    codes.put(LONGVARCHAR_ID, LONGVARCHAR);
    NULL = new JdbcTypeCode(NULL_ID, 19, "NULL", "NULL");
    codes.put(NULL_ID, NULL);
    NUMERIC = new JdbcTypeCode(NUMERIC_ID, 20, "NUMERIC", "NUMERIC");
    codes.put(NUMERIC_ID, NUMERIC);
    OTHER = new JdbcTypeCode(OTHER_ID, 21, "OTHER", "OTHER");
    codes.put(OTHER_ID, OTHER);
    REAL = new JdbcTypeCode(REAL_ID, 22, "REAL", "REAL");
    codes.put(REAL_ID, REAL);
    REF = new JdbcTypeCode(REF_ID, 23, "REF", "REF");
    codes.put(REF_ID, REF);
    SMALLINT = new JdbcTypeCode(SMALLINT_ID, 24, "SMALLINT", "SMALLINT");
    codes.put(SMALLINT_ID, SMALLINT);
    STRUCT = new JdbcTypeCode(STRUCT_ID, 25, "STRUCT", "STRUCT");
    codes.put(STRUCT_ID, STRUCT);
    TIME = new JdbcTypeCode(TIME_ID, 26, "TIME", "TIME");
    codes.put(TIME_ID, TIME);
    TIMESTAMP = new JdbcTypeCode(TIMESTAMP_ID, 27, "TIMESTAMP", "TIMESTAMP");
    codes.put(TIMESTAMP_ID, TIMESTAMP);
    TINYINT = new JdbcTypeCode(TINYINT_ID, 28, "TINYINT", "TINYINT");
    codes.put(TINYINT_ID, TINYINT);
    VAQRBINARY = new JdbcTypeCode(VAQRBINARY_ID, 29, "VARBINARY", "VARBINARY");
    codes.put(VAQRBINARY_ID, VAQRBINARY);
    VARCHAR = new JdbcTypeCode(VARCHAR_ID, 30, "VARCHAR", "VARCHAR");
    codes.put(VARCHAR_ID, VARCHAR);
  }
  
/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */

  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   */
  private JdbcTypeCode(int code, int sortOrder, String shortDescription,
      String description) {
    super(code, sortOrder, shortDescription, description, true);
  }

  /**
   * Returns the list of all defined reference code values.
   * @return list of all reference codes.
   */
  public static List<JdbcTypeCode> getCodes() {
    return new ArrayList<JdbcTypeCode>(codes.values());
  }

  /**
   * Returns the business code associated with the given code.
   * @param value value of the requested code.
   * @return the requested reference code.
   */
  public static JdbcTypeCode getCode(int value) {
    return codes.get(value);
  }

  /**
   * Guarantees that the returned object is unique in the virtual machine.
   * @return the code read from the stream if defined otherwise null.
   */
  private Object readResolve() {
    return getCode(getCode());
  }  
}
