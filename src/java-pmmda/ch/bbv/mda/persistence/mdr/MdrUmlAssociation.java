/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.UmlAssociation;

/**
 * Wrapper Class for a MDR Association instance This class provides methods to
 * simplify the access to the mdr model
 * @author UeliKurmann
 * @version $Revision$
 */
public class MdrUmlAssociation extends MdrUmlElement {

  /**
   * the mdr association
   */
  private UmlAssociation association;

  /**
   * constructor
   * @param association the uml association to be wrapped
   */
  public MdrUmlAssociation(UmlAssociation association) {
    this.association = association;
  }

  /**
   * returns the name of the association
   * @return the name of the association
   */
  public String getName() {
    return association.getName();
  }

  /**
   * returns an association end
   * @param i the index of the assoction end
   * @return AssociationEnd the mdr association end
   */
  public AssociationEnd getConnection(int i) {
    return (AssociationEnd) association.getConnection().get(i);
  }

  /**
   * the name of the association
   * @return the name of the association
   */
  public String getQualifiedName() {
    return getName();
  }
  
  public void setContext(Namespace namespace){
    association.setNamespace(namespace);
  }

  /**
   * returns the model element
   * @return ModelElement the association
   */
  public ModelElement getModelElement() {
    return association;
  }
}
