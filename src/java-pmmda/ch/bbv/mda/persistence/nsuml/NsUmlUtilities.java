/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.persistence.nsuml;

import ru.novosoft.uml.foundation.core.MModelElement;

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
    assert (element != null);
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
   * Defines a private constructor to insure that the class is never
   * instantiated.
   */
  private NsUmlUtilities() {
    // empty body
  }
}