/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
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

  private Map nodeViews;

  private IconFactory iconFactory;

  /**
   * Default constructor of the class.
   */
  public UiNodeTypeFactory() {
    nodeTypesForBo = new HashMap();
    nodeTypesForLw = new HashMap();
    nodePropertiesViews = new HashMap();
    nodeViews = new HashMap();
    iconFactory = new IconFactoryImpl();
  }

  /**
   * Returns the icon factory of the node type factory.
   * @return the icon factory.
   */
  public IconFactory getIconFactory() {
    return iconFactory;
  }

  public void register(UiNodeType type) {
    assert (type != null);
    ((UiNodeTypeImpl) type).setFactory(this);
    nodeTypesForBo.put(type.getDataObjectType().getBoClass(), type);
    nodeTypesForLw.put(type.getDataObjectType().getLwClass(), type);
  }

  public UiNodeType getNodeTypeForLw(Class clazz) {
    assert (clazz != null);
    return (UiNodeType) nodeTypesForLw.get(clazz);
  }

  public UiNodeType getNodeTypeForBo(Class clazz) {
    assert (clazz != null);
    return (UiNodeType) nodeTypesForBo.get(clazz);
  }

  /**
   * Returns the properties view associated with the given node type. The
   * algorithm assumes that at most one properties view is displayed per
   * application.
   * @param nodeType node type which properties view is requested.
   * @return the requested properties view for the given node type.
   */
  public NodePropertiesView getViewFor(UiNodeType nodeType) {
    assert (nodeType != null);

    NodePropertiesView view = (NodePropertiesView) nodePropertiesViews
        .get(nodeType);
    if (view == null) {
      try {
        Class clazz = nodeType.getPropertiesView();
        view = (NodePropertiesView) clazz.newInstance();
        nodePropertiesViews.put(nodeType, view);
      } catch (InstantiationException ie) {
        ie.printStackTrace();
      } catch (IllegalAccessException iae) {
        iae.printStackTrace();
      }
    }

    return view;
  }

  public NodePropertiesDialog getPropertiesDialog() {
    return new NodePropertiesDialog();
  }

  /**
   * Returns the node view defined in the node view type for the given
   * explorer node.
   * @param node explorer node to display in the view.
   * @param type user interface node view type describing the view.
   * @return instance of the expected type.
   */
  public NodeView getNodeViewFor(ExplorerNode node, UiNodeViewType type) {
    assert (node != null && type != null);
    Class viewClass = type.getNodeView();
    NodeView view = (NodeView) nodeViews.get(viewClass);
    if (view == null) {
      Object[] params = { node.getNodeType(), type.getParameters() };
      view = (NodeView) ReflectUtilities.createInstance(viewClass, types,
          params);
      nodeViews.put(viewClass, view);
    }
    if (view != null) {
      view.setModel(node);
    }
    if (log.isErrorEnabled()) {
      log.error("view not available for type " + type.getName());
    }
    return view;
  }
}