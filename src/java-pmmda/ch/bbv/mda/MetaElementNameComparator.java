/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda;

import java.util.Comparator;

/**
 * The element name comparator compares two MDA elements by their name.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MetaElementNameComparator implements Comparator {
  /**
   * Compares two elements. The two elements are equivalent if both elements
   * have the same name.
   * @param left tagged value to compare with.
   * @param right tagged value to compare with.
   * @return result of comparing both tag names. The ordering is based on the
   *         name of the element.
   */
  public int compare(Object left, Object right) {
    String leftName = getString(left);
    String rightName = getString(right);
    return (leftName.compareTo(rightName));
  }

  /**
   * Returns the name of the meta element or the string if the object is a
   * string. An illegal cast exception is thrown if the object is not a element
   * or a string.
   * @param object object to transform.
   * @return the requested string value.
   */
  private String getString(Object object) {
    String text;
    if (object instanceof String) {
      text = (String) object;
    } else {
      text = ((MetaElement) object).getName();
    }
    return text;
  }
}