/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda;

/**
 * The adornment type defines an adornment element of the UML/MDA model.
 * Different kinds of adornments are defined as subclassses.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class AdornmentType {
  /**
   * Name of the adornment type.
   */
  private String name;

  /**
   * Flag indicating if the adornment can be used in a package context.
   */
  private boolean packageScope;

  /**
   * Flag indicating if the adornment can be used in a class context.
   */
  private boolean classScope;

  /**
   * Flag indicating if the adornment can be used in a method context.
   */
  private boolean methodScope;

  /**
   * Flag indicating if the adornment can be used in a field context.
   */
  private boolean fieldScope;

  /**
   * Returns the name of the adornment.
   * @return Returns the name.
   * @see #setName(String)
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the adornment.
   * @param name new name of the adornment.
   * @see #getName()
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the flag indicating if the adornment can be used in this scope.
   * @return the scope flag for the requested scope.
   * @see #setPackageScope(boolean)
   */
  public boolean isPackageScope() {
    return packageScope;
  }

  /**
   * Sets the flag indicating if the adornment can be used in this scope.
   * @param packageScope new value of the flag.
   * @see #isPackageScope()
   */
  public void setPackageScope(boolean packageScope) {
    this.packageScope = packageScope;
  }

  /**
   * Returns the flag indicating if the adornment can be used in this scope.
   * @return the scope flag for the requested scope.
   * @see #setClassScope(boolean)
   */
  public boolean isClassScope() {
    return classScope;
  }

  /**
   * Sets the flag indicating if the adornment can be used in this scope.
   * @param classScope new value of the flag.
   * @see #isClassScope()
   */
  public void setClassScope(boolean classScope) {
    this.classScope = classScope;
  }

  /**
   * Returns the flag indicating if the adornment can be used in this scope.
   * @return the scope flag for the requested scope.
   * @see #setMethodScope(boolean)
   */
  public boolean isMethodScope() {
    return methodScope;
  }

  /**
   * Sets the flag indicating if the adornment can be used in this scope.
   * @param methodScope new value of the flag.
   * @see #isMethodScope()
   */
  public void setMethodScope(boolean methodScope) {
    this.methodScope = methodScope;
  }

  /**
   * Returns the flag indicating if the adornment can be used in this scope.
   * @return the scope flag for the requested scope.
   * @see #setFieldScope(boolean)
   */
  public boolean isFieldScope() {
    return fieldScope;
  }

  /**
   * Sets the flag indicating if the adornment can be used in this scope.
   * @param fieldScope new value of the flag.
   * @see #isFieldScope()
   */
  public void setFieldScope(boolean fieldScope) {
    this.fieldScope = fieldScope;
  }
}