/* copyright 2000-2007 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.sql.Timestamp;

/**
 * The data object interface defines the common interface for all data objects
 * manipulated through the data object tree framework. The interface requires a
 * minimal set of methods and follows the convention of Java beans. Change and
 * vetoable change listeners are supported.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface DataObject extends HasId {
  /**
   * List of simple system properties which should not be handled through
   * introspection.
   */
  static final String[] SYSTEM_SIMPLE_PROPERTIES = { "class", "id", "modified",
      "new", "timestamp" };
  
  /**
   * List of indexed framework properties which should not be handled through
   * introspection.
   */
  static final String[] SYSTEM_INDEXED_PROPERTIES = { "propertyValues"};

  /**
   * Sets the identifier of the data object.
   * @param id new identifier of the data object.
   * @see HasId#getId
   */
  void setId(Integer id);

  /**
   * Returns true if the object was modified otherwise false. A new object is
   * modified.
   * @return true if the object was modified otherwise false.
   * @see #setModified
   */
  boolean isModified();

  /**
   * Sets the new value of the modified flag.
   * @param modified new value of the modified flag.
   * @see #isModified
   */
  void setModified(boolean modified);

  /**
   * Returns true if the object is a new one otherwise false. A object is new if
   * its identity is null. No property is associated with it.
   * @return true if the object is new otherwise false.
   */
  boolean isNew();

  /**
   * Performs a deep copy of all referenced data objects and their properties.
   * @return the cloned data object.
   */
  DataObject copy();

  /**
   * Returns the timestamp the last time the object was stored in the persistent
   * store.
   * @return the timestamp of the last update. If the object is new, null is
   *         returned.
   * @see #setTimestamp(Timestamp)
   */
  Timestamp getTimestamp();

  /**
   * Sets the timestamp of the data object.
   * @param timestamp new timestamp value.
   * @see #getTimestamp()
   */
  void setTimestamp(Timestamp timestamp);

  /**
   * Adds a property change listener to the data object. If not defined for the
   * class an operation not supported exception is thrown.
   * @param listener listener to add.
   * @see #removePropertyChangeListener(PropertyChangeListener)
   */
  void addPropertyChangeListener(PropertyChangeListener listener);

  /**
   * Adds a property change listener for a property to the data object. If not
   * defined for the class an operation not supported exception is thrown.
   * @param propertyName name of the property to listen to.
   * @param listener listener to add.
   * @see #removePropertyChangeListener(String, PropertyChangeListener)
   */
  void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener);

  /**
   * Remove the property change listener from the data object. If not defined
   * for the class an operation not supported exception is thrown.
   * @param listener listener to remove.
   * @see #addPropertyChangeListener(PropertyChangeListener)
   */
  void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * Remove the property change listener from the data object. If not defined
   * for the class an operation not supported exception is thrown.
   * @param propertyName name of the property listened to.
   * @param listener listener to remove.
   * @see #addPropertyChangeListener(String, PropertyChangeListener)
   */
  void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener);

  /**
   * Adds a property vetoable change listener to the data object. If not defined
   * for the class an operation not supported exception is thrown.
   * @param listener listener to add.
   * @see #removeVetoableChangeListener(VetoableChangeListener)
   */
  void addVetoableChangeListener(VetoableChangeListener listener);

  /**
   * Adds a property vetoable change listener for a property to the data object.
   * If not defined for the class an operation not supported exception is
   * thrown.
   * @param propertyName name of the property to listen to.
   * @param listener listener to add.
   * @see #removeVetoableChangeListener(String, VetoableChangeListener)
   */
  void addVetoableChangeListener(String propertyName,
      VetoableChangeListener listener);

  /**
   * Remove the property vetoable change listener from the data object. If not
   * defined for the class an operation not supported exception is thrown.
   * @param listener listener to remove.
   * @see #addVetoableChangeListener(VetoableChangeListener)
   */
  void removeVetoableChangeListener(VetoableChangeListener listener);

  /**
   * Remove the property vetoable change listener from the data object. If not
   * defined for the class an operation not supported exception is thrown.
   * @param propertyName name of the property listened to.
   * @param listener listener to remove.
   * @see #addVetoableChangeListener(String, VetoableChangeListener)
   */
  void removeVetoableChangeListener(String propertyName,
      VetoableChangeListener listener);
}
