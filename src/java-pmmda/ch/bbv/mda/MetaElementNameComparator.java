/* copyright 2000-2004 Marcel Baumann 
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