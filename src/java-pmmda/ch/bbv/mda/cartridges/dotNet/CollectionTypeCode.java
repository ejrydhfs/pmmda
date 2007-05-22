/* 
 * copyright 2005 http://www.bbv.ch
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 */

package ch.bbv.mda.cartridges.dotNet;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.reference.ReferenceCodeImpl;

/**
 * The collection reference code implements the possible types of
 * indexed properties.<br/>
 * These types are specified by NHibernate.
 * See <a href="http://nhibernate.sourceforge.net/h2.0.3-docs/reference/html/collections.html">
 * here</a> for further information.
 * @author Mikkala Pedersen
 */
public class CollectionTypeCode extends ReferenceCodeImpl {

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20050321;
 
  /**
   * unordered, indexed, doesn't duplicates
   */
  public static final int BAG  = 1;
  
  /**
   * ordered, indexed, doesn't duplicates
   */
  public static final int LIST = 2;
  
  /**
   * unordered, unindexed, doesn't allow duplicates
   */
  public static final int SET  = 3;
  
  /**
   * List of all possible values
   */
  private static List<CollectionTypeCode> codes;
  
  static {
    codes = new ArrayList<CollectionTypeCode>();
    codes.add(new CollectionTypeCode(BAG, 1, "Bag", "Bag"));
    codes.add(new CollectionTypeCode(LIST, 2, "List", "List"));
    codes.add(new CollectionTypeCode(SET, 3, "Set", "Set"));
  }
  
  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   */
  public CollectionTypeCode(int code, int sortOrder, String shortDescription,
      String description) {
    super(code, sortOrder, shortDescription, description, true);
  }
  
  /**
   * Returns the list of all collection type codes.
   * @return The list of all collection type codes.
   */
  public static List<CollectionTypeCode> getCodes() {
    return codes;
  }

  /**
   * Returns the collection type code associated with the given value.
   * @param code value of the requested collection type code.
   * @return the requested collection type code.
   */
  public static CollectionTypeCode getCode(int code) {
    for (CollectionTypeCode item : codes) {
      if (item.getCode() == code) {
        return item;
      }
    }
    return null;
  }

  private Object readResolve() {
    return getCode(getCode());
  }
}
