/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.Model;

/**
 * Wrapper Class for a MDR Model instance This class provides methods to
 * simplify the access to the mdr model
 * 
 * @author UeliKurmann
 * @version $Revision$
 */

public class MdrUmlModel{
  /**
   * Logger
   */
  //private Log log = LogFactory.getLog(MdrUmlModel.class);

  private Hashtable<String, MdrUmlPackage> packages;

  private Hashtable<String, MdrUmlClass> classes;

  private UmlPackage umlPackage;

  /**
   * Constructor
   * 
   * @param umlPackage
   */
  public MdrUmlModel(UmlPackage umlPackage) {
    this.umlPackage = umlPackage;
    this.packages = new Hashtable<String, MdrUmlPackage>();
    this.classes = new Hashtable<String, MdrUmlClass>();

    // process packages
    for (Object modelElement : umlPackage.getModelManagement().getCore()
        .getModelElement().refAllOfType()) {
      ModelElement me = (ModelElement) modelElement;
      if (me instanceof org.omg.uml.modelmanagement.UmlPackage) {
        MdrUmlPackage pkg = new MdrUmlPackage(
            (org.omg.uml.modelmanagement.UmlPackage) me, this);
        packages.put(pkg.getQualifiedName(), pkg);
      }
    }

    // process classes and interfaces
    for (Object modelElement : umlPackage.getModelManagement().getCore()
        .getModelElement().refAllOfType()) {
      ModelElement me = (ModelElement) modelElement;
      if (me instanceof UmlClass || me instanceof Interface) {

        String qualifiedName = getQualifiedName(me.getNamespace());
        try {
          MdrUmlPackage mdrUmlPackage = getPackage(qualifiedName);
          MdrUmlClass tmpClass = new MdrUmlClass((Classifier) me,
              getPackage(qualifiedName));
          mdrUmlPackage.addClass(tmpClass);
        } catch (Exception e) {
          // do nothing
        }
      }
    }
  }

  /**
   * getPackageName
   * 
   * @param namespace
   * @return a return value
   */
  public String getQualifiedName(Namespace namespace) {
    assert namespace != null;

    if (namespace.getNamespace() == null)
      return namespace.getName();

    String qualifiedName = getQualifiedName(namespace.getNamespace()) + "."
        + namespace.getName();

    return qualifiedName;
  }

  /**
   * Creates a new MdrUmlPackage
   * 
   * @param qualifiedName
   *          the qualified name of the package
   * @param pkg
   *          the mdr instance of the package
   * @return a mdr package wrapper
   */
  public MdrUmlPackage createPackage(String qualifiedName,
      org.omg.uml.modelmanagement.UmlPackage pkg) {
    if (!packages.containsKey(qualifiedName)) {
      packages.put(qualifiedName, new MdrUmlPackage(pkg, this));
    }
    return getPackage(qualifiedName);
  }

  /**
   * adds a package to the hash table
   * 
   * @param qualifiedName
   * @param pkg
   */
  public void addPackage(String qualifiedName, MdrUmlPackage pkg) {
    if (!packages.containsKey(qualifiedName)) {
      packages.put(qualifiedName, pkg);
    }
  }

  /**
   * gets a package from the hash table
   * 
   * @param qualifiedName
   * @return the wrapped mdr package
   */
  public MdrUmlPackage getPackage(String qualifiedName) {
    return packages.get(qualifiedName);
  }

  /**
   * gets the class specified with the qualified name
   * 
   * @param qualifiedName
   *          the qualified name of the class
   * @return MdrUmlClass the wrapped mdr class
   */
  public MdrUmlClass getClass(String qualifiedName) {
    return classes.get(qualifiedName);
  }

  /**
   * adds a class to the class hash table
   * 
   * @param clazz
   *          the class to add
   */
  public void addClass(MdrUmlClass clazz) {
    classes.put(clazz.getQualifiedName().trim(), clazz);
  }

  /**
   * Returns all classes
   * 
   * @return a collection of wrapped mdr classes
   */
  public Collection<MdrUmlClass> getClasses() {
    return classes.values();
  }

  /**
   * Returns all qualified class names
   * 
   * @return a collection of the qualified class names
   */
  public Collection<String> getClassNames() {
    return classes.keySet();
  }

  /**
   * returns the name of the model
   * 
   * @return the model name
   */
  public String getName() {
    for (Object model : umlPackage.getModelManagement().getModel()
        .refAllOfClass()) {
      return ((Model) model).getName();
    }
    return null;
  }

  /**
   * sets the name of the model
   * 
   * @param name
   *          the name of the model
   */
  public void setName(String name) {
    for (Object model : umlPackage.getModelManagement().getModel()
        .refAllOfClass()) {
      ((Model) model).setName(name);
      return;
    }
  }

  /**
   * returns all packages referenced in the hash table
   * 
   * @return collection of wrapped mdr packages
   */
  public Collection<MdrUmlPackage> getPackages() {
    return packages.values();
  }

  /**
   * Returns all stereotypes of the model
   * 
   * @return collection of wrapped mdr stereotypes
   */
  public Collection<MdrUmlStereotype> getStereotypes() {
    Collection<MdrUmlStereotype> result = new ArrayList<MdrUmlStereotype>();
    for (Object modelElement : umlPackage.getModelManagement().getCore()
        .getModelElement().refAllOfType()) {
      ModelElement me = (ModelElement) modelElement;
      if (me instanceof Stereotype) {
        result.add(new MdrUmlStereotype((Stereotype) me));
      }
    }
    return result;
  }

  /**
   * Returns all datatypes of the model
   * 
   * @return collection of wrapped mdr datatypes
   */
  public Collection<MdrUmlDataType> getDataTypes() {
    Collection<MdrUmlDataType> result = new ArrayList<MdrUmlDataType>();
    for(Object modelElement:umlPackage.getModelManagement().getCore()
        .getModelElement().refAllOfType()){
      ModelElement me = (ModelElement) modelElement;
      if (me instanceof DataType) {
        result.add(new MdrUmlDataType((DataType) me));
      }
    }
    return result;
  }

  /**
   * Returns a datatype specified by it's name
   * 
   * @param name
   *          the name of the datatype
   * @return the wrapped mdr datatype
   */
  public MdrUmlDataType getDataType(String name) {
    for (MdrUmlDataType dataType : getDataTypes()) {
      if (name.equals(dataType.getName())) {
        return dataType;
      }
    }
    return null;
  }

  /**
   * Returns a collection of gerneralizations
   * 
   * @return collection of wrapped mdr generalizations
   */
  public Collection<MdrUmlGeneralization> getGeneralizations() {
    Collection<MdrUmlGeneralization> result = new ArrayList<MdrUmlGeneralization>();
    for(Object modelElement:umlPackage.getModelManagement().getCore()
        .getModelElement().refAllOfType()){
      ModelElement me = (ModelElement) modelElement;
      if (me instanceof Generalization) {
        result.add(new MdrUmlGeneralization((Generalization) me));
      }
    }
    return result;
  }

  /**
   * Returns a collection of mdr abstractions
   * 
   * @return collection of wrapped mdr abstractions
   */
  public Collection<MdrUmlAbstraction> getAbstractions() {
    Collection<MdrUmlAbstraction> result = new ArrayList<MdrUmlAbstraction>();
    for(Object modelElement:umlPackage.getModelManagement().getCore()
        .getModelElement().refAllOfType()){
      ModelElement me = (ModelElement) modelElement;
      if (me instanceof Abstraction) {
        result.add(new MdrUmlAbstraction((Abstraction) me));
      }
    }
    return result;
  }

  /**
   * Returns a collection mdr associations
   * 
   * @return collection of wrapped mdr associations
   */
  public Collection<MdrUmlAssociation> getAssociations() {
    Collection<MdrUmlAssociation> result = new ArrayList<MdrUmlAssociation>();
    for(Object modelElement:umlPackage.getModelManagement().getCore()
        .getModelElement().refAllOfType()){
      ModelElement me = (ModelElement) modelElement;
      if (me instanceof UmlAssociation) {
        result.add(new MdrUmlAssociation((UmlAssociation) me));
      }
    }
    return result;
  }

  /**
   * Returns the UmlPackage
   * 
   * @return UmlPackage the UML package of the model
   */
  public UmlPackage getUmlPackage() {
    return this.umlPackage;
  }

  
}
