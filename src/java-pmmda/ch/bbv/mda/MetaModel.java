/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.*;

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
  private List<MetaDatatype> datatypes;

  /**
   * The code type manager handling the reference codes of the model.
   */
  private transient CodeTypeMgr codeTypeMgr;

  /**
   * Default constructor of the class.
   */
  public MetaModel() {
    packages = new ArrayList<MetaPackage>();
    datatypes = new ArrayList<MetaDatatype>();
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
   * @see #getFilename()
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   * Returns the package with the given index.
   * @param index index of the requested class.
   * @return the requested class.
   * @see #addPackage(int, MetaPackage)
   */
  public MetaPackage getPackage(int index) {
    return packages.get(index);
  }

  /**
   * Adds the package in the list of packages.
   * @param packages package to add.
   * @pre packages != null
   */
  public void addPackage(MetaPackage packages) {
    assert packages != null;
    this.packages.add(packages);
    packages.setContext(this);
  }

  /**
   * Adds the package in the list of packages.
   * @param index position where the package should be inserted in the list.
   * @param packages package to add.
   * @pre packages != null
   * @see #getPackage(int)
   */
  public void addPackage(int index, MetaPackage packages) {
    assert packages != null;
    this.packages.add(index, packages);
    packages.setContext(this);
    Collections.sort(this.packages, MetaElement.NAME_COMPARATOR);
  }

  /**
   * Returns a list of all packages defined in the object.
   * @return list of all packages.
   * @see #setPackages(List)
   */
  public List<MetaPackage> getPackages() {
    return packages;
  }

  /**
   * Sets the list of packages of the model.
   * @param packages new list of packages.
   * @see #getPackages()
   */
  public void setPackages(List<MetaPackage> packages) {
    removeAllPackages();
    this.packages.addAll(packages);
    for (MetaPackage metaPackage : packages) {
      metaPackage.setContext(this);
    }
  }

  /**
   * Remove all packages of the model.
   */
  public void removeAllPackages() {
    for (MetaPackage metaPackage : packages) {
      metaPackage.setContext(null);
    }
    packages.clear();
  }

  /**
   * Removes a package from the model.
   * @param packages to be removed.
   */
  public void removePackage(MetaPackage packages) {
    if (this.packages.contains(packages)) {
      this.packages.remove(packages);
      packages.setContext(null);
    }
  }

  /**
   * Returns the datatype with the given index.
   * @param index index of the requested datatype.
   * @return the requested datatype.
   * @see #addDatatype(int, MetaDatatype)
   */
  public MetaDatatype getDatatype(int index) {
    return datatypes.get(index);
  }

  /**
   * Adds the datatype in the list of datatypes.
   * @param datatype datatype to add.
   * @pre datatype != null
   */
  public void addDatatype(MetaDatatype datatype) {
    assert datatype != null;
    datatypes.add(datatype);
    datatype.setContext(this);
  }

  /**
   * Adds the datatype in the list of datatypes.
   * @param index position where the datatype should be inserted in the list.
   * @param datatype datatype to add
   * @pre datatype != null
   * @see #getDatatype(int)
   */
  public void addDatatype(int index, MetaDatatype datatype) {
    datatypes.add(index, datatype);
    datatype.setContext(this);
    Collections.sort(datatypes, MetaElement.NAME_COMPARATOR);
  }

  /**
   * Returns a list of all datatypes defined in the object.
   * @return list of all datatypes.
   * @see #setDatatypes(List)
   */
  public List<MetaDatatype> getDatatypes() {
    return datatypes;
  }

  /**
   * Sets the list of dataytpes of the model.
   * @param datatypes new list of datatypes.
   * @see #getDatatypes()
   */
  public void setDatatypes(List<MetaDatatype> datatypes) {
    removeAllDatatypes();
    this.datatypes.addAll(datatypes);
    for (MetaDatatype datatype : datatypes) {
      datatype.setContext(this);
    }
  }

  /**
   * Remove all datatypes of the model.
   */
  public void removeAllDatatypes() {
    for (MetaDatatype datatype : datatypes) {
      datatype.setContext(null);
    }
    datatypes.clear();
  }

  /**
   * Removes a datatype from the model.
   * @param datatype to be removed.
   */
  public void removeDatatype(MetaDatatype datatype) {
    if (datatypes.contains(datatype)) {
      datatypes.remove(datatype);
      datatype.setContext(null);
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
    for (MetaDatatype datatype : getDatatypes()) {
      datatype.accept(visitor);
    }
  }

  // %% BEGIN USER CODE

  /**
   * Finds the package with the given qualified name.
   * @param qualifiedName the qualified name of the package to find.
   * @return the requested package if found otherwise null.
   */
  public MetaPackage findPackage(String qualifiedName) {
    return (MetaPackage) findElement(packages, qualifiedName);
  }

  /**
   * Finds the datatype with the given name.
   * @param name the qualified name of the datatype to find.
   * @return the requested datatype if found otherwise null.
   */
  public MetaDatatype findDatatype(String name) {
    return (MetaDatatype) findElement(datatypes, name);
  }

  /**
   * Finds the class with the given qualified name. If no package is specified
   * then directly in the model is searched. Nested classes are also found. The
   * qualified name is e.g. package1.package2.class.nestedClass1
   * @param qualifiedName qualified name of the class to find.
   * @return the requested class if found otherwise null.
   */
  public MetaClass findClass(String qualifiedName) {
    assert (qualifiedName != null) && (!qualifiedName.equals(""))
        && (!qualifiedName.endsWith("."));
    // if there is no package specified look within the classes of the meta
    // model
    if (qualifiedName.indexOf(".") == -1)
      return super.findClass(qualifiedName);
    // find best matching package
    MetaPackage currentPackage = null;
    String lastIdentifierMatched = "";
    for (MetaPackage metaPackage : packages) {
      if (qualifiedName.startsWith(metaPackage.getName())) {
        if (metaPackage.getName().length() > lastIdentifierMatched.length()) {
          currentPackage = metaPackage;
          lastIdentifierMatched = metaPackage.getName();
        }
      }
    }
    if ((currentPackage == null)
        || (currentPackage.getName().equals(qualifiedName))) {
      return null;
    }
    // find class or nested class
    qualifiedName = qualifiedName.substring(lastIdentifierMatched.length() + 1);
    StringTokenizer tokenizer = new StringTokenizer(qualifiedName, ".");
    MetaClass metaClass = currentPackage.findClass(tokenizer.nextToken());
    if (metaClass == null) {
      return null;
    }
    while (tokenizer.hasMoreTokens()) {
      String name = tokenizer.nextToken();
      metaClass = (MetaClass) metaClass.findElement(metaClass
          .getNestedClasses(), name);
      if (metaClass == null) {
        return null;
      }
    }
    return metaClass;
  }

  /**
   * finds the classifier with the given qualified name.
   * @param model model containing the requested classifier.
   * @param qualifiedName the qualified name of the requested classifier.
   * @return the requested classifier if found otherwise null
   * @pre (model != null) && (qualifiedName != null)
   */
  public static MetaClassifier findClassifier(MetaModel model,
      String qualifiedName) {
    assert (model != null) && (qualifiedName != null);
    MetaClassifier classifier = model.findDatatype(qualifiedName);
    if (classifier == null) {
      classifier = model.findClass(qualifiedName);
    }
    return classifier;
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