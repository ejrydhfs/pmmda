/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog.persistence;

import java.util.*;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectUpdate;
import ch.bbv.dog.DoFunctor;

/**
 * The collect functor collects all data objects in the graph.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CollectFunctor implements DoFunctor {
  /**
   * list of collected objects. The type of the items is data object.
   */
  private List objects;

  /**
   * list of new objects. The type of the items is data object.
   */
  private List newObjects;

  /**
   * list of updated objects. The type of the items is data object.
   */
  private List updatedObjects;

  /**
   * List of updates upon sucessful store of a graph of data objects. The type
   * of the items is data object update.
   */
  private List updates;

  /**
   * Default constructor of the class.
   */
  public CollectFunctor() {
    objects = new ArrayList();
    newObjects = new ArrayList();
    updatedObjects = new ArrayList();
    updates = new ArrayList();
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
  public List getObjects() {
    return Collections.unmodifiableList(objects);
  }

  /**
   * Returns the list of collected new data objects.
   * @return list of collected objects.
   */
  public List getNewObjects() {
    return Collections.unmodifiableList(newObjects);
  }

  /**
   * Returns the list of collected udpated data objects.
   * @return list of collected objects.
   */
  public List getUdpatedObjects() {
    return Collections.unmodifiableList(updatedObjects);
  }

  /**
   * Returns the list of collected data object updates. The identifier and the
   * timestamp are synchronized.
   * @return list of collected updates.
   */
  public List getUpdates() {
    for (Iterator i = updates.iterator(); i.hasNext();) {
      DataObjectUpdate update = (DataObjectUpdate) i.next();
      DataObject object = update.getObject();
      update.setId(object.getId());
      update.setTimestamp(object.getTimestamp());
    }
    return Collections.unmodifiableList(updates);
  }

  /**
   * Returns the list of data objects from the given list which were not found
   * in the collected ones.
   * @param dataObjects data object list to compare to.
   * @return the list of missing data objects from the collected ones.
   */
  public List getMissingObjectsFrom(List dataObjects) {
    List deleted = new ArrayList();
    for (Iterator i = dataObjects.iterator(); i.hasNext();) {
      DataObject original = (DataObject) i.next();
      if (!objects.contains(original)) {
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

  private void createUpdate(DataObject object) {
    DataObjectUpdate update = new DataObjectUpdate();
    update.setObject(object);
    update.setClassname(object.getClass().getName());
    updates.add(update);
    object.setModified(false);
  }
}