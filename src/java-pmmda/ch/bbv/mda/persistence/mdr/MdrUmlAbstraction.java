/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import java.util.Collection;

import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.ModelElement;

/**
 * Wrapper Class for a MDR Abstraction instance This class provides methods to
 * simplify the access to the mdr model
 * @author UeliKurmann
 * @version $Revision$
 */

public class MdrUmlAbstraction extends MdrUmlElement {

  /**
   * the mdr abstraction
   */
  Abstraction abstraction;

  /**
   * constructor
   * @param stereotype
   */
  public MdrUmlAbstraction(Abstraction stereotype) {
    this.abstraction = stereotype;
  }

  /**
   * returns the mdr element
   * @return the model element
   */
  public ModelElement getModelElement() {
    return abstraction;
  }

  /**
   * returns the name of the abstraction
   * @return the name of the abstraction
   */
  public String getName() {
    return abstraction.getName();
  }

  /**
   * returns the qualified name
   * @return null
   */
  public String getQualifiedName() {
    return getName();
  }

  /**
   * returns a collection of suppliers
   * @return collection of suppliers
   */
  @SuppressWarnings("unchecked")
  public Collection<ModelElement> getSuppliers() {
    return (Collection<ModelElement>)abstraction.getSupplier();
  }

  /**
   * returns a collection of clients
   * @return collection of clients
   */
  @SuppressWarnings("unchecked")
  public Collection<ModelElement> getClients() {
    return (Collection<ModelElement>)abstraction.getClient();
  }
}
