/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog;

import java.lang.reflect.Field;
import java.util.*;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dog.persistence.ojb.CollectFunctor;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The change functor collects all changes in the data objects between two
 * graphs. The algorithm first collects all objects in the original graph to be
 * able to compare with and stores them with the owner. The algorithm ensures
 * that the original representation of a modified object is synchronized meaning
 * all its properties are synchronized. If a property contains data object only
 * the existence and identity are synchronized. The properties of this objects
 * will be synchronized when the visitor reaches it.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ChangeFunctor implements DoFunctor {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(ChangeFunctor.class);

  /**
   * list of collected original objects. The type of the items is data object.
   */
  private List<DataObject> objects;

  /**
   * Map between the newly created objects of modified graph with the associated
   * newly created object in the original graph. New objects have no identifiers
   * therefore the map must be managed.
   */
  private Map<DataObject, DataObject> newObjects;

  /**
   * List of updates upon sucessful store of a graph of data objects. The type
   * of the items is data object update.
   */
  private List<DataObjectUpdate> updates;

  /**
   * Default constructor of the class.
   */
  public ChangeFunctor() {
    objects = new ArrayList<DataObject>();
    newObjects = new HashMap<DataObject, DataObject>();
    updates = new ArrayList<DataObjectUpdate>();
  }

  /**
   * Resets the list of collected data objects.
   * @param originalGraph the original graph to compare with.
   */
  public void reset(DataObject originalGraph) {
    objects.clear();
    newObjects.clear();
    updates.clear();
    CollectFunctor collect = new CollectFunctor();
    Class clazz = originalGraph.getClass();
    AbstractVisitorFunctor visitor = DataObjectManager.getVisitorFor(clazz);
    visitor.setFunctor(collect);
    DataObjectType.invokeVisitor(clazz, originalGraph, visitor);
    objects = new ArrayList<DataObject>(collect.getObjects());
  }

  public List<DataObject> getObjects() {
    return Collections.unmodifiableList(objects);
  }

  /**
   * Returns the list of collected data object updates. The identifier and the
   * timestamp are synchronized. All updates have a defined identifier and a
   * timestamp
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
    try {
      if (object.isNew()) {
        DataObject reference = newObjects.get(object);
        assert (reference != null)
            && (object.getClass() == reference.getClass());
        handle(object, reference);
        createUpdate(object);
      } else if (object.isModified()) {
        DataObject reference = findObject(object);
        assert (reference != null)
            && (object.getClass() == reference.getClass())
            && object.getId().equals(reference.getId());
        handle(object, reference);
        createUpdate(object);
      }
    } catch (IllegalArgumentException e) {
      log.error(e);
    } catch (IllegalAccessException e) {
      log.error(e);
    }
  }

  private void handle(DataObject object, DataObject reference)
      throws IllegalArgumentException, IllegalAccessException {
    List<Field> fields = retrieveFields(reference.getClass());
    for (Field field : fields) {
      if (isIndexed(field)) {
        List modifiedItems = getItems(object, field);
        List referenceItems = getItems(reference, field);
      } else {
        if (isTypeDataObject(field)) {
          DataObject modifiedItem = (DataObject) field.get(object);
          DataObject referenceItem = (DataObject) field.get(reference);
          if (!ObjectUtils.equals(modifiedItem, referenceItem)) {
            if (modifiedItem == null) {
              ReflectUtilities
                  .setProperty(referenceItem, field.getName(), null);
            } else if (modifiedItem.isNew()) {
              // TODO: create new instance and insert it.
            } else {
              // TODO: remove the item in reference, look if we have the needed
              // item in the removed list and if yes add it.
            }
          }
        } else {
          // handles the case of a simple property with a type not being
          // a data object.
          Object modifiedItem = field.get(object);
          Object referenceItem = field.get(reference);
          if (!ObjectUtils.equals(modifiedItem, referenceItem)) {
            ReflectUtilities.setProperty(referenceItem, field.getName(),
                ReflectUtilities.getProperty(modifiedItem, field.getName()));
          }
        }
      }
    }
  }

  /**
   * Retrieves the transitive closure of all fields declared in the class and
   * its ancestors.
   * @param clazz class which fields should be retrieved.
   * @return the list of fields declared in the class.
   * @pre (clazz != null) && DataObject.class.isAssignableFrom(clazz)
   */
  private List<Field> retrieveFields(Class clazz) {
    List<Field> fields = new ArrayList<Field>();
    Collections.addAll(fields, clazz.getFields());
    for (Class ancestor : clazz.getClasses()) {
      if (DataObject.class.isAssignableFrom(ancestor)) {
        fields.addAll(retrieveFields(ancestor));
      }
    }
    return fields;
  }

  /**
   * Returns true if the type of the field implements the data object interface.
   * @param field field to inquire.
   * @return true if the type of the field implements data object otherwise
   *         false.
   */
  private boolean isTypeDataObject(Field field) {
    return DataObject.class.isAssignableFrom(field.getType());
  }

  /**
   * Returns true if the field is an indexed one. An indexed field is either an
   * array, a collection, a list, a set or a map.
   * @param field field to inquire.
   * @return true if the field is indexed.
   */
  private boolean isIndexed(Field field) {
    Class type = field.getType();
    return type.isArray() || Collection.class.isAssignableFrom(type);
  }

  private List getItems(DataObject object, Field field)
      throws IllegalArgumentException, IllegalAccessException {
    assert isIndexed(field);
    List items;
    Class type = field.getType();
    if (type.isArray()) {
      items = Arrays.asList(field.get(object));
    } else {
      items = (List) field.get(object);
    }
    return items;
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
   * Finds the original data object associated with the given new data object.
   * @param object object which original data object should be found.
   * @return the requested object if found otherwise null.
   * @pre object != null
   */
  private DataObject findObject(DataObject object) {
    Class clazz = object.getClass();
    for (DataObject original : objects) {
      if ((clazz == original.getClass())
          && (object.getId().equals(original.getId()))) {
        return original;
      }
    }
    return null;
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