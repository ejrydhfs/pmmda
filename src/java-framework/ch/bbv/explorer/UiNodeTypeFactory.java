/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * License. $Id: UiNodeTypeFactory.java,v 1.1 2004/08/10 17:36:00 marcelbaumann
 * Exp $
 */
package ch.bbv.explorer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.utilities.ReflectUtilities;

/**
 * The factory handles all node types for one type of explorer and provides
 * services to retrieve the node type for a data model class instance. The
 * factory also provides services to retrieve the properties view for a
 * registered node type.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class UiNodeTypeFactory implements Cloneable {
  /**
   * The types of the create method signature to instantiate a new node view.
   */
  private static final Class[] types = { UiNodeType.class, Object.class };

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(UiNodeTypeFactory.class);

  /**
   * Map of node types associated with the class of a business object type.
   */
  private Map<Class, UiNodeType> nodeTypesForBo;

  /**
   * Map of node types associated with the class of a lightweight business
   * object type.
   */
  private Map<Class, UiNodeType> nodeTypesForLw;

  /**
   * Map of the node types associated with their property view.
   */
  private Map<UiNodeType, NodePropertiesView> nodePropertiesViews;

  /**
   * The node views map contains all the views of the registered node types. The
   * node views is Map <UiNodeType, Map <UiNodeViewType, NodeView> >.
   */
  private Map<UiNodeType, Map<UiNodeViewType, NodeView>> nodeViews;

  /**
   * The icon factory of the node type factory used to retrieve node icons.
   */
  private IconFactory iconFactory;

  /**
   * The popup menu factory used to create the menus associated with node types.
   */
  private PopupFactory popupFactory;

  /**
   * Default constructor of the class.
   */
  public UiNodeTypeFactory() {
    nodeTypesForBo = new HashMap<Class, UiNodeType>();
    nodeTypesForLw = new HashMap<Class, UiNodeType>();
    nodePropertiesViews = new HashMap<UiNodeType, NodePropertiesView>();
    nodeViews = new HashMap<UiNodeType, Map<UiNodeViewType, NodeView>>();
    iconFactory = new IconFactoryImpl();
    popupFactory = new PopupFactoryImpl();
  }

  /**
   * @inheritDoc
   * @see Object#clone()
   */
  @Override
  public UiNodeTypeFactory clone() {
    UiNodeTypeFactory clone = null;
    try {
      clone = (UiNodeTypeFactory)super.clone();
    } catch (CloneNotSupportedException e) {
      // not possible
    }
    clone.iconFactory = this.iconFactory;
    clone.nodePropertiesViews = this.nodePropertiesViews;
    clone.nodeTypesForBo = this.nodeTypesForBo;
    clone.nodeTypesForLw = this.nodeTypesForLw;
    clone.nodeViews = this.nodeViews;
    clone.popupFactory = this.popupFactory;
    return clone;
  }

  /**
   * Returns the icon factory of the node type factory.
   * @return the icon factory.
   */
  public IconFactory getIconFactory() {
    return iconFactory;
  }

  /**
   * Returns the popup menu factory of the node type factory.
   * @return the popup menu factory.
   */
  public PopupFactory getPopupFactory() {
    return popupFactory;
  }

  /**
   * Registers the node type in the factory. The node is configured to know its
   * factory.
   * @param type user interface type to register.
   * @pre type != null
   */
  public void register(UiNodeType type) {
    assert (type != null);
    ((UiNodeTypeImpl) type).setFactory(this);
    nodeTypesForBo.put(type.getDataObjectType().getBoClass(), type);
    nodeTypesForLw.put(type.getDataObjectType().getLwClass(), type);
  }

  /**
   * Returns the node type associated with the given lightweight data object
   * class.
   * @param clazz class of the lightweight data object.
   * @return the user interface node type if found otherwise null.
   * @pre clazz != null
   */
  public UiNodeType getNodeTypeForLw(Class clazz) {
    assert (clazz != null);
    return nodeTypesForLw.get(clazz);
  }

  /**
   * Returns the node type associated with the given data object class.
   * @param clazz class of the data object.
   * @return the user interface node type if found otherwise null.
   * @pre clazz != null
   */
  public UiNodeType getNodeTypeForBo(Class clazz) {
    assert (clazz != null);
    return nodeTypesForBo.get(clazz);
  }

  /**
   * Returns the properties view associated with the given node type. The
   * algorithm assumes that at most one properties view is displayed per
   * application.
   * @param type node type which properties view is requested.
   * @return the requested properties view for the given node type.
   * @pre type != null
   */
  public NodePropertiesView getViewFor(UiNodeType type) {
    assert (type != null);
    NodePropertiesView view = nodePropertiesViews.get(type);
    if (view == null) {
      try {
        Class<NodePropertiesView> clazz = type.getPropertiesView();
        view = clazz.newInstance();
        nodePropertiesViews.put(type, view);
      } catch (InstantiationException e) {
        log.error(e);
      } catch (IllegalAccessException e) {
        log.error(e);
      }
    }

    return view;
  }

  /**
   * Returns a free properties dialog.
   * @param explorer explorer owning the dialog.
   * @return the node properties dialog.
   */
  public NodePropertiesDialog getPropertiesDialog(Explorer explorer) {
    return new NodePropertiesDialog(explorer.getFrame());
  }

  /**
   * Returns the node views defined for the given explorer node. If the type of
   * the node has no associated views, an empty list is returned.
   * @param node explorer node to display in the view.
   * @return list of node views for the expected type.
   * @pre node != null
   */
  public List<NodeView> getNodeViews(ExplorerNode node) {
    assert node != null;
    List<NodeView> views = new ArrayList<NodeView>();
    UiNodeType nodeType = node.getNodeType();
    for (UiNodeViewType viewType : nodeType.getViewTypes()) {
      NodeView view = getViewFor(nodeType, viewType);
      if (view == null) {
        Class viewClass = viewType.getNodeView();
        Object[] params = { nodeType, viewType.getParameters() };
        view = (NodeView) ReflectUtilities.createInstance(viewClass, types,
            params);
        view.setName(viewType.getName());
        addView(nodeType, viewType, view);
      }
      if (view != null) {
        views.add(view);
        view.setModel(node);
      }
    }
    return views;
  }

  /**
   * Returns the node view defined in the node view type for the given explorer
   * node.
   * @param node explorer node to display in the view.
   * @param type user interface node view type describing the view.
   * @return instance of the expected type.
   * @pre (node != null) && (type != null)
   */
  public NodeView getNodeViewFor(ExplorerNode node, UiNodeViewType type) {
    assert (node != null) && (type != null);
    NodeView view = getViewFor(node.getNodeType(), type);
    if (view == null) {
      Class viewClass = type.getNodeView();
      Object[] params = { node.getNodeType(), type.getParameters() };
      view = (NodeView) ReflectUtilities.createInstance(viewClass, types,
          params);
      addView(node.getNodeType(), type, view);
    }
    if (view != null) {
      view.setModel(node);
    } else if (log.isErrorEnabled()) {
      log.error("view not available for node '" + node.getName() + "'");
    }
    return view;
  }

  /**
   * Returns the view for the node type and type view.
   * @param nodeType type of user interface node to display.
   * @param viewType the requested view type.
   * @return the requested view if found otherwise null.
   * @pre (nodeType != null) && (viewType != null)
   */
  private NodeView getViewFor(UiNodeType nodeType, UiNodeViewType viewType) {
    assert (nodeType != null) && (viewType != null);
    NodeView view = null;
    Map<UiNodeViewType, NodeView> views = nodeViews.get(nodeType);
    if (views != null) {
      view = views.get(viewType);
    }
    return view;
  }

  /**
   * Adds the view to the node views buffer structure.
   * @param nodeType type of the user interface node displayed in the view.
   * @param viewType the view type of the view.
   * @param view the node view to add.
   */
  private void addView(UiNodeType nodeType, UiNodeViewType viewType,
      NodeView view) {
    Map<UiNodeViewType, NodeView> views = nodeViews.get(nodeType);
    if (views == null) {
      views = new HashMap<UiNodeViewType, NodeView>();
      nodeViews.put(nodeType, views);
    }
    views.put(viewType, view);
  }
}