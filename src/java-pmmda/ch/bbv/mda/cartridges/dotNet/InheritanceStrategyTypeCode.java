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
 * The inheritance strategy code implements the possible inheritance strategies.<br/>
 * See <a href="http://www.hibernate.org/hib_docs/v3/reference/en/html/inheritance.html#inheritance-strategies">
 * here</a> for further information.
 * @author Mikkala Pedersen
 */
public class InheritanceStrategyTypeCode extends ReferenceCodeImpl {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20050324;

  /**
   * See <a href="http://www.hibernate.org/hib_docs/v3/reference/en/html/inheritance.html#inheritance-tablepersubclass">
   * here</a> for further information.
   */
  public static final int TABLE_PER_SUBCLASS = 1;
  
  /**
   * See <a href="http://www.hibernate.org/hib_docs/v3/reference/en/html/inheritance.html#inheritance-tableperconcrete">
   * here</a> for further information.
   */
  public static final int TABLE_PER_CONCRETE_CLASS = 2;
  
  /**
   * See <a href="http://www.hibernate.org/hib_docs/v3/reference/en/html/inheritance.html#inheritance-tableperclass">
   * here</a> for further information.
   */
  public static final int TABLE_PER_CLASS_HIERARCHY = 3;
  
  /**
   * List of all possible values
   */
  private static List<InheritanceStrategyTypeCode> codes;
  
  static {
    codes = new ArrayList<InheritanceStrategyTypeCode>();
    codes.add(new InheritanceStrategyTypeCode(TABLE_PER_SUBCLASS, 1, "Table per subclass", "For each class there is a table. The tables contain just the properties defined in this class."));
    codes.add(new InheritanceStrategyTypeCode(TABLE_PER_CONCRETE_CLASS, 2, "Table per concrete class", "For each class there is a table which contains all properties (includes derived proeprties)."));
    codes.add(new InheritanceStrategyTypeCode(TABLE_PER_CLASS_HIERARCHY, 3, "Table per class hierarchy", "For each class hierarchy exists one table with all possible properties. Property names must be unique and properties must have the .net-nhibernate-can-be-null tagged value set to true."));
  }
  
  /**
   * Constructor initializes the immutable instance with all property values.
   * @param code unique identifier for the code of this type.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param description description of the code.
   */
  public InheritanceStrategyTypeCode(int code, int sortOrder, String shortDescription,
      String description) {
    super(code, sortOrder, shortDescription, description, true);
  }
  
  /**
   * Returns the list of all inheritance strategies codes.
   * @return The list of all inheritance strategies codes.
   */
  public static List<InheritanceStrategyTypeCode> getCodes() {
    return codes;
  }

  /**
   * Returns the inheritance strategy code associated with the given value.
   * @param code value of the requested inheritance strategy code.
   * @return the requested collection type code.
   */
  public static InheritanceStrategyTypeCode getCode(int code) {
    for (InheritanceStrategyTypeCode item : codes) {
      if (item.getCode() == code) {
        return item;
      }
    }
    return null;
  }

  /**
   * Guarantees that the returned object is unique in the virtual machine.
   * @return the code read from the stream if defined otherwise null.
   */
  private Object readResolve() {
    return getCode(getCode());
  }
}
