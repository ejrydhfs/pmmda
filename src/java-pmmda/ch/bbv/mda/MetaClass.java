/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.bbv.mda.operators.ModelOperators;

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
public class MetaClass extends MetaElement {
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
   * Flag indicating if the element is a system element defined in the
   * environment.
   */
  private boolean system;

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
  private List<MetaClass> classes;

  /**
   * Default constructor of the class.
   */
  public MetaClass() {
    ancestors = new ArrayList<MetaClass>();
    properties = new ArrayList<MetaProperty>();
    indexedProperties = new ArrayList<MetaIndexedProperty>();
    classes = new ArrayList<MetaClass>();
  }

  /**
   * Returns true if the class is a system class.
   * @return system flag value.
   * @see #setSystem(boolean)
   */
  public boolean isSystem() {
    return system;
  }

  /**
   * Sets the system flag indicating if the class is as system class.
   * @param system new value of the flag.
   * @see #isSystem()
   */
  public void setSystem(boolean system) {
    this.system = system;
  }

  /**
   * Returns true if the class is an interface.
   * @return interface flag value.
   * @see #setInterface
   */
  public boolean isInterface() {
    return interfaces;
  }

  /**
   * Sets the system flag indicating if the class is an interface.
   * @param interfaces new value of the flag.
   * @see #isInterface
   */
  public void setInterface(boolean interfaces) {
    this.interfaces = interfaces;
  }

  /**
   * Returns true if the class is abstract.
   * @return abstract flag value.
   * @see #setAbstract
   */
  public boolean isAbstract() {
    return abstracts;
  }

  /**
   * Sets the abstract flag indicating if the class is abstract.
   * @param abstracts new value of the flag.
   * @see #isAbstract
   */
  public void setAbstract(boolean abstracts) {
    this.abstracts = abstracts;
  }

  /**
   * Returns true if the class is static.
   * @return static flag value.
   * @see #setStatic
   */
  public boolean isStatic() {
    return statics;
  }

  /**
   * Sets the abstract flag indicating if the class is static. Only nested
   * classes can be static.
   * @param statics new value of the flag.
   * @see #isStatic
   */
  public void setStatic(boolean statics) {
    this.statics = statics;
  }

  /**
   * Returns true if the class is final.
   * @return final flag value.
   * @see #setFinal
   */
  public boolean isFinal() {
    return finals;
  }

  /**
   * Sets the final flag indicating if the class is final.
   * @param finals new value of the flag.
   * @see #isFinal
   */
  public void setFinal(boolean finals) {
    this.finals = finals;
  }

  public MetaProperty getProperty(int index) {
    return properties.get(index);
  }

  public void addProperty(MetaProperty metaProperty) {
    properties.add(metaProperty);
    metaProperty.setContext(this);
  }

  public void addAncestor(MetaClass metaClass) {
    ancestors.add(metaClass);
  }

  public void addProperty(int index, MetaProperty metaProperty) {
    properties.add(index, metaProperty);
    metaProperty.setContext(this);
  }

  public List<MetaProperty> getProperties() {
    return properties;
  }

  public void setProperties(List<MetaProperty> properties) {
    removeAllProperties();
    properties.addAll(properties);
    for (MetaProperty property : properties) {
      property.setContext(this);
    }
  }

  public void removeAllProperties() {
    for (MetaProperty property : properties) {
      property.setContext(null);
    }
    properties.clear();
  }

  /**
   * Removes a property from this class
   * @param metaProperty to be removed.
   */
  public void removeProperty(MetaProperty metaProperty) {
    properties.remove(metaProperty);
  }

  public MetaIndexedProperty getIndexedProperty(int index) {
    return indexedProperties.get(index);
  }

  public void addIndexedProperty(MetaIndexedProperty metaIndexedProperty) {
    indexedProperties.add(metaIndexedProperty);
    metaIndexedProperty.setContext(this);
  }

  public void addIndexedProperty(int index, MetaIndexedProperty property) {
    indexedProperties.add(index, property);
    property.setContext(this);
  }

  public List<MetaIndexedProperty> getIndexedProperties() {
    return indexedProperties;
  }

  public void setIndexedProperties(List<MetaIndexedProperty> indexedProperties) {
    removeAllIndexedProperties();
    this.indexedProperties.addAll(indexedProperties);

    for (MetaIndexedProperty property : indexedProperties) {
      property.setContext(this);
    }
  }

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

  public MetaClass getClass(int index) {
    return classes.get(index);
  }

  public void addClass(MetaClass metaClass) {
    classes.add(metaClass);
    metaClass.setContext(this);
  }

  public void addClass(int index, MetaClass metaClass) {
    classes.add(index, metaClass);
    metaClass.setContext(this);
  }

  public List getClasses() {
    return classes;
  }

  public void setClasses(List<MetaClass> classes) {
    removeAllClasses();
    classes.addAll(classes);
    for (MetaClass clazz : classes) {
      clazz.setContext(this);
    }
  }

  public void removeAllClasses() {
    for (MetaClass clazz : classes) {
      clazz.setContext(null);
    }
    classes.clear();
  }

  /**
   * Removes a nested class from this class
   * @param metaClass to be removed.
   */
  public void removeClass(MetaClass metaClass) {
    classes.remove(metaClass);
  }

  /**
   * Returns the number of properties being primary keys.
   * @return number of primary properties in the class.
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

    for (Iterator i = properties.iterator(); i.hasNext();) {
      ((MetaProperty) i.next()).accept(visitor);
    }
    for (Iterator i = indexedProperties.iterator(); i.hasNext();) {
      ((MetaIndexedProperty) i.next()).accept(visitor);
    }
    for (Iterator i = classes.iterator(); i.hasNext();) {
      ((MetaClass) i.next()).accept(visitor);
    }
  }

  /**
   * Retunrs the package containing the class.
   * @return the package owning the class.
   */
  public MetaPackage retrievePackage() {
    return (MetaPackage) getContext();
  }

  public List<MetaClass> retrieveAncestors() {
    List<MetaClass> list = new ArrayList<MetaClass>();
    for (MetaClass clazz : ancestors) {
      if (!clazz.isInterface()) {
        list.add(clazz);
      }
    }
    return list;
  }

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
    MetaModel model = (MetaModel) getRoot();
    MetaClass ancestor = model.findClass(qualifiedName);
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
   * Returns true if context is of type class, false if not (package)
   * @return true if context is MetaClass
   */
  public boolean isNestedClass() {
    return getContext() instanceof MetaClass;
  }

  /**
   * Returns the package name of a qualified class name.
   * @param qualifiedClassname class name which package name is requested.
   * @return the package name.
   */
  public static String getPackageName(String qualifiedClassname) {
    return ModelOperators.getContextName(qualifiedClassname);
  }

  /**
   * Returns the class name of a qualified class name.
   * @param qualifiedClassname qualified class name to process.
   * @return the simple class name.
   */
  public static String getClassName(String qualifiedClassname) {
    return ModelOperators.getComponentName(qualifiedClassname);
  }
}