/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog.updates;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectId;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The data object update defines the data transferred to a client upon a
 * successful update. The information is used to synchronize the copy of data
 * objects in the client with the new values stored in the database in the
 * context of optimistic locking.
 * @author MarcelBaumann
 * @author ZlatkoFranjcic
 * @version $Revision$
 */
public class DataObjectUpdate implements Serializable {
  
  private static final long serialVersionUID = UpdatesSerialVersion.UID;

  /**
   * The id of the last created instance.
   */
  private static int staticInstanceId = 0;
  
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
  private List<Update> updates;

  /**
   * The instance id of this update. It is used to map local update objects
   * that have been sent to a remote site to the update objects that are
   * sent back from the very same remote site. This mapping is used to
   * identify updates that refer to the same data object.
   */
  private int instanceId;
    
  /**
   * Default constructor of the class.
   */
  public DataObjectUpdate() {   
    init();
  }
  
  /**
   * Initializes a new instance given the corresponding data object.   
   * @param object The data object this update belongs to.
   */
  public DataObjectUpdate(DataObject object) {
	init();
    this.object = object;
    id = object.getId();
    timestamp = object.getTimestamp();
    classname = object.getClass().getName();    
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
   * @return the data object being updated.
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
   * Returns the individual updates.
   * @return list of updates.
   * @see Update
   */
  public List<Update> getUpdates() {
    return updates;
  }

  /**
   * Adds an update to the list of updates.
   * @param update data property update to add.
   */
  public void addUpdate(Update update) {
    updates.add(update);
  }

  /**
   * Clears the list of modifications made to the underlying data object and
   * (re-)sets the id and time stamp of this update to the data object's id and
   * time stamp.
   */
  public void clearUpdates() {
	if (object != null) {
		id = object.getId();
        timestamp = object.getTimestamp();
	}
    updates.clear();
  }

  /**
   * Executes this update. It is assumed that this update is associated to a
   * non-null data object. The data object can be set either when instantiating
   * an update or via a call to the {@link #setObject(DataObject)} method. 
   */
  public void execute() {	 
	for(Update update : updates) {
		update.execute(object);
	}
	
	if (modifiesDataObject()) {
		object.setModified(true);
	}
  }

  /**
   * Indicates whether this update modifies the associated data object.
   * @return true if the update modifies the data object
   */
  public boolean modifiesDataObject() {	
	return !updates.isEmpty();
  }

  @Override
  public int hashCode() {
	  return instanceId;
  }
  
  @Override
  public boolean equals(Object o) {  
	  if (this == o) {
		  return true;
	  } else if (o instanceof DataObjectUpdate) {
		  return instanceId == ((DataObjectUpdate)o).instanceId;
	  } else {
		  return false;
	  }
  }
  
  /**
   * Initializes this update (called by all constructors).
   */
  private void init() {
	  instanceId = staticInstanceId++;
	  updates = new ArrayList<Update>();
  }
}