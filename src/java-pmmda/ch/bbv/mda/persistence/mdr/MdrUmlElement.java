/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TaggedValue;

import ch.bbv.mda.VisibilityCode;

/**
 * Abstract Wrapper Class for a MDR ModelElement instance This class provides
 * methods to simplify the access to the mdr model
 * @author UeliKurmann
 * @version $Revision$
 */

public abstract class MdrUmlElement {

  public abstract ModelElement getModelElement();

  public String getName() {
    return getModelElement().getName();
  }

  /**
   * Returns the full qualified name of the model element
   * @return name the qualified name of the model element
   */
  public abstract String getQualifiedName();

  /**
   * Returns the Stereotypes of a ModelElement
   * @return Collection of MdrUmlStereotypes
   */
  public Collection<MdrUmlStereotype> getStereotypes() {
    Iterator stereotypeIterator = getModelElement().getStereotype().iterator();
    Collection<MdrUmlStereotype> stereotypes = new ArrayList<MdrUmlStereotype>();
    while (stereotypeIterator.hasNext()) {
      stereotypes.add(new MdrUmlStereotype((Stereotype) stereotypeIterator
          .next()));
    }
    return stereotypes;
  }

  public void addStereotype(MdrUmlStereotype stereotype) {
    getModelElement().getStereotype().add(stereotype.getStereotype());
  }

  /**
   * Returns the Visibility Code
   * @return VisibilityCode
   */
  public VisibilityCode getVisibility() {
    return MdrUtilities.convertVisibility(getModelElement().getVisibility());
  }

  /**
   * Sets the visibility code
   * @param visibility code
   */
  public void setVisibility(VisibilityCode visibility) {
    getModelElement().setVisibility(MdrUtilities.convertVisibility(visibility));
  }

  /**
   * Retruns the Tagged Values
   * @return collection of MdrTaggedValue
   */
  public Collection<MdrTaggedValue> getTaggedValues() {
    Collection<MdrTaggedValue> collection = new ArrayList();
    for (TaggedValue taggedValue : (Collection<TaggedValue>) getModelElement()
        .getTaggedValue()) {
      collection.add(new MdrTaggedValue(taggedValue));
    }
    return collection;
  }

  /**
   * Adds a TaggedValue to the collection of tagged values
   * @param taggedValue
   */
  public void addTaggedValue(MdrTaggedValue taggedValue) {
    getModelElement().getTaggedValue().add(taggedValue.getTaggedValue());
  }

}
