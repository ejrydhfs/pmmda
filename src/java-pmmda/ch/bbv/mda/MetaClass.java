/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import java.util.ArrayList;
import java.util.List;

/**
 * The meta class models a package object as defined in the UML standard. It
 * provides additional services to retrieve tagged values associated with it.
 * These values are used to map the independent application model to the
 * platform specific architecture. The following tagged values are defined.
 * <ul>
 * <dt>shouldBeGenerated</dt>
 * <dd>Boolean flag indicating if the package should be mapped to the platform
 * or not. The default value is false.</dd>
 * </ul>
 * <p>
 * The meta class contains properties, indexed properties and reference codes.
 * </p>
 * <ul>
 * <li>Properties are standard attributes with their accessor methods.</li>
 * <li>Keys are properties which value is defined in a seperate code of a
 * specific type. A code is identified by its class and the key value. The key
 * value is an integer wrapper instance. Null values are supported.</li>
 * <li>Indexed properties are multiple properties which values are either build
 * in types or data types. In other words a indexed properties is a one to many
 * relation to another type. No null values are needed.</li>
 * <li>Reference are properties which value is another data type. In other
 * words a reference is a one to one relation to another data layer class. The
 * instance is identified by its class and the forward key value. The value is
 * the object identifier of the instance and should always be an integer. Null
 * values are supported.</li>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MetaClass extends MetaClassifier {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Number of properties being used as primary keys in the persistent
   * representation of the class.
   */
  private transient int nrOfPrimaryProperties;

  /**
   * Flag indicating if the element has the associated property.
   */
  private boolean interfaces;

  /**
   * Flag indicating if the element has the associated property.
   */
  private boolean abstracts;

  /**
   * Flag indicating if the element has the associated property.
   */
  private boolean finals;

  /**
   * Flag indicating if the element has the associated property.
   */
  private boolean statics;

  /**
   * List of ancestors of the class. An ancestor is either a class or an
   * interface.
   */
  private List<MetaClass> ancestors;

  /**
   * List of simple properties of the class.
   */
  private List<MetaProperty> properties;

  /**
   * List of indexed propreties of the class.
   */
  private List<MetaIndexedProperty> indexedProperties;

  /**
   * List of nested classes of the class.
   */
  private List<MetaClass> nestedClasses;

  /**
   * Default constructor of the class.
   */
  public MetaClass() {
    ancestors = new ArrayList<MetaClass>();
    properties = new ArrayList<MetaProperty>();
    indexedProperties = new ArrayList<MetaIndexedProperty>();
    nestedClasses = new ArrayList<MetaClass>();
  }

  /**
   * Returns true if the class is an interface.
   * @return interface flag value.
   * @see #setInterface(boolean)
   */
  public boolean isInterface() {
    return interfaces;
  }

  /**
   * Sets the system flag indicating if the class is an interface.
   * @param interfaces new value of the flag.
   * @see #isInterface()
   */
  public void setInterface(boolean interfaces) {
    this.interfaces = interfaces;
  }

  /**
   * Returns true if the class is abstract.
   * @return abstract flag value.
   * @see #setAbstract(boolean)
   */
  public boolean isAbstract() {
    return abstracts;
  }

  /**
   * Sets the abstract flag indicating if the class is abstract.
   * @param abstracts new value of the flag.
   * @see #isAbstract()
   */
  public void setAbstract(boolean abstracts) {
    this.abstracts = abstracts;
  }

  /**
   * Returns true if the class is static.
   * @return static flag value.
   * @see #setStatic(boolean)
   */
  public boolean isStatic() {
    return statics;
  }

  /**
   * Sets the abstract flag indicating if the class is static. Only nested
   * classes can be static.
   * @param statics new value of the flag.
   * @see #isStatic()
   */
  public void setStatic(boolean statics) {
    this.statics = statics;
  }

  /**
   * Returns true if the class is final.
   * @return final flag value.
   * @see #setFinal(boolean)
   */
  public boolean isFinal() {
    return finals;
  }

  /**
   * Sets the final flag indicating if the class is final.
   * @param finals new value of the flag.
   * @see #isFinal()
   */
  public void setFinal(boolean finals) {
    this.finals = finals;
  }

  /**
   * Adds an ancestor to the class.
   * @param ancestor ancestor to add.
   */
  public void addAncestor(MetaClass ancestor) {
    ancestors.add(ancestor);
  }

  /**
   * Gets the property located at the given index.
   * @param index index of the property to retrieve.
   * @return the requested property.
   * @see #addProperty(int, MetaProperty)
   */
  public MetaProperty getProperty(int index) {
    return properties.get(index);
  }

  /**
   * Adds a property to the list of property.
   * @param property property to add.
   */
  public void addProperty(MetaProperty property) {
    properties.add(property);
    property.setContext(this);
  }

  /**
   * Adds a property to the list of property.
   * @param index index of the property to add.
   * @param property property to add.
   */
  public void addProperty(int index, MetaProperty property) {
    properties.add(index, property);
    property.setContext(this);
  }

  /**
   * Returns all properties.
   * @return list of properties.
   * @see #setProperties(List)
   */
  public List<MetaProperty> getProperties() {
    return properties;
  }

  /**
   * Sets the list of properties.
   * @param properties new list of properties.
   * @see #getProperties()
   */
  public void setProperties(List<MetaProperty> properties) {
    removeAllProperties();
    properties.addAll(properties);
    for (MetaProperty property : properties) {
      property.setContext(this);
    }
  }

  /**
   * Removes all properties.
   */
  public void removeAllProperties() {
    for (MetaProperty property : properties) {
      property.setContext(null);
    }
    properties.clear();
  }

  /**
   * Removes a property from this class.
   * @param property to be removed.
   */
  public void removeProperty(MetaProperty property) {
    if (properties.contains(property)) {
      property.setContext(null);
      properties.remove(property);
    }
  }

  /**
   * Gets the property located at the given index.
   * @param index index of the property to retrieve.
   * @return the requested property.
   * @see #addIndexedProperty(int, MetaIndexedProperty)
   */
  public MetaIndexedProperty getIndexedProperty(int index) {
    return indexedProperties.get(index);
  }

  /**
   * Adds a property to the list of property.
   * @param property property to add.
   */
  public void addIndexedProperty(MetaIndexedProperty property) {
    indexedProperties.add(property);
    property.setContext(this);
  }

  /**
   * Adds a property to the list of property.
   * @param index index of the property to add.
   * @param property property to add.
   */
  public void addIndexedProperty(int index, MetaIndexedProperty property) {
    indexedProperties.add(index, property);
    property.setContext(this);
  }

  /**
   * Returns all properties.
   * @return list of properties.
   * @see #setIndexedProperties(List)
   */
  public List<MetaIndexedProperty> getIndexedProperties() {
    return indexedProperties;
  }

  /**
   * Sets the list of properties.
   * @param properties new list of properties.
   * @see #getIndexedProperties()
   */
  public void setIndexedProperties(List<MetaIndexedProperty> properties) {
    removeAllIndexedProperties();
    this.indexedProperties.addAll(properties);
    for (MetaIndexedProperty property : properties) {
      property.setContext(this);
    }
  }

  /**
   * Removes all properties.
   */
  public void removeAllIndexedProperties() {
    for (MetaIndexedProperty property : indexedProperties) {
      property.setContext(null);
    }
    indexedProperties.clear();
  }

  /**
   * Removes an indexed property from this class.
   * @param property to be removed.
   */
  public void removeIndexedProperty(MetaIndexedProperty property) {
    if (indexedProperties.contains(property)) {
      indexedProperties.remove(property);
      property.setContext(null);
    }
  }

  /**
   * Returns the nested class with the given index.
   * @param index index of the requested nested class.
   * @return the requested nested class.
   */
  public MetaClass getNestedClass(int index) {
    return nestedClasses.get(index);
  }

  /**
   * Adds the nested class.
   * @param clazz nested class to add.
   */
  public void addNestedClass(MetaClass clazz) {
    nestedClasses.add(clazz);
    clazz.setContext(this);
  }

  /**
   * Adds a nested class.
   * @param index index of the class to add.
   * @param clazz the nested class to add.
   */
  public void addNestedClass(int index, MetaClass clazz) {
    nestedClasses.add(index, clazz);
    clazz.setContext(this);
  }

  /**
   * Returns the list of nested classes.
   * @return the list of nested classes.
   * @see #setNestedClasses(List)
   */
  public List<MetaClass> getNestedClasses() {
    return nestedClasses;
  }

  /**
   * Sets the list of nested classes. The previous set of nested classes is
   * discarded.
   * @param classes list of nested classes to add.
   * @see #getNestedClasses()
   */
  public void setNestedClasses(List<MetaClass> classes) {
    removeAllNestedClasses();
    classes.addAll(classes);
    for (MetaClass clazz : classes) {
      clazz.setContext(this);
    }
  }

  /**
   * Removes all nested classes.
   */
  public void removeAllNestedClasses() {
    for (MetaClass clazz : nestedClasses) {
      clazz.setContext(null);
    }
    nestedClasses.clear();
  }

  /**
   * Removes a nested class from this class.
   * @param metaClass to be removed.
   */
  public void removeNestedClass(MetaClass metaClass) {
    nestedClasses.remove(metaClass);
  }

  /**
   * Returns the number of properties being primary keys.
   * @return number of primary properties in the class.
   * @see #setNrOfPrimaryProperties(int)
   */
  public int getNrOfPrimaryProperties() {
    return nrOfPrimaryProperties;
  }

  /**
   * Returns the property with the given name if found otherwise null.
   * @param name name of the property to find.
   * @return the requested property if found otherwise null.
   */
  public MetaProperty findProperty(String name) {
    return (MetaProperty) findElement(properties, name);
  }

  /**
   * Returns the property with the given name if found otherwise null.
   * @param name name of the property to find.
   * @return the requested property if found otherwise null.
   */
  public MetaIndexedProperty findIndexedProperty(String name) {
    return (MetaIndexedProperty) findElement(indexedProperties, name);
  }

  /**
   * Sets the number of properties being primary keys.
   * @param nrOfPrimaryProperties new number of primary properties.
   * @see #getNrOfPrimaryProperties()
   */
  public void setNrOfPrimaryProperties(int nrOfPrimaryProperties) {
    this.nrOfPrimaryProperties = nrOfPrimaryProperties;
  }

  /**
   * Returns the package name of the class.
   * @return package name of the class.
   */
  public String packageName() {
    return getContext().getName();
  }

  /**
   * Implements the visitor pattern.
   * @param visitor visitor to process.
   */
  public void accept(Visitor visitor) {
    visitor.visit(this);

    for (MetaProperty item : properties) {
      item.accept(visitor);
    }
    for (MetaIndexedProperty item : indexedProperties) {
      item.accept(visitor);
    }
    for (MetaClass item : nestedClasses) {
      item.accept(visitor);
    }
  }

  /**
   * Returns the package containing the class.
   * @return the package owning the class.
   */
  public MetaPackage retrievePackage() {
    if(this.isNestedClass()) return ((MetaClass)getContext()).retrievePackage();
    return (MetaPackage) getContext();
  }

  /**
   * Retrieves all the class extended in this class. In Java the list can
   * contains at most one item.
   * @return list of all extended implemented in the class.
   */
  public List<MetaClass> retrieveAncestors() {
    List<MetaClass> list = new ArrayList<MetaClass>();
    for (MetaClass clazz : ancestors) {
      if (!clazz.isInterface()) {
        list.add(clazz);
      }
    }
    return list;
  }

  /**
   * Retrieves all the interfaces implemented in this class.
   * @return list of all interfaces implemented in the class.
   */
  public List<MetaClass> retrieveInterfaces() {
    List<MetaClass> list = new ArrayList<MetaClass>();
    for (MetaClass clazz : ancestors) {
      if (clazz.isInterface()) {
        list.add(clazz);
      }
    }
    return list;
  }

  /**
   * Returns true if the class or one of its ancestors has an ancestor with the
   * given qualified name.
   * @param qualifiedName qualified name of the ancestor.
   * @return true if one ancestor has the requested name otherwise false.
   */
  public boolean hasAncestor(String qualifiedName) {
    assert qualifiedName != null;
    MetaClass ancestor = getModel().findClass(qualifiedName);
    boolean hasAncestor = false;
    if (ancestor != null) {
      hasAncestor = hasAncestor(ancestor);
    }
    return hasAncestor;
  }

  /**
   * Returns true if the class or one of its ancestors has the given class as
   * ancestor.
   * @param clazz ancestor class to infer.
   * @return true if the given class is an ancestor otherwise false.
   */
  public boolean hasAncestor(MetaClass clazz) {
    assert clazz != null;
    List<MetaClass> ancestors = retrieveAncestors();
    boolean hasAncestor = ancestors.contains(clazz);
    if (!hasAncestor) {
      for (MetaClass ancestor : ancestors) {
        hasAncestor = ancestor.hasAncestor(clazz);
        if (hasAncestor) {
          break;
        }
      }
    }
    return hasAncestor;
  }

  /**
   * Returns true if the class or one of its ancestors has an interface with the
   * given qualified name.
   * @param qualifiedName qualified name of the ancestor.
   * @return true if one ancestor has the requested name otherwise false.
   */
  public boolean hasInterface(String qualifiedName) {
    assert qualifiedName != null;
    MetaModel model = (MetaModel) getRoot();
    MetaClass ancestor = model.findClass(qualifiedName);
    boolean hasInterface = false;
    if (ancestor != null) {
      hasInterface = hasInterface(ancestor);
    }
    return hasInterface;
  }

  /**
   * Returns true if the class or one of its ancestors has the given class as
   * ancestor.
   * @param clazz ancestor class to infer.
   * @return true if the given class is an ancestor otherwise false.
   */
  public boolean hasInterface(MetaClass clazz) {
    assert clazz != null;
    List<MetaClass> interfaces = retrieveInterfaces();
    boolean hasInterface = interfaces.contains(clazz);
    if (!hasInterface) {
      for (MetaClass ancestor : interfaces) {
        hasInterface = ancestor.hasInterface(clazz);
        if (hasInterface) {
          break;
        }
      }
    }
    return hasInterface;
  }

  /**
   * Returns true if context is of type class, false if not (package).
   * @return true if context is MetaClass
   */
  public boolean isNestedClass() {
    return getContext() instanceof MetaClass;
  }
}