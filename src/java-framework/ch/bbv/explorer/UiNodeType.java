/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.util.Collection;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JPopupMenu;

import ch.bbv.dog.DataObjectType;
import ch.bbv.dog.LightweightObject;

/**
 * The node type defines the characteristics of a class displayed in the tree
 * and list explorer view. A node type provides all descriptive type information
 * about a viewable data object. It describes how to display a Java bean in the
 * explorer. Each node type describes the following aspects.
 * <ul>
 * <dt>Name Getter</dt>
 * <dd>Getter method providing the name of the instance of the node type. The
 * name is displayed as label in the navigation tree.</dd>
 * <dt>Icon Factory</dt>
 * <dd>Optional factory to provide icons for instances of the node type.</dd>
 * <dt>Object Factory</dt>
 * <dd>The object factory is responsible for the creation of new nodes. The
 * factory also retrieves the data object associated with the lightweight data
 * object displayed in the navigator view.
 * <dd>
 * <dt>Node views</dt>
 * <dd>Multiple views can be configured to display selected attributes of the
 * children of the currently selected node.</dd>
 * </ul>
 * <p>
 * At most one node type can be defined for a given Java class. The display
 * modes of the various view are configured in the associated views. The factory
 * is used to retrieve the node type associated with a given data object or
 * lightweight class.
 * <p>
 * @author Marcel Baumann
 * @version $Revision$
 */
public interface UiNodeType {
  /**
   * Returns the icon associated with the given object. The object is described
   * in this node type.
   * @param lwObject object, which icon is requested.
   * @return the icon representing the object and its state.
   * @pre lwObject != null
   */
  Icon getIconFor(Object lwObject);

  /**
   * Returns the name of the object as it should be displayed. The object is
   * described in this node type.
   * @param lwObject object, which node is requested
   * @return the name of the object.
   * @pre lwObject != null
   */
  String getNameFor(Object lwObject);

  /**
   * Returns the data object represented with the given lightweight object. The
   * node type is responsible for the management between data objects and their
   * lightweight representation.
   * @param lwObject object, which node is requested
   * @return the data object associated with the lightweight object.
   * @pre lwObject != null
   */
  Object getDataObjectFor(Object lwObject);

  /**
   * Returns true if the data object associated with it is editable. The data
   * object.
   * @param lwObject object, which status should be returned.
   * @return true if the object can be edited otherwise false.
   * @pre lwObject != null
   */
  boolean isEditable(Object lwObject);

  /**
   * Returns true if the the data object and the relation tying it with its
   * owner can be edited. Object can be added, inserted and removed from an
   * editable relation.
   * @param lwObject object, which status should be returned.
   * @return true if the object and its children can be removed otherwise false.
   * @pre lwObject != null
   */
  boolean isStructureEditable(Object lwObject);

  /**
   * Returns the popup menu associated with node type. The popup menu contains
   * all configured views to display the node and its children in the right
   * pane, the manipulation actions for its relations, and specific actions
   * associated with the node type.
   * @param node node which popup menu should be displayed.
   * @return the popup menu associated with the menu.
   * @pre node != null
   */
  JPopupMenu getPopupMenu(ExplorerNode node);

  /**
   * Returns the property panel used to edit an existing object or to initialize
   * a newly created object. A property panel is defined for a data object.
   * @return the properties view used to modify an object described in this node
   *         type.
   * @see NodePropertiesView
   */
  Class getPropertiesView();

  /**
   * Returns the class of the view associated with the given name.
   * @param name the name of the requested view.
   * @return the requested view if found otherwise null.
   * @pre name != null
   */
  UiNodeViewType getView(String name);

  /**
   * Returns the current view associated with the node. If the current view is
   * defined it is automatically used to display the nodes in the node view.
   * @return the current user interface view if defined otherwise null.
   * @see #setCurrentView(String)
   */
  UiNodeViewType getCurrentView();

  /**
   * Sets the current view associated with the node. If the name is null the
   * current view is set back to the default one. The default view is the first
   * one in the list of known user interface views with the default flag.
   * @param name name of the new current view. If null no current view is
   *        defined.
   * @pre name != null
   * @see #getCurrentView()
   */
  void setCurrentView(String name);

  /**
   * Returns the type of the data object associated with this node type.
   * @return data object class if defined otherwise null.
   */
  DataObjectType getDataObjectType();

  /**
   * Returns the list of relations displayed in the navigator. The relations are
   * used to retrieve the data object children of an instance of this node type.
   * @return list of relations displayed in the explorer.
   */
  Collection getRelations();

  /**
   * Returns the user interface relation with the given name.
   * @param name name of the requested relation.
   * @return the requested relation if found otherwise null.
   * @pre name != null
   */
  UiRelation getRelation(String name);

  /**
   * Returns the list of properties as a list of user interface properties.
   * @return list of simple properties displayed in the explorer.
   */
  List getFields();

  /**
   * Returns the size of the properties with the given name of the specified
   * object. A simple property returns one, a multiple property, its effective
   * size.
   * @param object object which relation should be computed.
   * @param relation relation which size should be computed.
   * @return the number of objects in the relation.
   * @pre object != null && property != null
   */
  int getRelationSize(Object object, UiRelation relation);

  /**
   * Returns the number of rows for the given object.
   * @param object object which number of the children should be infered.
   * @return number of rows the table representation of the object has.
   * @pre object != null
   */
  int getRowCount(Object object);

  /**
   * Returns the factory to which the node belongs.
   * @return the factory owning the node if defined otherwise null.
   */
  UiNodeTypeFactory getFactory();

  /**
   * Finds the property containing the given lightweight object.
   * @param parent lightweight parent data object.
   * @param object lightweight owned data object.
   * @return the name of the property implementing the aggregation.
   */
  String findProperty(LightweightObject parent, LightweightObject object);
}