/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package org.sf.pmmda.development.taglets;

import java.util.Map;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

/** The taglet implements the precondition tag introduced to support
 * programming by contract. The writer should use the OCL syntax.
 * Consult the documentation of the Doclet interface provided with JavaDoc for
 * a description of the methods of this class.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class PreTaglet extends GenericTaglet implements Taglet {
  /** Name of the taglet.
   */
  private static final String NAME = "pre";

  public PreTaglet() {
    super(NAME);
  }

  /** Registers the taglet in the taglet map.
   * @param tagletMap map of all registered taglets.
   */
  public static void register(Map<String, Taglet> tagletMap) {
    GenericTaglet.register(new PreTaglet(), tagletMap);
  }

  public boolean inConstructor() {
    return true;
  }

  public boolean inMethod() {
    return true;
  }

  public String toString(Tag tag) {
    return convert(tag, "Precondition: ");
  }
}
