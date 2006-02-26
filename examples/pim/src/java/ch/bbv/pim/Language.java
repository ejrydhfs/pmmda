/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
*/
   
package ch.bbv.pim;

import ch.bbv.dog.reference.ReferenceCodeImpl;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/* %% USER DEFINED IMPORT START %% */
/* %% USER DEFINED IMPORT END %% */

/**
 * The language used to correspond with the contact.
 * @author Marcel Baumann
 */
public class Language extends ReferenceCodeImpl {
  /**
   * The code value associated with the reference code.
   */
  public static final int ENGLISH_ID = 1;
  /**
   * The code value associated with the reference code.
   */
  public static final int GERMAN_ID = 2;
  /**
   * The code value associated with the reference code.
   */
  public static final int FRENCH_ID = 3;
  /**
   * The code value associated with the reference code.
   */
  public static final int ITALIAN_ID = 4;
  /**
   * The code value associated with the reference code.
   */
  public static final int JAPANESE_ID = 10;
  /**
   * The code value associated with the reference code.
   */
  public static final int SPANISH_ID = 5;

  /**
   * The code value associated with the reference code.
   */
  public static final Language ENGLISH;
  /**
   * The code value associated with the reference code.
   */
  public static final Language GERMAN;
  /**
   * The code value associated with the reference code.
   */
  public static final Language FRENCH;
  /**
   * The code value associated with the reference code.
   */
  public static final Language ITALIAN;
  /**
   * The code value associated with the reference code.
   */
  public static final Language JAPANESE;
  /**
   * The code value associated with the reference code.
   */
  public static final Language SPANISH;

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * List of all reference codes of this type.
   */
  private static Map<Integer, Language> codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new HashMap<Integer, Language>();
    ENGLISH = new Language(ENGLISH_ID, 1, "English", "English");
    codes.put(ENGLISH_ID, ENGLISH);
    GERMAN = new Language(GERMAN_ID, 2, "German", "German");
    codes.put(GERMAN_ID, GERMAN);
    FRENCH = new Language(FRENCH_ID, 3, "French", "French");
    codes.put(FRENCH_ID, FRENCH);
    ITALIAN = new Language(ITALIAN_ID, 4, "Italian", "Italian");
    codes.put(ITALIAN_ID, ITALIAN);
    JAPANESE = new Language(JAPANESE_ID, 10, "Japanese", "Japanese");
    codes.put(JAPANESE_ID, JAPANESE);
    SPANISH = new Language(SPANISH_ID, 5, "Spanish", "Spanish");
    codes.put(SPANISH_ID, SPANISH);
  }
  
/* %% USER DEFINED DECLARATION START %% */
/* %% USER DEFINED DECLARATION END %% */

/* %% USER DEFINED CONSTRUCTORS START %% */
/* %% USER DEFINED CONSTRUCTORS END %% */

/* %% USER DEFINED CODE START %% */
/* %% USER DEFINED CODE END %% */

  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type
   * @param sortOrder sort order of the code instance
   * @param shortDescription short description of the code
   * @param description description of the code
   */
  private Language(int code, int sortOrder, String shortDescription,
      String description) {
    super(code, sortOrder, shortDescription, description, true);
  }

  /**
   * Returns the list of all defined reference code values.
   * @return list of all reference codes.
   */
  public static List<Language> getCodes() {
    return new ArrayList<Language>(codes.values());
  }

  /**
   * Returns the business code associated with the given code.
   * @param value value of the requested code
   * @return the requested reference code
   */
  public static Language getCode(int value) {
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
