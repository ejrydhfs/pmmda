/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

/**
 * @author AdrianBachofen Finds a class based on its qualified name within the
 *         entire model. If not present it returns null;
 */
public class VisitorFindClass implements Visitor {

  /**
   * Qualified class name to be searched in the entire MetaModel
   */
  private String qualifiedClassName;

  /**
   * Qualified name minus packet string. Usually the class name.
   */
  private String classNameTrunc;

  /**
   * Result class found during iteration.
   */
  private MetaClass metaClass;

  /**
   * Default constructor of the visitor.
   * @param qualifiedClassName to be looked-up.
   */
  public VisitorFindClass(String qualifiedClassName) {
    initialize(qualifiedClassName);
  }

  /**
   * Member to reinitialize an already constructed visitor. Resets the result and sets
   * the qualified name to the parameter value.
   * @param qualifiedClassName to be looked-up.
   */
  public void initialize(String qualifiedClassName) {
    this.qualifiedClassName = qualifiedClassName;
    classNameTrunc = qualifiedClassName;
    metaClass = null;
  }

  /**
   * Result after the visit (model.accept(this visitor))
   * @return null if no appropriate class found, other wise the found class.
   */
  public MetaClass getResult() {
    return metaClass;
  }

  /**
   * No operation performed.
   * @see ch.bbv.mda.Visitor#visit(ch.bbv.mda.MetaModel)
   */
  public void visit(MetaModel item) {
  }

  /**
   * Removes packet name from qualified name if match found.
   * Class name (trunc) is stored into classNameTrunc. 
   * @see ch.bbv.mda.Visitor#visit(ch.bbv.mda.MetaPackage)
   */
  public void visit(MetaPackage item) {
    if (classNameTrunc.startsWith(item.getName())) {
      String temp = classNameTrunc.substring(item.getName().length() + 1);
      if (temp.indexOf(".") == -1)
        classNameTrunc = classNameTrunc.substring(item.getName().length() + 1);
    }
  }

  /**
   * Compares class name with current trunc. If match then result is set.
   * @see ch.bbv.mda.Visitor#visit(ch.bbv.mda.MetaClass)
   */
  public void visit(MetaClass item) {
    if (MetaClass.getClassName(item.getName()).equals(classNameTrunc)) {
      metaClass = item;
    }
  }

  /**
   * No operation performed.
   * @see ch.bbv.mda.Visitor#visit(ch.bbv.mda.MetaProperty)
   */
  public void visit(MetaProperty item) {
  }

  /**
   * No operation performed.
   * @see ch.bbv.mda.Visitor#visit(ch.bbv.mda.MetaIndexedProperty)
   */
  public void visit(MetaIndexedProperty item) {
  }

  /**
   * No operation performed.
   * @see ch.bbv.mda.Visitor#visit(ch.bbv.mda.TaggedValue)
   */
  public void visit(TaggedValue item) {
  }

}