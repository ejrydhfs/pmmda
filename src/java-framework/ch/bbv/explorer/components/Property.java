/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.components;

/**
 * The class defines a Java property. A property has a name and a value.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class Property {
  /**
   * The name of the property.
   */
  private String name;

  /**
   * The value of the property.
   */
  private String value;

  /**
   * The default value of the property.
   */
  private String defaultValue;

  /**
   * Constructor of the class.
   * @param name the property name.
   * @param value the property value.
   */
  public Property(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the property name.
   * @return the property name.
   * @see #setName(String)
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the property.
   * @param name new name of the property.
   * @see #getName()
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the property value.
   * @return the property value.
   * @see #setValue(String)
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value of the property.
   * @param value new value of the property.
   * @see #getValue()
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns the property default value.
   * @return the property default value.
   * @see #setDefaultValue(String)
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * Sets the value of the property.
   * @param defaultValue new value of the property.
   * @see #getDefaultValue()
   */
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = value;
  }

}