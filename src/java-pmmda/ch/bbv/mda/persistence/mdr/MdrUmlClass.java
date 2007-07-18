/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.UmlClass;

/**
 * Wrapper Class for a MDR Class instance This class provides methods to
 * simplify the access to the mdr model
 * 
 * @author UeliKurmann
 * @version $Revision$
 */

public class MdrUmlClass extends MdrUmlElement {

  /**
   * the mdr class
   */
  private Classifier element;

  /**
   * the owner package
   */
  private MdrUmlPackage ownerPackage;

  /**
   * table of methods
   */
  private Hashtable<String, MdrUmlMethod> methods;

  /**
   * Constructor
   * 
   * @param umlClass
   *          a MDR classifier
   */
  public MdrUmlClass(Classifier umlClass) {
    this(umlClass, null);
  }

  /**
   * Constructor
   * 
   * @param umlClass
   *          the mdr classifier
   * @param ownerPackage
   *          the owner package
   */
  public MdrUmlClass(Classifier umlClass, MdrUmlPackage ownerPackage) {
    this.element = umlClass;

    this.ownerPackage = ownerPackage;
    methods = new Hashtable<String, MdrUmlMethod>();
    // iterate over features (methods)
    for (Object featureObj : umlClass.getFeature()) {
      Feature feature = (Feature) featureObj;
      if (feature instanceof Operation) {
        methods.put(feature.getName(), new MdrUmlMethod((Operation) feature,
            this));
      }
    }
  }

  /**
   * Returns a collection of attributes
   * 
   * @return Collection<MdrUmlAttribute> a collection of wrapped mdr attributes
   */
  public Collection<MdrUmlAttribute> getAttributes() {
    ArrayList<MdrUmlAttribute> result = new ArrayList<MdrUmlAttribute>();
    for(Object featureObj:element.getFeature()){
      Feature feature = (Feature) featureObj;
      if (feature instanceof Attribute) {
        result.add(new MdrUmlAttribute((Attribute) feature, this));
      }
    }
    return result;
  }

  /**
   * Adds an attribute tot the class
   * 
   * @param attribute
   *          the wrapped mdr attribute
   */
  @SuppressWarnings("unchecked")
  public void addAttribute(MdrUmlAttribute attribute) {
    element.getFeature().add(attribute.getModelElement());
  }

  /**
   * returns the model note: sometimes it isn't possible to return the model. in
   * this case null is returned
   * 
   * @return MdrUmlModel the model or null
   */
  public MdrUmlModel getModel() {
    return getPackage() != null ? getPackage().getModel() : null;
  }

  /**
   * the name of the class
   * 
   * @return name the unqualified name of the class
   */
  public String getName() {
    return element.getName();
  }

  /**
   * returns the owner package of the class
   * 
   * @return the package
   */
  public MdrUmlPackage getPackage() {
    return ownerPackage;
  }

  /**
   * returns the full qualified name of the class
   * 
   * @return qualified name of the class
   */
  public String getQualifiedName() {
    return MdrUtilities.getFullQualifiedName((Classifier) getModelElement());
  }

  /**
   * returns the stereotype of the class
   * 
   * @return name of the stereotype
   */
  public String getStereotype() {
    try {
      return ((Stereotype) element.getStereotype().iterator().next()).getName();
    } catch (RuntimeException e) {
      return null;
    }
  }

  /**
   * States whether the class is abstract
   * 
   * @return boolean true iff the class is abstract, else false
   */
  public boolean isAbstract() {
    return element.isAbstract();
  }

  /**
   * States whether the class is final
   * 
   * @return boolean true iff the class is final, else false
   */
  public boolean isFinal() {
    return element.isLeaf();
  }

  /**
   * States whether this classifier is an interface
   * 
   * @return boolean true iff it is an interfacne, else false
   */
  public boolean isInterface() {
    return element instanceof Interface;
  }

  /**
   * States whether the class is static Attention: This method isn't implemented
   * and returns always false
   * 
   * @return boolean true iff the class is static, else false
   */
  public boolean isStatic() {
    return false;
  }

  /**
   * Sets the class as static Attention: no value is set actually
   * 
   * @param isStatic
   */
  public void setStatic(boolean isStatic) {
    //
  }

  /**
   * Sets the abstract modifier
   * 
   * @param isAbstract
   *          true if abstract
   */
  public void setAbstract(boolean isAbstract) {
    element.setAbstract(isAbstract);
  }

  /**
   * Sets final modifier
   * 
   * @param isFinal
   *          true if class is final
   */
  public void setFinal(boolean isFinal) {
    element.setLeaf(isFinal);
  }

  /**
   * Sets the name of the class
   * 
   * @param name
   *          the name of the class
   */
  public void setName(String name) {
    element.setName(name);
  }

  /**
   * Returns the uml instance of the class
   * 
   * @return classifer the mdr classifier
   */
  public ModelElement getModelElement() {
    return element;
  }

  /**
   * Returns a collection of wrapped innerclass instances
   * 
   * @return collection of wrapped mdr classes
   */
  public Collection<MdrUmlClass> getInnerClasses() {
    Collection<MdrUmlClass> result = new ArrayList<MdrUmlClass>();
    for (Object modelElement : element.getOwnedElement()) {
      if (modelElement instanceof UmlClass) {
        result.add(new MdrUmlClass((Classifier) modelElement));
      }
    }
    return result;
  }
}
