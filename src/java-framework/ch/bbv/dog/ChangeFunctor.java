/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog;

import java.lang.reflect.Field;
import java.util.*;

import org.apache.commons.lang.ObjectUtils;

import ch.bbv.dog.persistence.ojb.CollectFunctor;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The change functor collects all changes in the data objects between two
 * graphs. The algorithm first collects all objects in the original graph to be
 * able to compare with and stores them with the owner.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ChangeFunctor implements DoFunctor {
  /**
   * The original graph used to infer the changes against the traversed graph.
   */
  private DataObject originalGraph;

  /**
   * list of collected original objects. The type of the items is data object.
   */
  private List<DataObject> objects;

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
  public ChangeFunctor() {
    objects = new ArrayList<DataObject>();
    updatedObjects = new ArrayList<DataObject>();
    updates = new ArrayList<DataObjectUpdate>();
  }

  /**
   * Resets the list of collected data objects.
   * @param originalGraph the original graph to compare with.
   */
  public void reset(DataObject originalGraph) {
    this.originalGraph = originalGraph;
    objects.clear();
    updatedObjects.clear();
    updates.clear();
    CollectFunctor collect = new CollectFunctor();
    Class clazz = originalGraph.getClass();
    AbstractVisitorFunctor visitor = DataObjectManager.getVisitorFor(clazz);
    visitor.setFunctor(collect);
    DataObjectType.invokeVisitor(clazz, originalGraph, visitor);
    objects = new ArrayList<DataObject>(collect.getObjects());
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
    objects.add(object);
    if (object.isNew()) {
      createUpdate(object);
    } else if (object.isModified()) {
      updatedObjects.add(object);
      createUpdate(object);
    }
  }

  private void handle(DataObject modified, DataObject reference)
      throws IllegalArgumentException, IllegalAccessException {
    assert modified.getClass() == reference.getClass();
    List<Field> fields = retrieveFields(reference.getClass());
    for (Field field : fields) {
      if (isIndexed(field)) {
        List modifiedItems = getItems(modified, field);
        List referenceItems = getItems(reference, field);
      } else {
        if (isTypeDataObject(field)) {
          DataObject modifiedItem = (DataObject) field.get(modified);
          DataObject referenceItem = (DataObject) field.get(reference);
        } else {
          // handles the case of a simple property with a type not being
          // a data object.
          Object modifiedItem = field.get(modified);
          Object referenceItem = field.get(reference);
          if (ObjectUtils.equals(modifiedItem, referenceItem)) {
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

  private Class getComponentType(Field field) {
    Class componentType = null;
    Class type = field.getType();
    if (type.isArray()) {
      componentType = type.getComponentType();
    } else {
      componentType = DataObject.class;
    }
    return componentType;
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