/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id: 
 */
package ch.bbv.mda.ui.metaview;

import javax.swing.JPopupMenu;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.PopupSelector;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.nodeactions.AddNodeAction;
import ch.bbv.mda.MetaClass;

/**
 * Provides pop-up menus within the MetaViewView dialog (and possibly other).
 * It provides pop up menus to move classes from the model to the view visualization
 * @author AdrianBachofen
 *
 */
public class MetaModelPopupSelector implements PopupSelector {

  /**
   * The popup menu of the selector. The menu is computed lazy.
   */
  private JPopupMenu menu;

  /**
   * The user interface node type to which the selector is associated to.
   */
  private UiNodeType type;

  /**
   * Contains the metaViewView for the pop-up menus to operate on
   */
  private MetaViewView metaViewView;
  
  /**
   * Constructor of the class. A default validation action and no node specific
   * actions are created.
   * @param type user interface node type the selector is associated to
   * @param metaViewView view where pop-up operations have to be performed on.
   * @pre type != null
   */
  public MetaModelPopupSelector(UiNodeType type, MetaViewView metaViewView) {
    assert type != null;
    this.type = type;
    this.metaViewView = metaViewView;
  }

  public JPopupMenu select(ExplorerNode node) {
    assert node.getNodeType() == type;
    menu = new JPopupMenu();
    if(node.getDataObject() instanceof MetaClass) {
      menu.add(new AddToViewNodeAction(node.getNodeType(), "Add to View", metaViewView));
    }
    return menu;
  }

}
