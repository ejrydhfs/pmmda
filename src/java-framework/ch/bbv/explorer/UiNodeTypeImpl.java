/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ch.bbv.dog.DataObjectType;

/**
 * The node type implementation provides a default implementation for the node
 * type interface. The class provides all the services defined in the interface.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class UiNodeTypeImpl implements UiNodeType {
  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(UiNodeTypeImpl.class);

  /**
   * The data object type descriptor for the type displayed in this node type.
   */
  private DataObjectType type;

  /**
   * The class defining the view on the properties of the data object.
   */
  private Class nodePropertiesViewClass;

  /**
   * List of fields of the children to display on the right pane.
   */
  private List<UiField> fields;

  /**
   * Map of all relations of the user interface node type.
   */
  private Map<String, UiRelation> relations;

  /**
   * The simple property containing the name of the node as displayed in the
   * navigator.
   */
  private String nameProperty;

  /**
   * Flag indicating if the node is editable.
   */
  private boolean editable;

  /**
   * Flag indicating if the children of the node can be added or removed.
   */
  private boolean structureEditable;

  private List<UiNodeViewType> uiNodeViews;

  private UiNodeViewType currentView;

  /**
   * The user interface node type factory ownint the node type.
   */
  private UiNodeTypeFactory factory;

  private Map<String, Object> parameters;

  /**
   * Constructor of the class.
   * @param type tyupe of the data object which user interface representation is
   *          defined.
   * @param nameProperty property containing the name to display in the left
   *          view.
   * @param nodePropertiesViewClass class defining the property view for the
   *          data object.
   */
  public UiNodeTypeImpl(DataObjectType type, String nameProperty,
      Class nodePropertiesViewClass) {
    this.type = type;
    fields = new ArrayList<UiField>();
    relations = new HashMap<String, UiRelation>();
    this.nameProperty = nameProperty;
    this.nodePropertiesViewClass = nodePropertiesViewClass;
    parameters = new HashMap<String, Object>();
    uiNodeViews = new ArrayList<UiNodeViewType>();
    editable = true;
    structureEditable = true;
  }

  /**
   * Sets the editable flag.
   * @param editable new value of the flag.
   * @see #isEditable(Object)
   */
  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  /**
   * Returns true if the lightweight object is editable.
   * @param lwObject lightweight object to check.
   * @return true if the object is editable otherwise false.
   * @see #setEditable(boolean)
   */
  public boolean isEditable(Object lwObject) {
    return editable;
  }

  /**
   * Sets the structure editable flag.
   * @param structureEditable new value of the flag.
   * @see #isStructureEditable(Object)
   */
  public void setStructureEditable(boolean structureEditable) {
    this.structureEditable = structureEditable;
  }

  /**
   * Returns true if the lightweight object structure is editable.
   * @param lwObject lightweight object to check.
   * @return true if the object structure is editable otherwise false.
   * @see #setStructureEditable(boolean)
   */
  public boolean isStructureEditable(Object lwObject) {
    return structureEditable;
  }

  public void registerViews(List<UiNodeViewType> uiNodeViews) {
    this.uiNodeViews = uiNodeViews;
  }

  public void registerView(UiNodeViewType uiNodeView) {
    uiNodeViews.add(uiNodeView);
  }

  public List<UiNodeViewType> getViewTypes() {
    return Collections.unmodifiableList(uiNodeViews);
  }

  public UiNodeViewType getCurrentView() {
    if ((currentView == null) && (uiNodeViews.size() > 0)) {
      currentView = uiNodeViews.get(0);
    }
    return currentView;
  }

  public void setCurrentView(String name) {
    assert (name != null);
    currentView = getView(name);
  }

  public UiNodeViewType getView(String name) {
    assert (name != null);
    for (UiNodeViewType viewType : uiNodeViews) {
      if (name.equals(viewType.getName())) {
        return viewType;
      }
    }
    return null;
  }

  public String getNameFor(Object lwObject) {
    String name = null;
    try {
      name = (String) PropertyUtils.getProperty(lwObject, nameProperty);
    } catch (InvocationTargetException e) {
      log.error(e);
    } catch (NoSuchMethodException e) {
      log.error(e);
    } catch (IllegalAccessException e) {
      log.error(e);
    }
    return name;
  }

  public Object getDataObjectFor(Object lwObject) {
    return null;
  }

  public Class getPropertiesView() {
    return this.nodePropertiesViewClass;
  }

  public DataObjectType getDataObjectType() {
    return type;
  }

  public Collection<UiRelation> getRelations() {
    return relations.values();
  }

  public int getRelationSize(Object object, UiRelation relation) {
    return 0;
  }

  public int getRowCount(Object object) {
    int rowCount = 0;
    for (UiRelation relation : getRelations()) {
      rowCount += getPropertySize(object, relation);
    }
    return rowCount;
  }

  /**
   * Adds an indexed property to the list of indexed properties belonging to the
   * node type.
   * @param relation user interface relation describing the characteristics of
   *          relation to a data object.
   * @pre relation != null
   */
  public void addRelation(UiRelation relation) {
    assert relation != null;
    relations.put(relation.getProperty(), relation);
  }

  public UiRelation getRelation(String property) {
    assert property != null;
    return relations.get(property);
  }

  /**
   * Adds a simple property to the list of indexed properties belonging to the
   * node type.
   * @param field user interface property describing the characteristics of a
   *          simple property.
   * @pre field != null
   */
  public void addField(UiField field) {
    assert field != null;
    fields.add(field);
  }

  public List<UiField> getFields() {
    return fields;
  }

  public int getPropertySize(Object object, UiRelation relation) {
    return 1;
  }

  public UiNodeTypeFactory getFactory() {
    return factory;
  }

  public String findProperty(Object parent, Object object) {
    assert ((parent != null) && (object != null));
    String property = null;
    UiNodeType nodeType = factory.getNodeTypeForLw(parent.getClass());
    for (UiRelation relation : nodeType.getRelations()) {
      Class lwClass = relation.getLwClass();
      if ((lwClass != null) && (object.getClass() == lwClass)) {
        Collection values = relation.getValues(parent);
        if (values.contains(object)) {
          property = relation.getProperty();
          break;
        }
      }
    }
    if (property == null) {
      for (UiField field : nodeType.getFields()) {
        if (object == field.getProperty(parent)) {
          property = field.getPropertyName();
          break;
        }
      }
    }
    return property;
  }

  public Object setParameter(String key, Object value) {
    return parameters.put(key, value);
  }

  public Object getParameter(String key) {
    return parameters.get(key);
  }

  void setFactory(UiNodeTypeFactory factory) {
    this.factory = factory;
  }
}