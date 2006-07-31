/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.operators;

import java.util.List;

import ch.bbv.mda.MetaElement;
import ch.bbv.mda.Stereotype;
import ch.bbv.mda.VisibilityCode;

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
  public static final String PACKAGE_TEXT = "package";

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
   * @param qualifiedName name of the item to check
   * @return true if it belongs to a standard package otherwise false
   */
  public static boolean isStandardItem(String qualifiedName) {
    return ((qualifiedName != null) && (qualifiedName.startsWith("java") || qualifiedName
        .startsWith("javax")));
  }

  /**
   * Returns true if the given list contains an element with the qualified name.
   * @param elements list of elements to search
   * @param qualifiedName qualified name of the requested element
   * @return the requested element if found otherwise null
   */
  public static boolean containsElementNamed(List<MetaElement> elements,
      String qualifiedName) {
    assert (elements != null) && (qualifiedName != null);
    boolean containsElementNamed = false;
    for (MetaElement element : elements) {
      containsElementNamed = qualifiedName.equals(element.getQualifiedName());
      if (containsElementNamed) {
        break;
      }
    }
    return containsElementNamed;
  }

  /**
   * Returns the string representation of the element visibility.
   * @param visibility visibility code
   * @return the string representation of the visibility
   */
  public static String visibilityToString(int visibility) {
    String text = "";
    switch (visibility) {
    case VisibilityCode.PUBLIC_ID:
      text = PUBLIC_TEXT;
      break;
    case VisibilityCode.PROTECTED_ID:
      text = PROTECTED_TEXT;
      break;
    case VisibilityCode.PACKAGE_ID:
      text = PACKAGE_TEXT;
      break;
    case VisibilityCode.PRIVATE_ID:
      text = PRIVATE_TEXT;
      break;
    }
    return text;
  }

  /**
   * Adds the stereotype with the given name to the item. If the stereotype is
   * not defined in the model, creates and registers it in the model otherwise
   * no activities are performed.
   * @param item item to which the stereotype should be added
   * @param name the name of the stereotype to add
   */
  public static void addStereotype(MetaElement item, String name) {
    if ((name != null) && (name.trim().length() > 0)) {
      Stereotype stereotype = item.getModel().findStereotype(name);
      if (stereotype == null) {
        stereotype = new Stereotype();
        stereotype.setName(name);
        item.getModel().addStereotype(stereotype);
      }
    }
  }
}