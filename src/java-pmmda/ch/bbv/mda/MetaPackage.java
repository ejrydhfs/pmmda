/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.*;

/**
 * The meta package models a package object as defined in the UML standard. It
 * provides additional services to retrieve tagged values associated with it.
 * These values are used to map the independent application model to the
 * platform specific architecture. The following tagged values are defined.
 * <ul>
 * <dt>shouldBeGenerated</dt>
 * <dd>Boolean flag indicating if the package should be mapped to the platform
 * or not. The default value is false.</dd>
 * <dt>ifPackage</dt>
 * <dd>The package in which the interfaces of the data object interfaces should
 * be generated. If null no interfaces will be generated. The default value is
 * null.</dd>
 * <dt>boPackage</dt>
 * <dd>The package in which the data object classes will be generated. If null
 * no classes will be generated. The default value is null.</dd>
 * <dt>lwPackage</dt>
 * <dd>The package in which the lightweight object classes will be generated.
 * If null no lightweight classes will be generated. The default value is null.
 * </dd>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MetaPackage extends MetaElement {
  /**
   * List of all classes defined in the package.
   */
  private List classes;

  /**
   * Flag indicating if the package belongs to the standard packages of the
   * environment or not.
   */
  private boolean system;

  /**
   * Default constructor of the class.
   */
  public MetaPackage() {
    classes = new ArrayList();
  }

  /**
   * Returns true if the package belongs to the standards ones otherwise false.
   * @return the value of the flag.
   * @see #setSystem(boolean)
   */
  public boolean isSystem() {
    return system;
  }

  /**
   * Sets the new value of the package.
   * @param system true if the package is a standard one otherwise false.
   * @see #isSystem()
   */
  public void setSystem(boolean system) {
    this.system = system;
  }

  /**
   * Returns the class with the given index.
   * @param index index of the requested class.
   * @return the requested class.
   */
  public MetaClass getClass(int index) {
    return (MetaClass) classes.get(index);
  }

  /**
   * Adds the class in the list of classes of the package.
   * @param metaClass class to add.
   */
  public void addClass(MetaClass metaClass) {
    classes.add(metaClass);
    metaClass.setContext(this);
  }

  /**
   * Adds the class in the list of classes of the package.
   * @param index position where the class should be inserted in the list.
   * @param metaClass class to add.
   */
  public void addClass(int index, MetaClass metaClass) {
    classes.add(index, metaClass);
    metaClass.setContext(this);
  }

  /**
   * Returns a list of all classes of the package.
   * @return list of all classes.
   * @see #setClasses(List)
   */
  public List getClasses() {
    return classes;
  }

  /**
   * Set the list of classes of the package.
   * @param classes new list of classes.
   * @see #getClasses()
   */
  public void setClasses(List classes) {
    removeAllClasses();
    classes.addAll(classes);

    for (Iterator i = classes.iterator(); i.hasNext();) {
      ((MetaClass) i.next()).setContext(this);
    }
  }

  public void removeAllClasses() {
    for (Iterator i = classes.iterator(); i.hasNext();) {
      ((MetaClass) i.next()).setContext(null);
    }
    classes.clear();
  }

  /**
   * Implements the visitor pattern.
   * @param visitor visitor to process.
   */
  public void accept(Visitor visitor) {
    visitor.visit(this);
    for (Iterator i = classes.iterator(); i.hasNext();) {
      ((MetaClass) i.next()).accept(visitor);
    }
  }

  public MetaClass findClass(String name) {
    return (MetaClass) findElement(classes, name);
  }
}