/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.explorer;

import javax.swing.ImageIcon;

/**
 * The children icon selector selects the icon for tree nodes. The icon shows 
 * if the node has children or not. The name of the provided icons must 
 * respect the following conventions. The name of the node type is used as
 * first part of the name, the below defined strings are used to indicate 
 * if the icon is for nodes with or without children. 
 * @author marcelbaumann
 * @version $Revision$
 */
public class ChildrenIconSelector implements IconSelector {
  /**
   * Extension for the icons representing a node without children.
   */
  public static final String NO_CHILDREN = "-empty";

  /**
   * Extension for the icons representing a node with children.
   */
  public static final String WITH_CHILDREN = "-not-empty";

  /**
   * Icon factory managing all icons.
   */
  private IconFactory factory;

  /** 
   * Name of the node type which icons are managed.
   */
  private String nodeTypeName;

  /** Constructor of the class.
   * @param factory icon factory where the icons can be retrieved.
   * @param nodeTypeName name of the node type which icon should be displayed.
   */
  public ChildrenIconSelector(IconFactory factory, String nodeTypeName) {
    this.factory = factory;
    this.nodeTypeName = nodeTypeName;
  }

  public ImageIcon select(ExplorerNode node, boolean expanded) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(nodeTypeName);
    if (node.getChildCount() == 0) {
      buffer.append(NO_CHILDREN);
    } else {
      buffer.append(WITH_CHILDREN);
    }
    return factory.getSmallIconFor(buffer.toString());
  }
}