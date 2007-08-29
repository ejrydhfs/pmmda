/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id: 
 */
package ch.bbv.mda.ui;

import javax.swing.JPopupMenu;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.PopupSelector;
import ch.bbv.explorer.UiNodeType;

/**
 * Provides pop-up menus within the MetaViewView dialog (and possibly other).
 * Currently it just assures that there are no menu elements available.
 * @author AdrianBachofen
 *
 */
public class MetaViewPopupSelector implements PopupSelector {

  /**
   * The popup menu of the selector. The menu is computed lazy.
   */
  private JPopupMenu menu;

  /**
   * The user interface node type to which the selector is associated to.
   */
  private UiNodeType type;

  /**
   * Constructor of the class. A default validation action and no node specific
   * actions are created.
   * @param type user interface node type the selector is associated to
   * @pre type != null
   */
  public MetaViewPopupSelector(UiNodeType type) {
    assert type != null;
    this.type = type;
  }

  public JPopupMenu select(ExplorerNode node) {
    assert node.getNodeType() == type;
    if (menu == null) {
      menu = new JPopupMenu();
    }
    return menu;
  }

}
