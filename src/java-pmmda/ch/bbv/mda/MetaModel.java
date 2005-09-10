/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.*;

import ch.bbv.mda.operators.ModelOperators;
import ch.bbv.mda.pim.CodeTypeMgr;

/**
 * The meta project defines a MDA project composed of a set of packages. Overall
 * administrative properties are defined in the class. The class is a Java bean.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MetaModel extends MetaPackage {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The name of the file containing the meta model. The information is managed
   * within the application and is therefore transient.
   */
  private transient String filename;

  /**
   * List of all packages of the model.
   * @serial
   */
  private List<MetaPackage> packages;

  /**
   * List of all data types of the model.
   * @serial
   */
  private List<MetaDatatype> dataTypes;

  /**
   * The code type manager handling the reference codes of the model.
   */
  private transient CodeTypeMgr codeTypeMgr;

  /**
   * Default constructor of the class.
   */
  public MetaModel() {
    packages = new ArrayList<MetaPackage>();
    dataTypes = new ArrayList<MetaDatatype>();
    codeTypeMgr = new CodeTypeMgr();
  }

  /**
   * Returns the code type manager of the document.
   * @return the code type manager property.
   */
  public CodeTypeMgr getCodeTypeMgr() {
    return codeTypeMgr;
  }

  /**
   * Returns the filename where the model is stored.
   * @return the filename of the model.
   * @see #setFilename(String)
   */
  public String getFilename() {
    return filename;
  }

  /**
   * Sets the filename of the model.
   * @param filename new filename where the model is stored.
   * @see #setFilename(String)
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }

  public MetaPackage getPackage(int index) {
    return packages.get(index);
  }

  public void addPackage(MetaPackage metaPackage) {
    packages.add(metaPackage);
    metaPackage.setContext(this);
  }

  public void addPackage(int index, MetaPackage metaPackage) {
    packages.add(index, metaPackage);
    metaPackage.setContext(this);
    Collections.sort(packages, MetaElement.NAME_COMPARATOR);
  }

  public List<MetaPackage> getPackages() {
    return packages;
  }

  public void setPackages(List<MetaPackage> packages) {
    removeAllPackages();
    this.packages.addAll(packages);
    for (MetaPackage metaPackage : packages) {
      metaPackage.setContext(this);
    }
  }

  public void removeAllPackages() {
    for (MetaPackage metaPackage : packages) {
      metaPackage.setContext(null);
    }
    packages.clear();
  }

  /**
   * Removes a package from the model.
   * @param metaPackage to be removed.
   */
  public void removePackage(MetaPackage metaPackage) {
    if (packages.contains(metaPackage)) {
      packages.remove(metaPackage);
      metaPackage.setContext(null);
    }
  }

  public MetaDatatype getDataType(int index) {
    return dataTypes.get(index);
  }

  public void addDataType(MetaDatatype metaDataType) {
    dataTypes.add(metaDataType);
    metaDataType.setContext(this);
  }

  public void addDataType(int index, MetaDatatype metaDataType) {
    dataTypes.add(index, metaDataType);
    metaDataType.setContext(this);
    Collections.sort(packages, MetaElement.NAME_COMPARATOR);
  }

  public List<MetaDatatype> getDataTypes() {
    return dataTypes;
  }

  public void setDataTypes(List<MetaDatatype> dataTypes) {
    removeAllDataTypes();
    dataTypes.addAll(dataTypes);
    for (MetaDatatype dataType : dataTypes) {
      dataType.setContext(this);
    }
  }

  public void removeAllDataTypes() {
    for (MetaDatatype dataType : dataTypes) {
      dataType.setContext(null);
    }
    dataTypes.clear();
  }

  public void removeDataType(MetaDatatype dataType) {
    if (dataTypes.contains(dataType)) {
      dataTypes.remove(dataType);
      dataType.setContext(null);
    }
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
    for (MetaPackage metaPackage : packages) {
      metaPackage.accept(visitor);
    }
    for (MetaClass clazz : getClasses()) {
      clazz.accept(visitor);
    }
    for (MetaDatatype datatype : getDataTypes()) {
      datatype.accept(visitor);
    }
  }

  // %% BEGIN USER CODE

  public MetaPackage findPackage(String qualifiedName) {
    return (MetaPackage) findElement(packages, qualifiedName);
  }

  public MetaDatatype findDataType(String qualifiedName) {
    return (MetaDatatype) findElement(dataTypes, qualifiedName);
  }

  /**
   * Finds the class with the given qualified name.
   * @param qualifiedName qualified name of the class to find.
   * @return the requested class if found otherwise null.
   */
  public MetaClass findClass(String qualifiedName) {
    MetaClass clazz = null;
    if (qualifiedName != null) {
      String packageName = ModelOperators.getContextName(qualifiedName);
      if (packageName != null) {
        MetaPackage metaPackage = findPackage(packageName);
        if (metaPackage != null) {
          clazz = metaPackage.findClass(ModelOperators
              .getComponentName(qualifiedName));
        }
      }
    }
    return clazz;
  }

  /**
   * Returns the list of meta classes referencing the given class.
   * @param clazz class which references are looked for.
   * @return the list of classes referencing the given class.
   */
  public Collection getClassReferences(MetaClass clazz) {
    assert clazz != null;
    VisitorCollectClassReferences visitor = new VisitorCollectClassReferences();
    visitor.setReferencedClass(clazz);
    accept(visitor);
    return visitor.getReferences();
  }

  /**
   * Returns all classes being a subclass of the given class.
   * @param clazz the class which subclasses are looked for.
   * @return the list of classes derived from the given one.
   * @pre clazz != null
   */
  public Set<MetaClass> findSubClasses(MetaClass clazz) {
    assert clazz != null;
    Set<MetaClass> classes = new HashSet<MetaClass>();
    for (MetaPackage mPackage : getPackages()) {
      for (MetaClass subClass : mPackage.getClasses()) {
        if (subClass.hasAncestor(clazz)) {
          classes.add(subClass);
        }
      }
    }
    return classes;
  }

  // %% END USER CODE
}