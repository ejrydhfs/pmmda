/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The visitor collects all references to classes in the model. The reference
 * list can be used to determine dependencies between classes.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class VisitorCollectClassReferences implements Visitor {
  /** 
   * Qualified name of the class which references are collected.
   */
  private String className;
  
  /** 
   * Set of classes referencing the class being inferred.
   */
  private Set classes;

  public VisitorCollectClassReferences() {
    classes = new HashSet();
  }

	/** Returns the list of classes referencing the class defined in the visitor.
	 * @return the list of classes referencing the defined class.
	 */
  public Collection getReferences() {
    return classes;
  }

	/** Sets the class which references should be looked for.
	 * @param clazz class which references will be collected.
	 */
  public void setReferencedClass(MetaClass clazz) {
    assert (clazz != null);
    className = clazz.qualifiedName();
    classes.clear();
  }

  public void visit(MetaModel model) {
    assert (model != null);
    classes.clear();
  }

  public void visit(MetaPackage metaPackage) {
    // empty body
  }

  public void visit(MetaClass metaClass) {
    // empty body
  }

  public void visit(MetaProperty property) {
    String typeName = property.getTypeName();
    if (className.equals(typeName)) {
      MetaClass type = (MetaClass) property.getContext();
        classes.add(type);
    }
  }

  public void visit(MetaIndexedProperty property) {
    String typeName = property.getTypeName();
    if (className.equals(typeName)) {
      MetaClass type = (MetaClass) property.getContext();
        classes.add(type);
    }
  }

  public void visit(TaggedValue taggedValue) {
    // empty body
  }
}
