/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.*;

import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.model_management.MPackage;

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

  public MetaDatatype getDatatype(int index) {
    return datatypes.get(index);
  }

  public void addDatatype(MetaDatatype datatype) {
    datatypes.add(datatype);
    datatype.setContext(this);
  }

  public void addDatatype(int index, MetaDatatype datatype) {
    datatypes.add(index, datatype);
    datatype.setContext(this);
    Collections.sort(packages, MetaElement.NAME_COMPARATOR);
  }

  public List<MetaDatatype> getDatatypes() {
    return datatypes;
  }

  public void setDatatypes(List<MetaDatatype> dataTypes) {
    removeAllDatatypes();
    dataTypes.addAll(dataTypes);
    for (MetaDatatype dataType : dataTypes) {
      dataType.setContext(this);
    }
  }

  public void removeAllDatatypes() {
    for (MetaDatatype dataType : datatypes) {
      dataType.setContext(null);
    }
    datatypes.clear();
  }

  public void removeDatatype(MetaDatatype dataType) {
    if (datatypes.contains(dataType)) {
      datatypes.remove(dataType);
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
   * Finds the datatype with the given qualified name.
   * @param qualifiedName the qualified name of the datatype to find.
   * @return the requested datatype if found otherwise null.
   */
  public MetaDatatype findDatatype(String qualifiedName) {
    return (MetaDatatype) findElement(datatypes, qualifiedName);
  }

  /**
   * Finds the class with the given qualified name. If no package is specified then directly in the model is searched.
   * Nested classes are also found. The qualified name is e.g. package1.package2.class.nestedClass1
   * @param qualifiedName qualified name of the class to find.
   * @return the requested class if found otherwise null.
   *
   */
  public MetaClass findClass(String qualifiedName) {
    assert(qualifiedName != null) && (!qualifiedName.equals("")) && (!qualifiedName.endsWith("."));
    
    // if there is no package specified look within the classes of the meta model
    if(qualifiedName.indexOf(".") == -1) return super.findClass(qualifiedName);
    
    // find best matching packet
    MetaPackage currentPackage = null;
    String lastIdentifierMatched = "";
    for(MetaPackage metaPackage : packages) {
      if(qualifiedName.startsWith(metaPackage.getName())) {
        if(metaPackage.getName().length() > lastIdentifierMatched.length()) {
          currentPackage = metaPackage;
          lastIdentifierMatched = metaPackage.getName();
        }
      }
    }
    if(currentPackage == null) return null;
    if(currentPackage.getName().equals(qualifiedName)) return null;

    // find class or nested class
    qualifiedName = qualifiedName.substring(lastIdentifierMatched.length()+1);
    StringTokenizer tokenizer = new StringTokenizer(qualifiedName, ".");
    MetaClass metaClass = currentPackage.findClass(tokenizer.nextToken());
    if(metaClass == null) return null;
    
    while (tokenizer.hasMoreTokens()) {
      String name = tokenizer.nextToken();
      metaClass = (MetaClass)metaClass.findElement(metaClass.getNestedClasses(), name);
      if(metaClass == null) return null;
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