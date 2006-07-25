/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * Wrapper Class for a MDR Package instance This class provides methods to
 * simplify the access to the mdr model
 * @author UeliKurmann
 * @version $Revision$
 */

public class MdrUmlPackage extends MdrUmlElement {

  /**
   * the mdr package
   */
  private UmlPackage umlPackage;

  /**
   * the wrapped mdr model
   */
  private MdrUmlModel model;

  /**
   * Hashtables for full qualified class and package names
   */
  private Hashtable<String, MdrUmlClass> classes;

  private Hashtable<String, MdrUmlPackage> packages;

  /**
   * Constructor
   * @param pkg mdr instance of the package
   * @param model wrapped mdr model
   */
  public MdrUmlPackage(org.omg.uml.modelmanagement.UmlPackage pkg,
      MdrUmlModel model) {
    this.umlPackage = pkg;
    this.classes = new Hashtable<String, MdrUmlClass>();
    this.packages = new Hashtable<String, MdrUmlPackage>();
    this.model = model;
  }

  public String getName() {
    return umlPackage.getName();
  }

  /**
   * Sets the name of the package
   * @param packageName
   */
  public void setName(String packageName) {
    String oldName = getName();
    umlPackage.setName(packageName);
  }

  /**
   * Adds a class or interface to the package
   * @param clazz
   */
  public void addClass(MdrUmlClass clazz) {
    // TODO: clean up problem: interfaces are also classes in the mdr context
    if (!classes.containsKey(clazz.getName())) {
      classes.put(clazz.getName(), clazz);
    } else {
      if (clazz.isInterface()) {
        classes.put(clazz.getName(), clazz);
      }
    }
  }

  /**
   * Updates the name of a owned class
   * @param clazz
   * @param oldName
   */
  public void updateClassName(MdrUmlClass clazz, String oldName) {
    if (classes.contains(oldName)) {
      classes.remove(oldName);
    }
    classes.put(clazz.getName(), clazz);
  }

  /**
   * Returns a collection of owned classes
   * @return collection of wrapped mdr classes
   */
  public Collection<MdrUmlClass> getClasses() {
    return classes.values();
  }

  public String getQualifiedName() {
    Namespace namespace = umlPackage.getNamespace();
    StringBuilder sb = new StringBuilder();
    while (namespace != null && namespace.getNamespace() != null) {
      sb.insert(0, "." + namespace.getName());
      namespace = namespace.getNamespace();
    }

    if (namespace != null) {
      sb.insert(0, namespace.getName());
    }

    if (sb.length() > 0)
      sb.append("." + getName());
    else
      sb.append(getName());

    return sb.toString();
  }

  public MdrUmlModel getModel() {
    return model;
  }

  /**
   * Returns a collection of sub packages
   * @return collection of wrapped mdr sub packages
   */
  public Collection<MdrUmlPackage> getPackages() {
    return packages.values();
  }

  public ModelElement getModelElement() {
    return umlPackage;
  }

  /**
   * Returns a collection of abstractions
   * @return collection of wrapped mdr abstractions
   */
  public Collection<MdrUmlAbstraction> getAbstractions() {
    Collection<MdrUmlAbstraction> result = new ArrayList<MdrUmlAbstraction>();
    Iterator classIter = umlPackage.getOwnedElement().iterator();
    while (classIter.hasNext()) {
      ModelElement me = (ModelElement) classIter.next();
      if (me instanceof Abstraction) {
        result.add(new MdrUmlAbstraction((Abstraction) me));
      }
    }
    return result;
  }

  /**
   * Returns a Class in the current package with the given name
   * @param name
   * @return
   */
  public MdrUmlClass getClassByName(String name) {
    for (MdrUmlClass clazz : getClasses()) {
      if (clazz.getName().equals(name))
        return clazz;
    }
    return null;
  }

  /**
   * Returns the namespace of the package
   * @return namespace namespace of the package
   */
  public Namespace getNamespace() {
    return umlPackage.getNamespace();
  }
}
