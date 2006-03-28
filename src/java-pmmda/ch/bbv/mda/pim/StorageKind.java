/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/
   
package ch.bbv.mda.pim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.bbv.dog.reference.ReferenceCodeImpl;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * The storage kind defines how the values of the reference code type should be 
 * stored.
 * @version $Revision$
 * @author ${element.valueOfTag("author")}
 */
public class StorageKind extends ReferenceCodeImpl {
  /**
   * The code value associated with the reference code.
   */
  public static final int CODE_ID = 0;
  /**
   * The code value associated with the reference code.
   */
  public static final int PROPERTIES_ID = 1;
  /**
   * The code value associated with the reference code.
   */
  public static final int DATABASE_ID = 2;

  /**
   * The code value associated with the reference code.
   */
  public static final StorageKind CODE;
  /**
   * The code value associated with the reference code.
   */
  public static final StorageKind PROPERTIES;
  /**
   * The code value associated with the reference code.
   */
  public static final StorageKind DATABASE;

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * List of all reference codes of this type.
   */
  private static Map<Integer, StorageKind> codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new HashMap<Integer, StorageKind>();
    CODE = new StorageKind(CODE_ID, 1, "code", "code");
    codes.put(CODE_ID, CODE);
    PROPERTIES = new StorageKind(PROPERTIES_ID, 2, "properties", "properties");
    codes.put(PROPERTIES_ID, PROPERTIES);
    DATABASE = new StorageKind(DATABASE_ID, 3, "database", "database");
    codes.put(DATABASE_ID, DATABASE);
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
  private StorageKind(int code, int sortOrder, String shortDescription,
      String description) {
    super(code, sortOrder, shortDescription, description, true);
  }

  /**
   * Returns the list of all defined reference code values.
   * @return list of all reference codes.
   */
  public static List<StorageKind> getCodes() {
    return new ArrayList<StorageKind>(codes.values());
  }

  /**
   * Returns the business code associated with the given code.
   * @param value value of the requested code.
   * @return the requested reference code.
   */
  public static StorageKind getCode(int value) {
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
