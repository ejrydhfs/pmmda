/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.explorer;

import javax.swing.ImageIcon;

/**
 * The simple icon selector returns always the same icon for the nodes. The name
 * of the icon is defined at construction time.
 * @author marcelbaumann
 * @version $Revision$
 */
public class SimpleIconSelector implements IconSelector {
  /**
   * Name of the node type which icons should be managed.
   */
  private String nodeTypeName;

  /**
   * Constructor of the class.
   * @param nodeTypeName name of the node type which icons are managed.
   */
  public SimpleIconSelector(String nodeTypeName) {
    this.nodeTypeName = nodeTypeName;
  }

  public ImageIcon select(ExplorerNode node, boolean expanded) {
    IconFactory iconFactory = node.getExplorer().getNodeTypeFactory()
        .getIconFactory();
    return iconFactory.getSmallIconFor(nodeTypeName);
  }
}