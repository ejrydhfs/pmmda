/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog;

import java.io.Serializable;

/**
 * The data property update identifies the modification of one property in one
 * data object.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class DataPropertyUpdate implements Serializable {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Updated property.
   */
  private String property;

  /**
   * Index of the property if relevant otherwise null.
   */
  private Integer index;

  /**
   * New value of the property.
   */
  private String value;

  /**
   * Default constructor of the class.
   */
  public DataPropertyUpdate() {
    // default constructor
  }

  /**
   * Constructor of the class.
   * @param property name of the updated property.
   * @param index index of the update if the property is indexed otherwisen ull.
   * @param value new value of the property.
   * @pre property != null
   */
  public DataPropertyUpdate(String property, Integer index, String value) {
    this.property = property;
    this.index = index;
    this.value = value;
  }

  /**
   * Constructor of the class.
   * @param property name of the updated property.
   * @param value new value of the property.
   * @pre property != null
   */
  public DataPropertyUpdate(String property, String value) {
    this(property, null, value);
  }

  /**
   * Returns the name of the property.
   * @return name of the property.
   * @see #setProperty(String)
   */
  public String getProperty() {
    return property;
  }

  /**
   * Sets the name of the property.
   * @param property name of the property.
   * @see #getProperty()
   */
  public void setProperty(String property) {
    this.property = property;
  }

  /**
   * Returns the value of the property.
   * @return index of the property.
   * @see #setIndex(Integer)
   */
  public Integer getIndex() {
    return index;
  }

  /**
   * Sets the value of the property.
   * @param index index of the property.
   * @see #getIndex()
   */
  public void setIndex(Integer index) {
    this.index = index;
  }

  /**
   * Returns the value of the property.
   * @return value of the property.
   * @see #setValue(String)
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value of the property.
   * @param value value of the property.
   * @see #getValue()
   */
  public void setValue(String value) {
    this.value = value;
  }
}