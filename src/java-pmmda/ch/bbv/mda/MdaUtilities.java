/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import java.util.Iterator;
import java.util.List;

/**
 * The MDA utilities define a set of convenience methods to manipulate various
 * attributes of a MDA model.
 * @author MarcelBaumann
 * @version $Revision$
 */
public final class MdaUtilities {
  /**
   * String representation of the public visibility.
   */
  public static final String PUBLIC_TEXT = "public";

  /**
   * String representation of the protected visibility.
   */
  public static final String PROTECTED_TEXT = "protected";

  /**
   * String representation of the package private visibility.
   */
  public static final String PACKAGE_TEXT = "";

  /**
   * String representation of the private visibility.
   */
  public static final String PRIVATE_TEXT = "private";

  /**
   * Default hidden constructor to implement the static class pattern.
   */
  private MdaUtilities() {
    // implements the static class pattern.
  }

  /**
   * Returns true if the qualified name belongs to a standard package. Standard
   * packages belongs to the package java or javax.
   * @param qualifiedName name of the item to check.
   * @return true if it belongs to a standard package otherwise false.
   */
  public static boolean isStandardItem(String qualifiedName) {
    return ((qualifiedName != null) && (qualifiedName.startsWith("java") || qualifiedName
        .startsWith("javax")));
  }

  /**
   * Returns true if the given list contains an element with the qualified name.
   * @param elements list of elements to search.
   * @param qualifiedName qualified name of the requested element.
   * @return the requested element if found otherwise null.
   */
  public static boolean containsElementNamed(List elements, String qualifiedName) {
    assert ((elements != null) && (qualifiedName != null));

    boolean containsElementNamed = false;
    for (Iterator i = elements.iterator(); i.hasNext() && !containsElementNamed;) {
      MetaElement element = (MetaElement) i.next();
      containsElementNamed = qualifiedName.equals(element.qualifiedName());
    }
    return containsElementNamed;
  }

  /**
   * Returns an identifier representation of the qualified name.
   * @param qualifiedName qualified name to transform.
   * @return the transformed qualified name.
   */
  public static String qualifiedNameToIdentifier(String qualifiedName) {
    return qualifiedName.replaceAll(".", "_");
  }

  /**
   * Returns the string representation of the element visibility.
   * @param visibility visibility code.
   * @return the string representation of the visibility.
   */
  public static String visibilityToString(int visibility) {
    String text = "";
    switch (visibility) {
    case VisibilityCode.PUBLIC_VISIBILITY_CODE:
      text = PUBLIC_TEXT;
      break;
    case VisibilityCode.PROTECTED_VISIBILITY_CODE:
      text = PROTECTED_TEXT;
      break;
    case VisibilityCode.PACKAGE_VISIBILITY_CODE:
      text = PACKAGE_TEXT;
      break;
    case VisibilityCode.PRIVATE_VISIBILITY_CODE:
      text = PRIVATE_TEXT;
      break;
    }
    return text;
  }
}