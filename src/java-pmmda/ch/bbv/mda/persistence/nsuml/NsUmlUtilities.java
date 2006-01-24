/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.persistence.nsuml;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.model_management.MPackage;
import ch.bbv.mda.VisibilityCode;

/**
 * The NS UML utilities provides a set of convenience methods to navigate
 * through a NS UML model and extract information.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class NsUmlUtilities {
  /**
   * Returns the qualified name of the model element. The root context is the
   * model itself and does not belong to the qualified name as defined in the
   * Java language.
   * @param element element, which qualified name should be returned.
   * @return the qualified name of the element if defined otherwise an empty
   *         string.
   * @pre element != null
   */
  public static String getQualifiedName(MModelElement element) {
    assert element != null;
    MModelElement context = element;
    StringBuilder buffer = new StringBuilder();
    if ((context != null) && (context.getModelElementContainer() != null)) {
      buffer.append(element.getName());
      context = context.getModelElementContainer();
      while ((context != null) && (context.getModelElementContainer() != null)) {
        buffer.insert(0, ".").insert(0, context.getName());
        context = context.getModelElementContainer();
      }
    }
    return buffer.toString();
  }

  /**
   * Returns the context of the package.
   * @param packages packages which context should be returned.
   * @return the package context if defined otherwise null.
   * @pre packages != null
   */
  public static MPackage getContext(MPackage packages) {
    assert packages != null;
    MModelElement context = packages.getModelElementContainer();
    if ((context == null) || context.getModelElementContainer() == null) {
      context = null;
    }
    return (MPackage) context;
  }

  /**
   * Sets the visibility of the item.
   * @param item item which visibility should be set.
   * @param visibility new visibility of the item.
   * @pre (item != null) && (visibility != null)
   */
  public static void setVisibility(MModelElement item, VisibilityCode visibility) {
    assert (item != null) && (visibility != null);
    if (VisibilityCode.PUBLIC_ID == visibility.getCode()) {
      item.setVisibility(MVisibilityKind.PUBLIC);
    } else if (VisibilityCode.PROTECTED_ID == visibility.getCode()) {
      item.setVisibility(MVisibilityKind.PROTECTED);
    } else if (VisibilityCode.PRIVATE_ID == visibility.getCode()) {
      item.setVisibility(MVisibilityKind.PRIVATE);
    } else if (VisibilityCode.PACKAGE_ID == visibility.getCode()) {
      item.setVisibility(MVisibilityKind.PUBLIC);
    }
  }

  /**
   * Defines a private constructor to insure that the class is never
   * instantiated.
   */
  private NsUmlUtilities() {
    // empty body
  }
}