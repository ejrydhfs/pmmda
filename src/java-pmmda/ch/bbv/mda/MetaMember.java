/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import ch.bbv.mda.operators.ModelOperators;

/**
 * Represents a member of a java class such as a field. This is an abstract
 * class dealing with information common to method, constructor and field
 * members.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class MetaMember extends MetaElement {
  /**
   * Type property of the class. The type is the qualified name of the class
   * defining the type of the property or the name of the primitive type.
   * @serial
   */
  private String typeName;

  /**
   * Classifier of the member corresponding to the typename. The field is lazy
   * initialized.
   */
  private MetaClassifier classifier;

  /**
   * Flag indicating if the member is static or not.
   * @serial
   */
  private boolean statics;

  /**
   * Flag indicating if the member is final or not.
   * @serial
   */
  private boolean finals;

  /**
   * Returns the type name of the element. If a classifier is defined the name
   * of the classifier is returned otherwise the stored type name.
   * @return qualified type name of the element
   * @see #setTypeName(String)
   */
  public String getTypeName() {
    return (classifier == null ? typeName : classifier.getQualifiedName());
  }

  /**
   * Sets the type name of the element. The classifier is updated.
   * @param typeName new qualified type name of the element
   * @see #getTypeName()
   */
  public void setTypeName(String typeName) {
    this.typeName = typeName;
    classifier = retrieveClassifier();
  }

  /**
   * Returns the classifier of the class.
   * @return the classifier of the member
   * @see #setClassifier(MetaClassifier)
   */
  public MetaClassifier getClassifier() {
    return retrieveClassifier();
  }

  /**
   * Sets the classifier of the member.
   * @param classifier new classifier of the member
   * @see #getClassifier()
   */
  public void setClassifier(MetaClassifier classifier) {
    this.classifier = classifier;
    typeName = getTypeName();
  }

  /**
   * Returns the flag indicating if the member is static or not.
   * @return value of the static flag
   * @see #setStatic(boolean)
   */
  public boolean isStatic() {
    return statics;
  }

  /**
   * Sets the flag indicating if the member is static or not.
   * @param statics new value of the flag
   * @see #isStatic()
   */
  public void setStatic(boolean statics) {
    this.statics = statics;
  }

  /**
   * Returns the flag indicating if the member is final or not.
   * @return value of the static flag
   * @see #setFinal(boolean)
   */
  public boolean isFinal() {
    return finals;
  }

  /**
   * Sets the flag indicating if the member is final or not.
   * @param finals new value of the flag
   * @see #isFinal()
   */
  public void setFinal(boolean finals) {
    this.finals = finals;
  }

  /**
   * Returns the simple name of the type of the element.
   * @return the class name of the type
   */
  public String retrieveTypeClassName() {
    return ModelOperators.getComponentName(getTypeName());
  }

  /**
   * Returns the classifier instance of the member. The algorithm first looks at
   * the datatypes and if not found at the classes, and last for use cases.
   * @return the classifier associated with the typename of the member if found
   *         otherwise null
   */
  public MetaClassifier retrieveClassifier() {
    if (classifier == null) {
      return getModel().findClassifier(typeName);
    }
    return classifier;
  }

  @Override
  public MetaClass getContext() {
    return (MetaClass) super.getContext();
  }
}