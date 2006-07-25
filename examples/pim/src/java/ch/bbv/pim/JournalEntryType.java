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

public class JournalEntryType extends ReferenceCodeImpl {
  /**
   * The code value associated with the reference code.
   */
  public static final int EMAIL_ID = 1;
  /**
   * The code value associated with the reference code.
   */
  public static final int PHONE_ID = 2;
  /**
   * The code value associated with the reference code.
   */
  public static final int LETTER_ID = 3;
  /**
   * The code value associated with the reference code.
   */
  public static final int MEETING_ID = 4;
  /**
   * The code value associated with the reference code.
   */
  public static final int NOTE_ID = 5;

  /**
   * The code value associated with the reference code.
   */
  public static final JournalEntryType EMAIL;
  /**
   * The code value associated with the reference code.
   */
  public static final JournalEntryType PHONE;
  /**
   * The code value associated with the reference code.
   */
  public static final JournalEntryType LETTER;
  /**
   * The code value associated with the reference code.
   */
  public static final JournalEntryType MEETING;
  /**
   * The code value associated with the reference code.
   */
  public static final JournalEntryType NOTE;

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * List of all reference codes of this type.
   */
  private static Map<Integer, JournalEntryType> codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new HashMap<Integer, JournalEntryType>();
    EMAIL = new JournalEntryType(EMAIL_ID, 1, "email", "email");
    codes.put(EMAIL_ID, EMAIL);
    PHONE = new JournalEntryType(PHONE_ID, 2, "phone call", "phone call");
    codes.put(PHONE_ID, PHONE);
    LETTER = new JournalEntryType(LETTER_ID, 3, "letter", "letter");
    codes.put(LETTER_ID, LETTER);
    MEETING = new JournalEntryType(MEETING_ID, 4, "meeting", "meeting");
    codes.put(MEETING_ID, MEETING);
    NOTE = new JournalEntryType(NOTE_ID, 5, "note", "note");
    codes.put(NOTE_ID, NOTE);
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
  private JournalEntryType(int code, int sortOrder, String shortDescription,
      String description) {
    super(code, sortOrder, shortDescription, description, true);
  }

  /**
   * Returns the list of all defined reference code values.
   * @return list of all reference codes.
   */
  public static List<JournalEntryType> getCodes() {
    return new ArrayList<JournalEntryType>(codes.values());
  }

  /**
   * Returns the business code associated with the given code.
   * @param value value of the requested code
   * @return the requested reference code
   */
  public static JournalEntryType getCode(int value) {
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
