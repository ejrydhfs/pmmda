/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.updates.DataObjectUpdate;

/**
 * The default implementation of the data object interface is available as a
 * convenience class.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class DataObjectImpl implements DataObject {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The identifier of the data object.
   */
  private Integer id;

  /**
   * The modified flag of the data object.
   */
  private boolean modified;
  
  /**
   * The timestamp for optimistic locking of the data object.
   */
  private Timestamp timestamp;

  /**
   * The utility class used to implement change events for Java beans.
   */
  protected PropertyChangeSupport propertyChangeSupport;

  /**
   * The utility class used to implement vetoable change events for Java beans.
   */
  protected VetoableChangeSupport vetoableChangeSupport;
  
  /**
   * The update object collecting all modifications made to this instance.
   * After saving the changes made to the data object, the individual modifications
   * get flushed from the update object. 
   */
  protected DataObjectUpdate update;

  /**
   * Default constructor of the class.
   */
  public DataObjectImpl() {
    propertyChangeSupport = new PropertyChangeSupport(this);
    vetoableChangeSupport = new VetoableChangeSupport(this);
    update = new DataObjectUpdate();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public boolean isModified() {
    return modified;
  }

  public void setModified(boolean modified) {   
	this.modified = modified;
  }
  
  public boolean isNew() {
	return id == null;
  }

  public DataObject copy() {
    return null;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }

  public void addVetoableChangeListener(VetoableChangeListener listener) {
    vetoableChangeSupport.addVetoableChangeListener(listener);
  }

  public void addVetoableChangeListener(String propertyName,
      VetoableChangeListener listener) {
    vetoableChangeSupport.addVetoableChangeListener(propertyName, listener);
  }

  public void removeVetoableChangeListener(VetoableChangeListener listener) {
    vetoableChangeSupport.removeVetoableChangeListener(listener);
  }

  public void removeVetoableChangeListener(String propertyName,
      VetoableChangeListener listener) {
    vetoableChangeSupport.removeVetoableChangeListener(propertyName, listener);
  }

  public DataObjectUpdate getUpdate() {  
	return update;
  }
  
  public void resetUpdate() {  
    update = new DataObjectUpdate(this);
  }
}
  
