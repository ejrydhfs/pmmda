/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

/**
 * Represents a member of a java class such as a field, a constructor, or a
 * method. This is an abstract class dealing with information common to method,
 * constructor and field members.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class MetaMember extends MetaElement {
  /**
   * Type property of the class. The type is the qualified name of the class
   * defining the type of the property or the name of the primitive type.
   */
  private String typeName;

  /**
   * Flag indicating if the member is static or not.
   */
  private boolean statics;

  /**
   * Flag indicating if the member is final or not.
   */
  private boolean finals;

  /**
   * Returns the type name of the element.
   * @return qualified type name of the element.
   * @see #setTypeName
   */
  public String getTypeName() {
    return typeName;
  }

  /**
   * Sets the type name of the element.
   * @param typeName new qualified type name of the element.
   * @see #getTypeName
   */
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  /**
   * Returns the flag indicating if the member is static or not.
   * @return value of the static flag.
   * @see #setStatic
   */
  public boolean isStatic() {
    return statics;
  }

  /**
   * Sets the flag indicating if the member is static or not.
   * @param statics new value of the flag.
   * @see #isStatic
   */
  public void setStatic(boolean statics) {
    this.statics = statics;
  }

  /**
   * Returns the flag indicating if the member is final or not.
   * @return value of the static flag.
   * @see #setFinal
   */
  public boolean isFinal() {
    return finals;
  }

  /**
   * Sets the flag indicating if the member is final or not.
   * @param finals new value of the flag.
   * @see #isFinal
   */
  public void setFinal(boolean finals) {
    this.finals = finals;
  }

  /**
   * Retrieves the package name of the type of the element.
   * @return the package name of the type.
   */
  public String retrieveTypePackageName() {
    return MetaClass.getPackageName(getTypeName());
  }

  /**
   * Returns the class n ame of the type of the element.
   * @return the class name of the type.
   */
  public String retrieveTypeClassName() {
    return MetaClass.getClassName(getTypeName());
  }

  /**
   * Indicates whether this model element is a leaf (no children)
   */
  public boolean isLeaf() {
    return true;
  }
}