/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog.persistence.ojb;

import java.util.*;

import ch.bbv.dog.*;

/**
 * The collect functor collects all data objects in the graph.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CollectFunctor implements DoFunctor {
  /**
   * list of collected objects. The type of the items is data object.
   */
  private List<DataObject> objects;

  /**
   * list of new objects. The type of the items is data object.
   */
  private List<DataObject> newObjects;

  /**
   * list of updated objects. The type of the items is data object.
   */
  private List<DataObject> updatedObjects;

  /**
   * List of updates upon sucessful store of a graph of data objects. The type
   * of the items is data object update.
   */
  private List<DataObjectUpdate> updates;

  /**
   * Default constructor of the class.
   */
  public CollectFunctor() {
    objects = new ArrayList<DataObject>();
    newObjects = new ArrayList<DataObject>();
    updatedObjects = new ArrayList<DataObject>();
    updates = new ArrayList<DataObjectUpdate>();
  }

  /**
   * Resets the list of collected data objects.
   */
  public void reset() {
    objects.clear();
    newObjects.clear();
    updatedObjects.clear();
    updates.clear();
  }

  /**
   * Returns the list of collected data objects.
   * @return list of collected objects.
   */
  public List<DataObject> getObjects() {
    return Collections.unmodifiableList(objects);
  }

  /**
   * Returns the list of collected new data objects.
   * @return list of collected objects.
   */
  public List<DataObject> getNewObjects() {
    return Collections.unmodifiableList(newObjects);
  }

  /**
   * Returns the list of collected udpated data objects.
   * @return list of collected objects.
   */
  public List<DataObject> getUdpatedObjects() {
    return Collections.unmodifiableList(updatedObjects);
  }

  /**
   * Returns the list of collected data object updates. The identifier and the
   * timestamp are synchronized. All updates have a defined identifier and a
   * timestamp.
   * @return list of collected updates.
   */
  public List getUpdates() {
    for (DataObjectUpdate update : updates) {
      DataObject object = update.getObject();
      update.setId(object.getId());
      update.setTimestamp(object.getTimestamp());
      assert (update.getId() != null) && (update.getTimestamp() != null);
    }
    return Collections.unmodifiableList(updates);
  }

  /**
   * Returns the list of data objects from the given list which were not found
   * in the collected ones.
   * @param dataObjects data object list to compare to.
   * @return the list of missing data objects from the collected ones.
   */
  public List getMissingObjectsFrom(List<DataObject> dataObjects) {
    List<DataObject> deleted = new ArrayList<DataObject>();
    Map ids = createIds(dataObjects);
    for (DataObject original : dataObjects) {
      if (!ids.containsKey(new DataObjectId(original))) {
        deleted.add(original);
      }
    }
    return deleted;
  }

  public void execute(DataObject object) {
    objects.add(object);
    if (object.isNew()) {
      newObjects.add(object);
      createUpdate(object);
    } else if (object.isModified()) {
      updatedObjects.add(object);
      createUpdate(object);
    }
  }

  /**
   * Creates a partial update object of a modified data object. The update is
   * completed after the commit of the transaction.
   * @param object object being updated.
   */
  private void createUpdate(DataObject object) {
    DataObjectUpdate update = new DataObjectUpdate();
    update.setObject(object);
    update.setClassname(object.getClass().getName());
    updates.add(update);
  }

  /**
   * Creates a map of the identifiers of the list of data objects.
   * @param objects list of objects which identity should be computed.
   * @return the map of all identities.
   */
  private Map<DataObjectId, DataObjectId> createIds(List<DataObject> objects) {
    Map<DataObjectId, DataObjectId> ids = new HashMap<DataObjectId, DataObjectId>();
    for (HasId hasId : objects) {
      DataObjectId id = new DataObjectId(hasId);
      ids.put(id, id);
    }
    return ids;
  }
}