/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPopupMenu;

/**
 * The popup factory is a default implementation of the popup factory interface.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class PopupFactoryImpl implements PopupFactory {
  /**
   * List of all registered selectors.
   */
  private Map selectors;

  /**
   * Default constructor of the class.
   */
  public PopupFactoryImpl() {
    selectors = new HashMap();
  }

  public void registerPopupSelector(UiNodeType nodeType, PopupSelector selector) {
    assert ((nodeType != null) && (selector != null));
    selectors.put(nodeType, selector);
  }

  public JPopupMenu getMenu(ExplorerNode node) {
    assert (node != null);
    JPopupMenu menu = null;
    PopupSelector selector = (PopupSelector) selectors.get(node.getNodeType());
    if (selector != null) {
      menu = selector.select(node);
    }
    return menu;
  }
}