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
public class MetaElementNameComparator implements Comparator<MetaElement> {
  /**
   * Compares two elements. The two elements are equivalent if both elements
   * have the same name.
   * @param left tagged value to compare with.
   * @param right tagged value to compare with.
   * @return result of comparing both tag names. The ordering is based on the
   *         name of the element.
   */
  public int compare(MetaElement left, MetaElement right) {
    return (left.getName().compareTo(right.getName()));
  }
}