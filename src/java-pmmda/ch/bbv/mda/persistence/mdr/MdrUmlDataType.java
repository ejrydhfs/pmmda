/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.ModelElement;

/**
 * Wrapper Class for a MDR Datatype instance This class provides methods to
 * simplify the access to the mdr model
 * @author UeliKurmann
 * @version $Revision$
 */

public class MdrUmlDataType extends MdrUmlElement {

  DataType dataType;

  public MdrUmlDataType(DataType stereotype) {
    this.dataType = stereotype;
  }

  /**
   * Returns the name of the datatype
   * @return name the name of the datatype
   */
  public String getName() {
    return dataType.getName();
  }

  /**
   * Returns the full qualified name of the datatype
   * @return name the full qualified name of the datatype
   */
  public String getQualifiedName() {
    return MdrUtilities.getFullQualifiedName(dataType);
  }

  /**
   * Returns the mdr instance of the datatype
   * @return ModelElement the mdr datatype
   */
  public ModelElement getModelElement() {
    return dataType;
  }
}
