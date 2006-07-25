/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import org.omg.uml.foundation.core.Stereotype;

/**
 * Wrapper Class for a MDR Stereotype instance This class provides methods to
 * simplify the access to the mdr model
 * @author UeliKurmann
 * @version $Revision$
 */

public class MdrUmlStereotype {

  private Stereotype stereotype;

  /**
   * Constructor
   * @param stereotype
   */
  public MdrUmlStereotype(Stereotype stereotype) {
    this.stereotype = stereotype;
  }

  /**
   * Returns the stereotype
   * @return stereotype
   */
  public Stereotype getStereotype() {
    return stereotype;
  }

  /**
   * Sets the stereotype
   * @return stereotype
   */
  public String getName() {
    return stereotype.getName();
  }
}
