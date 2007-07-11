/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TaggedValue;

import ch.bbv.mda.VisibilityCode;

/**
 * Abstract Wrapper Class for a MDR ModelElement instance This class provides
 * methods to simplify the access to the MDR model
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
    Collection<MdrUmlStereotype> stereotypes = new ArrayList<MdrUmlStereotype>();
    for(Object stereoType:getModelElement().getStereotype()){
      stereotypes.add(new MdrUmlStereotype((Stereotype) stereoType));
    }
    return stereotypes;
  }

  @SuppressWarnings("unchecked")
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
   * Returns the Tagged Values
   * @return collection of MdrTaggedValue
   */
  public List<MdrTaggedValue> getTaggedValues() {
    List<MdrTaggedValue> taggedValues = new ArrayList<MdrTaggedValue>();
    for (Object taggedValue : (Collection<?>) getModelElement().getTaggedValue()) {
      taggedValues.add(new MdrTaggedValue((TaggedValue)taggedValue));
    }
    return taggedValues;
  }
  
  /**
   * Returns the Tagged Values with the given name
   * @param name the name of the tagged values
   * @return the list of tagged values
   */
  public List<MdrTaggedValue> getTaggedValue(String name){
    List<MdrTaggedValue> result = new ArrayList<MdrTaggedValue>();
    for(MdrTaggedValue taggedValue:getTaggedValues()){
      if(taggedValue.getName().equals(name)){
        result.add(taggedValue);
      }
    }
    return result;
  }

  /**
   * Adds a TaggedValue to the collection of tagged values
   * @param taggedValue
   */
  @SuppressWarnings("unchecked")
  public void addTaggedValue(MdrTaggedValue taggedValue) {
    getModelElement().getTaggedValue().add(taggedValue.getTaggedValue());
  }

}
