/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * License. $Id: UiNodeTypeFactory.java,v 1.1 2004/08/10 17:36:00 marcelbaumann
 * Exp $
 */
package ch.bbv.explorer;

import java.util.HashMap;
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
public class UiNodeTypeFactory {
  /**
   * The types of the create method signature to instantiate a new node view.
   */
  private static final Class[] types = { UiNodeType.class, Object.class };

  /**
   * Logger for all instance of the class.
   */
  private static Log log = LogFactory.getLog(UiNodeTypeFactory.class);

  private Map nodeTypesForBo;

  private Map nodeTypesForLw;

  private Map nodePropertiesViews;

  /**
   * The node views map contains all the views of the registered node types. The
   * node views is Map <UiNodeType, Map <UiNodeViewType, NodeView> >.
   */
  private Map nodeViews;

  /**
   * The icon factory of the node type factory used to retrieve node icons.
   */
  private IconFactory iconFactory;

  private PopupFactory popupFactory;

  /**
   * Default constructor of the class.
   */
  public UiNodeTypeFactory() {
    nodeTypesForBo = new HashMap();
    nodeTypesForLw = new HashMap();
    nodePropertiesViews = new HashMap();
    nodeViews = new HashMap();
    iconFactory = new IconFactoryImpl();
    popupFactory = new PopupFactoryImpl();
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
    return (UiNodeType) nodeTypesForLw.get(clazz);
  }

  /**
   * Returns the node type associated with the given data object class.
   * @param clazz class of the data object.
   * @return the user interface node type if found otherwise null.
   * @pre clazz != null
   */
  public UiNodeType getNodeTypeForBo(Class clazz) {
    assert (clazz != null);
    return (UiNodeType) nodeTypesForBo.get(clazz);
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

    NodePropertiesView view = (NodePropertiesView) nodePropertiesViews
        .get(type);
    if (view == null) {
      try {
        Class clazz = type.getPropertiesView();
        view = (NodePropertiesView) clazz.newInstance();
        nodePropertiesViews.put(type, view);
      } catch (InstantiationException ie) {
        ie.printStackTrace();
      } catch (IllegalAccessException iae) {
        iae.printStackTrace();
      }
    }

    return view;
  }

  /**
   * Returns a free properties dialog.
   * @return the node properties dialog.
   */
  public NodePropertiesDialog getPropertiesDialog() {
    return new NodePropertiesDialog();
  }

  /**
   * Returns the node view defined in the node view type for the given explorer
   * node.
   * @param node explorer node to display in the view.
   * @param type user interface node view type describing the view.
   * @return instance of the expected type.
   */
  public NodeView getNodeViewFor(ExplorerNode node, UiNodeViewType type) {
    assert (node != null && type != null);
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
   */
  private NodeView getViewFor(UiNodeType nodeType, UiNodeViewType viewType) {
    NodeView view = null;
    Map views = (Map) nodeViews.get(nodeType);
    if (views != null) {
      view = (NodeView) views.get(viewType);
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
    Map views = (Map) nodeViews.get(nodeType);
    if (views == null) {
      views = new HashMap();
      nodeViews.put(nodeType, views);
    }
    views.put(viewType, view);
  }
}