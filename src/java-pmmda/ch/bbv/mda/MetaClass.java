/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.bbv.utilities.TextUtilities;

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
   * Number of properties being used as primary keys in the persistent
   * representation of the class.
   */
  private transient int nrOfPrimaryProperties;

  private boolean interfaces;

  private boolean abstracts;

  private boolean finals;

  private boolean statics;

  private List ancestors;

  private List properties;

  private List indexedProperties;

  private List imports;

  private List declarations;

  private List constructors;

  private List definitions;

  /**
   * List of nested classes of the class.
   */
  private List classes;

  private boolean system;

  /**
   * Default constructor of the class.
   */
  public MetaClass() {
    init();
  }

  public void init() {
    super.init();
    if (ancestors == null)
      ancestors = new ArrayList();
    if (properties == null)
      properties = new ArrayList();
    if (indexedProperties == null)
      indexedProperties = new ArrayList();
    if (classes == null)
      classes = new ArrayList();
  }

  /**
   * Returns the list of user defined import statements.
   * @return List of user defined import statements.
   * @see #setImports
   */
  public List getImports() {
    return imports;
  }

  /**
   * Sets the list of user defined import statements.
   * @param imports new list of user defined import statements.
   * @see #getImports
   */
  public void setImports(List imports) {
    this.imports = imports;
  }

  /**
   * Returns the list of user defined declarations.
   * @return List of user defined declarations.
   * @see #setDeclarations
   */
  public List getDeclarations() {
    return declarations;
  }

  /**
   * Sets the list of user defined declarations.
   * @param declarations new list of user defined declarations.
   * @see #getDeclarations
   */
  public void setDeclarations(List declarations) {
    this.declarations = declarations;
  }

  /**
   * Returns the constructors of the class.
   * @return the constructors of the class.
   * @see #setConstructors(List)
   */
  public List getConstructors() {
    return constructors;
  }

  /**
   * Sets the constructors of the class.
   * @param constructors the new list of constructors of the class.
   * @see #getConstructors()
   */
  public void setConstructors(List constructors) {
    this.constructors = constructors;
  }

  /**
   * Returns the definitions of the class.
   * @return the definitions of the class.
   * @see #setDefinitions(List)
   */
  public List getDefinitions() {
    return definitions;
  }

  /**
   * Sets the definitions of the class.
   * @param definitions hte new definitions of the class.
   * @see #getDefinitions()
   */
  public void setDefinitions(List definitions) {
    this.definitions = definitions;
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
    return (MetaProperty) properties.get(index);
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

  public List getProperties() {
    return properties;
  }

  public void setProperties(List properties) {
    removeAllProperties();
    properties.addAll(properties);

    for (Iterator i = properties.iterator(); i.hasNext();) {
      ((MetaProperty) i.next()).setContext(this);
    }
  }

  public void removeAllProperties() {
    for (Iterator i = properties.iterator(); i.hasNext();) {
      ((MetaProperty) i.next()).setContext(null);
    }
    properties.clear();
  }

  public MetaIndexedProperty getIndexedProperty(int index) {
    return (MetaIndexedProperty) indexedProperties.get(index);
  }

  public void addIndexedProperty(MetaIndexedProperty metaIndexedProperty) {
    indexedProperties.add(metaIndexedProperty);
    metaIndexedProperty.setContext(this);
  }

  public void addIndexedProperty(int index,
      MetaIndexedProperty metaIndexedProperty) {
    indexedProperties.add(index, metaIndexedProperty);
    metaIndexedProperty.setContext(this);
  }

  public List getIndexedProperties() {
    return indexedProperties;
  }

  public void setIndexedProperties(List indexedProperties) {
    removeAllIndexedProperties();
    this.indexedProperties.addAll(indexedProperties);

    for (Iterator i = properties.iterator(); i.hasNext();) {
      ((MetaIndexedProperty) i.next()).setContext(this);
    }
  }

  public void removeAllIndexedProperties() {
    for (Iterator i = indexedProperties.iterator(); i.hasNext();) {
      ((MetaIndexedProperty) i.next()).setContext(null);
    }
    indexedProperties.clear();
  }

  public MetaClass getClass(int index) {
    return (MetaClass) classes.get(index);
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

  public void setClasses(List classes) {
    removeAllClasses();
    classes.addAll(classes);

    Iterator iterator = classes.iterator();
    while (iterator.hasNext()) {
      ((MetaClass) iterator.next()).setContext(this);
    }
  }

  public void removeAllClasses() {
    Iterator iterator = classes.iterator();
    while (iterator.hasNext()) {
      ((MetaClass) iterator.next()).setContext(null);
    }
    classes.clear();
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

  public List retrieveAncestors() {
    List list = new ArrayList();
    for (Iterator i = ancestors.iterator(); i.hasNext();) {
      MetaClass mClass = (MetaClass) i.next();
      if (!mClass.isInterface()) {
        list.add(mClass);
      }
    }
    return list;
  }

  public List retrieveInterfaces() {
    List list = new ArrayList();
    for (Iterator i = ancestors.iterator(); i.hasNext();) {
      MetaClass mClass = (MetaClass) i.next();
      if (mClass.isInterface()) {
        list.add(mClass);
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
    assert (qualifiedName != null);

    List ancestors = retrieveAncestors();
    boolean hasAncestor = MdaUtilities.containsElementNamed(ancestors,
        qualifiedName);
    if (!hasAncestor) {
      for (Iterator i = ancestors.iterator(); i.hasNext() && !hasAncestor;) {
        hasAncestor = ((MetaClass) i.next()).hasAncestor(qualifiedName);
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
    assert (qualifiedName != null);

    List interfaces = retrieveInterfaces();
    boolean hasInterface = MdaUtilities.containsElementNamed(interfaces,
        qualifiedName);
    if (!hasInterface) {
      Iterator it = interfaces.iterator();
      while (it.hasNext() && !hasInterface) {
        hasInterface = ((MetaClass) it.next()).hasInterface(qualifiedName);
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
    return TextUtilities.getContextName(qualifiedClassname);
  }

  /**
   * Returns the class name of a qualified class name.
   * @param qualifiedClassname qualified class name to process.
   * @return the simple class name.
   */
  public static String getClassName(String qualifiedClassname) {
    return TextUtilities.getComponentName(qualifiedClassname);
  }

  public boolean isLeaf() {
    if (properties != null && properties.isEmpty() && indexedProperties != null
        && indexedProperties.isEmpty() && classes != null & classes.isEmpty())
      return true;
    return false;
  }
}