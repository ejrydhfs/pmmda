/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.reference;

import java.util.Iterator;
import java.util.List;

/**
 * The reference code information defines the capabilities of a reference code
 * type.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ReferenceCodeInfo {
  /**
   * The qualified class name of the reference code.
   */
  private String classname;

  /**
   * Version of the reference code.
   */
  private String version;

  /**
   * Flag indicating if the reference code type is hierarchical or not.
   */
  private boolean hierarchical;

  /**
   * Flag indicating if the reference code class is available in the VM or not.
   */
  private boolean available;

  /**
   * The class of the reference code type.
   */
  private transient Class type;

  /**
   * List of all codes of the reference codes.
   */
  private List codes;

  /**
   * Creates a description of the reference code with the given class name.
   * @param classname qualified class name of the reference code type.
   * @return the description of the reference code.
   */
  public static ReferenceCodeInfo create(String classname) {
    assert (classname != null);

    ReferenceCodeInfo info = new ReferenceCodeInfo();
    info.setClassname(classname);
    info.setVersion(null);
    info.setHierarchical(false);
    info.setAvailable(true);
    try {
      Class clazz = Class.forName(classname);
      info.setType(clazz);

      Class[] interfaces = clazz.getInterfaces();
      for (int i = 0; i < interfaces.length; i++) {
        if (interfaces[i].getName().equals(classname)) {
          info.setHierarchical(true);
          break;
        }
      }
    } catch (ClassNotFoundException e) {
      info.setAvailable(false);
    }
    return info;
  }

  /**
   * Returns the descriptor of the reference code with the given classname.
   * @param infos list to search for.
   * @param classname name of the class of the requested code.
   * @return The descriptor of the requested code if found otherwise null.
   */
  public static ReferenceCodeInfo find(List infos, String classname) {
    assert ((infos != null) && (classname != null));

    ReferenceCodeInfo info = null;
    for (Iterator i = infos.iterator(); i.hasNext();) {
      info = (ReferenceCodeInfo) i.next();
      if (!classname.equals(info.getClassName())) {
        info = null;
      } else {
        break;
      }
    }
    return info;
  }

  /**
   * Returns the qualified class name of the described reference code type.
   * @return the qualified class name of the described reference code.
   */
  public String getClassName() {
    return classname;
  }

  /**
   * Sets the qualified class name of the described reference code type.
   * @param classname qualified classname of the described reference code.
   */
  public void setClassname(String classname) {
    this.classname = classname;
  }

  /**
   * Returns the version of the values of the reference code.
   * @return version of the reference code values.
   * @see #setVersion
   */
  public String getVersion() {
    return version;
  }

  /**
   * Sets the version of the values of the reference code.
   * @param version new version of the set of values.
   * @see #getVersion
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * Returns the flag indicating if the reference code is hierarchical.
   * @return true if the reference code is hierarchical otherwise false.
   * @see #setHierarchical
   */
  public boolean isHierarchical() {
    return hierarchical;
  }

  /**
   * Sets the hierarchical flag indicating if the code is hierarchical.
   * @param hierarchical new value of the flag.
   * @see #isHierarchical
   */
  public void setHierarchical(boolean hierarchical) {
    this.hierarchical = hierarchical;
  }

  /**
   * Returns the flag indicating if the reference code values are available.
   * @return true if the reference code is available otherwise false.
   * @see #setAvailable
   */
  public boolean isAvailable() {
    return available;
  }

  /**
   * Sets the flag indicating if the reference code values are available.
   * @param available new value of the flag.
   * @see #isAvailable
   */
  public void setAvailable(boolean available) {
    this.available = available;
  }

  /**
   * Returns the type of the reference code.
   * @return the class implementing the reference code.
   * @see #setType
   */
  public Class getType() {
    return type;
  }

  /**
   * Sets the type of the reference code.
   * @param type the new type of the reference code.
   * @see #getType
   */
  public void setType(Class type) {
    this.type = type;
  }

  /**
   * Returns the code values of the reference code.
   * @return the values of the reference code.
   * @see #setCodes
   */
  public List getCodes() {
    return codes;
  }

  /**
   * Sets the code values of the reference code.
   * @param codes code values of the reference code.
   * @see #getCodes
   */
  public void setCodes(List codes) {
    this.codes = codes;
  }
}