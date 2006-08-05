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
 * The class models a legal country defined on earch. The set of countries are 
 * defined in an ISO standard.
 * @author Marcel Baumann
 */
public class Country extends ReferenceCodeImpl {
  /**
   * The code value associated with the reference code.
   */
  public static final int SWITZERLAND_ID = 1;
  /**
   * The code value associated with the reference code.
   */
  public static final int GERMANY_ID = 2;
  /**
   * The code value associated with the reference code.
   */
  public static final int FRANCE_ID = 3;
  /**
   * The code value associated with the reference code.
   */
  public static final int ITALY_ID = 4;
  /**
   * The code value associated with the reference code.
   */
  public static final int HOLLAND_ID = 5;
  /**
   * The code value associated with the reference code.
   */
  public static final int SPAIN_ID = 6;
  /**
   * The code value associated with the reference code.
   */
  public static final int PORTUGAL_ID = 7;
  /**
   * The code value associated with the reference code.
   */
  public static final int USA_ID = 20;
  /**
   * The code value associated with the reference code.
   */
  public static final int CANADA_ID = 21;
  /**
   * The code value associated with the reference code.
   */
  public static final int LICHTENSTEIN_ID = 8;
  /**
   * The code value associated with the reference code.
   */
  public static final int POLAN_ID = 9;
  /**
   * The code value associated with the reference code.
   */
  public static final int GREAT_BRITAIN_ID = 10;
  /**
   * The code value associated with the reference code.
   */
  public static final int ISRAEL_ID = 11;

  /**
   * The code value associated with the reference code.
   */
  public static final Country SWITZERLAND;
  /**
   * The code value associated with the reference code.
   */
  public static final Country GERMANY;
  /**
   * The code value associated with the reference code.
   */
  public static final Country FRANCE;
  /**
   * The code value associated with the reference code.
   */
  public static final Country ITALY;
  /**
   * The code value associated with the reference code.
   */
  public static final Country HOLLAND;
  /**
   * The code value associated with the reference code.
   */
  public static final Country SPAIN;
  /**
   * The code value associated with the reference code.
   */
  public static final Country PORTUGAL;
  /**
   * The code value associated with the reference code.
   */
  public static final Country USA;
  /**
   * The code value associated with the reference code.
   */
  public static final Country CANADA;
  /**
   * The code value associated with the reference code.
   */
  public static final Country LICHTENSTEIN;
  /**
   * The code value associated with the reference code.
   */
  public static final Country POLAN;
  /**
   * The code value associated with the reference code.
   */
  public static final Country GREAT_BRITAIN;
  /**
   * The code value associated with the reference code.
   */
  public static final Country ISRAEL;

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 99990101;

  /**
   * List of all reference codes of this type.
   */
  private static Map<Integer, Country> codes;

  /**
   * Initializes the reference codes of this type.
   */
  static {
    codes = new HashMap<Integer, Country>();
    SWITZERLAND = new Country(SWITZERLAND_ID, 1, "Switzerland", "Switzerland");
    codes.put(SWITZERLAND_ID, SWITZERLAND);
    GERMANY = new Country(GERMANY_ID, 2, "Germany", "Germany");
    codes.put(GERMANY_ID, GERMANY);
    FRANCE = new Country(FRANCE_ID, 3, "France", "France");
    codes.put(FRANCE_ID, FRANCE);
    ITALY = new Country(ITALY_ID, 4, "Italy", "Italy");
    codes.put(ITALY_ID, ITALY);
    HOLLAND = new Country(HOLLAND_ID, 5, "Holland", "Holland");
    codes.put(HOLLAND_ID, HOLLAND);
    SPAIN = new Country(SPAIN_ID, 6, "Spain", "Spain");
    codes.put(SPAIN_ID, SPAIN);
    PORTUGAL = new Country(PORTUGAL_ID, 7, "Portugal", "Portugal");
    codes.put(PORTUGAL_ID, PORTUGAL);
    USA = new Country(USA_ID, 20, "USA", "United States of America");
    codes.put(USA_ID, USA);
    CANADA = new Country(CANADA_ID, 21, "Canada", "Canada");
    codes.put(CANADA_ID, CANADA);
    LICHTENSTEIN = new Country(LICHTENSTEIN_ID, 8, "Lichtenstein", "Lichtenstein");
    codes.put(LICHTENSTEIN_ID, LICHTENSTEIN);
    POLAN = new Country(POLAN_ID, 9, "Polan", "Polan");
    codes.put(POLAN_ID, POLAN);
    GREAT_BRITAIN = new Country(GREAT_BRITAIN_ID, 10, "UK", "Great Britain");
    codes.put(GREAT_BRITAIN_ID, GREAT_BRITAIN);
    ISRAEL = new Country(ISRAEL_ID, 11, "Israel", "Isreal");
    codes.put(ISRAEL_ID, ISRAEL);
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
  private Country(int code, int sortOrder, String shortDescription,
      String description) {
    super(code, sortOrder, shortDescription, description, true);
  }

  /**
   * Returns the list of all defined reference code values.
   * @return list of all reference codes.
   */
  public static List<Country> getCodes() {
    return new ArrayList<Country>(codes.values());
  }

  /**
   * Returns the business code associated with the given code.
   * @param value value of the requested code
   * @return the requested reference code
   */
  public static Country getCode(int value) {
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
