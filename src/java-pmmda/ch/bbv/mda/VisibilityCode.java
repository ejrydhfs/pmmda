/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/
   
package ch.bbv.mda;

import ch.bbv.dog.reference.ReferenceCodeImpl;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * The visibility reference code defines the available visibility levels in UML 
 * as business codes. These codes covers all the visibility values for common 
 * languages such as Java or C++.
 * @version $Revision$
 * @author Marcel Baumann
 */
public class VisibilityCode extends ReferenceCodeImpl {
  /**
   * The code value associated with the reference code.
   */
  public static final int PUBLIC_ID = 1;
  /**
   * The code value associated with the reference code.
   */
  public static final int PROTECTED_ID = 2;
  /**
   * The code value associated with the reference code.
   */
  public static final int PACKAGE_ID = 3;
  /**
   * The code value associated with the reference code.
   */
  public static final int PRIVATE_ID = 4;

  /**
   * The code value associated with the reference code.
   */
  public static final VisibilityCode PUBLIC;
  /**
   * The code value associated with the reference code.
   */
  public static final VisibilityCode PROTECTED;
  /**
   * The code value associated with the reference code.
   */
  public static final VisibilityCode PACKAGE;
  /**
   * The code value associated with the reference code.
   */
  public static final VisibilityCode PRIVATE;

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * List of all reference codes of this type.
   */
  private static Map<Integer, VisibilityCode> codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new HashMap<Integer, VisibilityCode>();
    PUBLIC = new VisibilityCode(PUBLIC_ID, 1, "public", "public");
    codes.put(PUBLIC_ID, PUBLIC);
    PROTECTED = new VisibilityCode(PROTECTED_ID, 2, "protected", "protected");
    codes.put(PROTECTED_ID, PROTECTED);
    PACKAGE = new VisibilityCode(PACKAGE_ID, 3, "package", "package");
    codes.put(PACKAGE_ID, PACKAGE);
    PRIVATE = new VisibilityCode(PRIVATE_ID, 4, "private", "private");
    codes.put(PRIVATE_ID, PRIVATE);
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
  private VisibilityCode(int code, int sortOrder, String shortDescription,
      String description) {
    super(code, sortOrder, shortDescription, description, true);
  }

  /**
   * Returns the list of all defined reference code values.
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

  /**
   * Guarantees that the returned object is unique in the virtual machine.
   * @return the code read from the stream if defined otherwise null.
   */
  private Object readResolve() {
    return getCode(getCode());
  }  
}
