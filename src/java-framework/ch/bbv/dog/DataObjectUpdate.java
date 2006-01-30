/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * The data object update defines the data transfered to a client upon a
 * sucessfull update. The information is used to synchronize the copy of data
 * objects in the client with the new values stored in the database in the
 * context of optimistic locking.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class DataObjectUpdate implements Serializable {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Identifier of the object been updated.
   */
  private Integer id;

  /**
   * Name of the class of the data object.
   */
  private String classname;

  /**
   * Timestamp of the update.
   */
  private Timestamp timestamp;

  /**
   * The object being updated. This value should not be transmitted over the
   * wire.
   */
  private transient DataObject object;

  /**
   * List of all modified properties of the object.
   */
  private List<DataPropertyUpdate> updates;

  /**
   * Default constructor of the class.
   */
  public DataObjectUpdate() {
    updates = new ArrayList<DataPropertyUpdate>();
  }

  /**
   * Returns the identifier of the update.
   * @return the data object identifier.
   * @see #setId
   */
  public Integer getId() {
    return id;
  }

  /**
   * Sets the identifier of the update.
   * @param id the new data object identifier.
   * @see #getId
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Returns the classname of the update.
   * @return the data object classname.
   * @see #setClassname
   */
  public String getClassname() {
    return classname;
  }

  /**
   * Sets the classname of the update.
   * @param classname the new data object classname.
   * @see #getClassname
   */
  public void setClassname(String classname) {
    this.classname = classname;
  }

  /**
   * Returns the optimistic locking timestamp of the update.
   * @return the optimistic locking timestamp.
   * @see #setTimestamp
   */
  public Timestamp getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the optimistic locking timestamp of the update.
   * @param timestamp new optimistic locking timestamp.
   * @see #getTimestamp
   */
  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Gets the data object being updated.
   * @return data object being updated.
   * @see #setObject
   */
  public DataObject getObject() {
    return object;
  }

  /**
   * Sets the object being updated.
   * @param object the data object being updated.
   * @see #getObject()
   */
  public void setObject(DataObject object) {
    this.object = object;
  }

  /**
   * Returns the updates of the update.
   * @return list of updates.
   * @see #setUpdates
   */
  public List getUpdates() {
    return updates;
  }

  /**
   * Sets the updates of the update.
   * @param updates new updates list.
   * @see #getUpdates
   */
  public void setUpdates(List<DataPropertyUpdate> updates) {
    this.updates = updates;
  }

  /**
   * Returns the update stored at the given index.
   * @param index index of the requested data property update.
   * @return the requested data property update.
   */
  public DataPropertyUpdate getUpdate(int index) {
    return updates.get(index);
  }

  /**
   * Adds an update to the list of updates.
   * @param update data property update to add.
   */
  public void addUpdate(DataPropertyUpdate update) {
    updates.add(update);
  }

  /**
   * Adds a list of updates.
   * @param updates list of data property updates to add.
   */
  public void addUpdates(List<DataPropertyUpdate> updates) {
    updates.addAll(updates);
  }
}