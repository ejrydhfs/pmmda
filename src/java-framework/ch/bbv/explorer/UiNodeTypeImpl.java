/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.apache.commons.beanutils.PropertyUtils;

import ch.bbv.dog.DataObjectType;
import ch.bbv.explorer.actions.*;
import ch.bbv.explorer.nodeactions.*;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The node type implementation provides a default implementation for the node
 * type interface. The class provides all the services defined in the interface.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class UiNodeTypeImpl implements UiNodeType {
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
  private List fields;

  /**
   * Map of all relations of the user interface node type.
   */
  private Map relations;

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

  private JPopupMenu popupMenu;

  private List uiNodeViews;

  private UiNodeViewType currentView;

  private UiNodeTypeFactory factory;

  /**
   * Constructor of the class.
   * @param type tyupe of the data object which user interface representation is
   *        defined.
   * @param nameProperty property containing the name to display in the left
   *        view.
   * @param nodePropertiesViewClass class defining the property view for the
   *        data object.
   */
  public UiNodeTypeImpl(DataObjectType type, String nameProperty,
      Class nodePropertiesViewClass) {
    this.type = type;
    fields = new ArrayList();
    relations = new HashMap();
    this.nameProperty = nameProperty;
    this.nodePropertiesViewClass = nodePropertiesViewClass;
    uiNodeViews = new ArrayList();
    editable = true;
    structureEditable = true;
  }

  /**
   * Sets the editable flag.
   * @param editable new value of the flag.
   */
  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  /**
   * Returns true if the lightweight object is editable.
   * @param lwObject lightweight object to check.
   * @return true if the object is editable otherwise false.
   */
  public boolean isEditable(Object lwObject) {
    return editable;
  }

  public void setStructureEditable(boolean structureEditable) {
    this.structureEditable = structureEditable;
  }

  /**
   * Returns true if the lightweight object structure is editable.
   * @param lwObject lightweight object to check.
   * @return true if the object structure is editable otherwise false.
   */
  public boolean isStructureEditable(Object lwObject) {
    return structureEditable;
  }

  public void registerViews(List uiNodeViews) {
    this.uiNodeViews = uiNodeViews;
  }

  public void registerView(UiNodeViewType uiNodeView) {
    uiNodeViews.add(uiNodeView);
  }

  public UiNodeViewType getCurrentView() {
    if ((currentView == null) && (uiNodeViews.size() > 0)) {
      currentView = (UiNodeViewType) uiNodeViews.get(0);
    }
    return currentView;
  }

  public void setCurrentView(String name) {
    currentView = getView(name);
  }

  public UiNodeViewType getView(String name) {
    assert (name != null);

    UiNodeViewType view = null;
    for (Iterator i = uiNodeViews.iterator(); i.hasNext();) {
      view = (UiNodeViewType) i.next();
      if (!name.equals(view.getName())) {
        view = null;
      } else {
        break;
      }
    }
    return view;
  }

  public Icon getIconFor(Object lwObject) {
    return null;
  }

  public String getNameFor(Object lwObject) {
    String name = null;
    try {
      name = (String) PropertyUtils.getProperty(lwObject, nameProperty);
    } catch (InvocationTargetException ite) {
      ite.printStackTrace();
    } catch (NoSuchMethodException nsmi) {
      nsmi.printStackTrace();
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
    }

    return name;
  }

  public Object getProperty(Object object, UiField field) {
    assert ((object != null) && (field != null));

    Object value = null;
    try {
      value = PropertyUtils.getProperty(object, field.getProperty());
    } catch (InvocationTargetException ite) {
      ite.printStackTrace();
    } catch (NoSuchMethodException nsmi) {
      nsmi.printStackTrace();
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
    }

    return value;
  }

  public void setProperty(Object object, UiField field, Object value) {
    ReflectUtilities.setProperty(object, field.getProperty(), value);
  }

  public Object getDataObjectFor(Object lwObject) {
    return null;
  }

  public JPopupMenu getPopupMenu(ExplorerNode node) {
    if (popupMenu == null) {
      UiNodeType nodeType = node.getNodeType();
      popupMenu = new JPopupMenu();
      popupMenu.add(new DisplayNodeAction(this, "View"));
      if (nodeType.isEditable(null)) {
        popupMenu.add(new EditNodeAction(this, "Edit"));
      }
      if (uiNodeViews.size() > 0) {
        JMenu viewMenu = new JMenu("Views...");
        for (Iterator i = uiNodeViews.iterator(); i.hasNext();) {
          UiNodeViewType view = (UiNodeViewType) i.next();
          viewMenu.add(new DisplayNodeAction(this, view.getName()));
        }
        popupMenu.add(viewMenu);
      }

      Collection relations = getRelations();
      if (relations.size() > 0) {
        popupMenu.addSeparator();

        UiNodeTypeFactory factory = nodeType.getFactory();
        JMenu createMenu = new JMenu("Add...");
        for (Iterator i = relations.iterator(); i.hasNext();) {
          UiRelation relation = (UiRelation) i.next();
          UiNodeType childType = factory
              .getNodeTypeForLw(relation.getLwClass());
          if (childType != null) {
            createMenu
                .add(new AddNodeAction(childType, relation.getProperty()));
          }
        }
        popupMenu.add(createMenu);
      }
      if (nodeType.getDataObjectType().isRoot()) {
        popupMenu.add(createRootNodeActions());
      }
      if (nodeType.isStructureEditable(null)) {
        popupMenu.add(new RemoveNodeAction(this, "Remove"));
      }
    }

    return popupMenu;
  }

  public Class getPropertiesView() {
    return this.nodePropertiesViewClass;
  }

  public DataObjectType getDataObjectType() {
    return type;
  }

  public Collection getRelations() {
    return relations.values();
  }

  public int getRelationSize(Object object, UiRelation relation) {
    return 0;
  }

  public int getRowCount(Object object) {
    int rowCount = 0;
    for (Iterator i = getRelations().iterator(); i.hasNext();) {
      UiRelation relation = (UiRelation) i.next();
      rowCount += getPropertySize(object, relation);
    }

    return rowCount;
  }

  /**
   * Adds an indexed property to the list of indexed properties belonging to the
   * node type.
   * @param relation user interface relation describing the characteristics of
   *        relation to a data object.
   * @pre property != null
   */
  public void addRelation(UiRelation relation) {
    assert (relation != null);
    relations.put(relation.getProperty(), relation);
  }

  public UiRelation getRelation(String property) {
    assert (property != null);
    return (UiRelation) relations.get(property);
  }

  /**
   * Adds a simple property to the list of indexed properties belonging to the
   * node type.
   * @param field user interface property describing the characteristics of a
   *        simple property.
   * @pre property != null
   */
  public void addField(UiField field) {
    fields.add(field);
  }

  public List getFields() {
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
    for (Iterator i = nodeType.getRelations().iterator(); i.hasNext();) {
      UiRelation relation = (UiRelation) i.next();
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
      for (Iterator i = nodeType.getFields().iterator(); i.hasNext();) {
        UiField field = (UiField) i.next();
        if (object == field.getProperty(parent)) {
          property = field.getProperty();
          break;
        }
      }
    }
    return property;
  }

  void setFactory(UiNodeTypeFactory factory) {
    this.factory = factory;
  }

  /**
   * Creates the menu of actions for a root node.
   * @return the menu of actions available to a root node.
   */
  private JMenu createRootNodeActions() {
    JMenu actions = new JMenu("Actions...");
    actions.add(new SaveNodeAction(this, "Save"));
    actions.add(new RevertNodeAction(this, "Revert"));
    actions.add(new ReloadNodeAction(this, "Reload"));
    return actions;
  }
}