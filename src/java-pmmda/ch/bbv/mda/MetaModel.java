/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.*;

/**
 * The meta project defines a MDA project composed of a set of packages. Overall
 * administrative properties are defined in the class. The class is a Java bean.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MetaModel extends MetaPackage {
  /**
   * The name of the file containing the meta model. The information is managed
   * within the application and is therefore transient.
   */
  private transient String filename;

  /**
   * List of all packages of the model.
   */
  private List packages;

  /**
   * List of all data types of the model.
   */
  private List dataTypes;

  /**
   * Default constructor of the class.
   */
  public MetaModel() {
    init();
  }

  /**
   * Initializes member variables. Only complex types have to be initialized
   * here. The init() method is called after deserializing and loading from xml
   * with XStream. Transient members have to be initialized with this init()
   * function.
   */
  public void init() {
    super.init();
    if (packages == null)
      packages = new ArrayList();
    if (dataTypes == null)
      dataTypes = new ArrayList();
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
    return (MetaPackage) packages.get(index);
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

  public List getPackages() {
    return packages;
  }

  public void setPackages(List packages) {
    removeAllPackages();
    packages.addAll(packages);

    for (Iterator i = packages.iterator(); i.hasNext();) {
      ((MetaPackage) i.next()).setContext(this);
    }
  }

  public void removeAllPackages() {
    for (Iterator i = packages.iterator(); i.hasNext();) {
      ((MetaPackage) i.next()).setContext(null);
    }
    packages.clear();
  }

  /**
   * Removes a package from the model
   * @param metaPackage to be removed.
   */
  public void removePackage(MetaPackage metaPackage){
    packages.remove(metaPackage);
  }
  
  public MetaDataType getDataType(int index) {
    return (MetaDataType) dataTypes.get(index);
  }

  public void addDataType(MetaDataType metaDataType) {
    dataTypes.add(metaDataType);
    metaDataType.setContext(this);
  }

  public void addDataType(int index, MetaDataType metaDataType) {
    dataTypes.add(index, metaDataType);
    metaDataType.setContext(this);
    Collections.sort(packages, MetaElement.NAME_COMPARATOR);
  }

  public List getDataTypes() {
    return dataTypes;
  }

  public void setDataTypes(List dataTypes) {
    removeAllDataTypes();
    dataTypes.addAll(dataTypes);

    for (Iterator i = dataTypes.iterator(); i.hasNext();) {
      ((MetaDataType) i.next()).setContext(this);
    }
  }

  public void removeAllDataTypes() {
    for (Iterator i = dataTypes.iterator(); i.hasNext();) {
      ((MetaDataType) i.next()).setContext(null);
    }
    dataTypes.clear();
  }
  
  public void removeDataType(MetaDataType metaDataType){
    dataTypes.remove(metaDataType);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
    for (Iterator i = packages.iterator(); i.hasNext();) {
      ((MetaPackage) i.next()).accept(visitor);
    }
    for (Iterator i = getClasses().iterator(); i.hasNext();) {
      ((MetaClass) i.next()).accept(visitor);
    }
  }

  /**
   * Indicates whether this model element is a leaf (no children)
   */
  public boolean isLeaf() {
    if (packages == null || packages.isEmpty())
      return true;
    return false;
  }

  // %% BEGIN USER CODE
  public MetaPackage findPackage(String qualifiedName) {
    return (MetaPackage) findElement(packages, qualifiedName);
  }

  public MetaDataType findDataType(String qualifiedName) {
    return (MetaDataType) findElement(dataTypes, qualifiedName);
  }

  public MetaClass findClass(String qualifiedName) {
    MetaClass metaClass = null;
    if (qualifiedName != null) {
	    String packageName = MetaClass.getPackageName(qualifiedName);
	    if (packageName != null) {
	      MetaPackage metaPackage = findPackage(packageName);
	      if (metaPackage != null) {
	        metaClass = metaPackage
	            .findClass(MetaClass.getClassName(qualifiedName));
	      }
	    }
    }
    return metaClass;
  }

  /**
   * Returns the list of meta classes referencing the given class.
   * @param clazz class which references are looked for.
   * @return the list of classes referencing the given class.
   */
  public Collection getClassReferences(MetaClass clazz) {
    assert (clazz != null);
    VisitorCollectClassReferences visitor = new VisitorCollectClassReferences();
    visitor.setReferencedClass(clazz);
    accept(visitor);
    return visitor.getReferences();
  }

  // %% END USER CODE
}