/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import ch.bbv.dnm.users.client.Application;
import ch.bbv.dog.validation.Validator;
import ch.bbv.explorer.actions.RemoveNodeAction;
import ch.bbv.explorer.nodeactions.*;

/**
 * The default popup selector provides standard popup menus for node types.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class PopupSelectorDefault implements PopupSelector {
  /**
   * The popup menu of the selector. The menu is computed lazy.
   */
  private JPopupMenu menu;

  /**
   * The user interface node type to which the selector is associated to.
   */
  private UiNodeType type;

  /**
   * Constructor of the class.
   * @param type user interface node type to which the selector is associated
   *        to.
   */
  public PopupSelectorDefault(UiNodeType type) {
    this.type = type;
  }

  public JPopupMenu select(ExplorerNode node) {
    assert (node.getNodeType() == type);
    if (menu == null) {
      UiNodeType type = node.getNodeType();
      menu = new JPopupMenu();
      menu.add(new DisplayNodeAction(type, "View"));
      if (type.isEditable(null)) {
        menu.add(new EditNodeAction(type, "Edit"));
      }
      List views = type.getViews();
      if (views.size() > 0) {
        JMenu viewMenu = new JMenu("Views...");
        for (Iterator i = views.iterator(); i.hasNext();) {
          UiNodeViewType view = (UiNodeViewType) i.next();
          viewMenu.add(new DisplayNodeAction(type, view.getName()));
        }
        menu.add(viewMenu);
      }

      Collection relations = type.getRelations();
      if (relations.size() > 0) {
        menu.addSeparator();

        UiNodeTypeFactory factory = type.getFactory();
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
        menu.add(createMenu);
      }
      if (type.getDataObjectType().isRoot()) {
        menu.add(createRootNodeActions(type));
      }
      if (type.isStructureEditable(null)) {
        menu.add(new RemoveNodeAction(type, "Remove"));
      }
    }
    return menu;
  }

  /**
   * Creates the menu of actions for a root node.
   * @param type node tzpe for which the root node actions shold be created.
   * @return the menu of actions available to a root node.
   */
  private JMenu createRootNodeActions(UiNodeType type) {
    JMenu actions = new JMenu("Actions...");
    actions.add(new ExpandNodeAction(type, "Expand All"));
    actions.add(new CollapseNodeAction(type, "Collapse All"));
    actions.add(new ValidateNodeAction(type, "Validate",
        (Validator) Application.getInstance().getComponent(Validator.NAME)));
    menu.addSeparator();
    actions.add(new SaveNodeAction(type, "Save"));
    actions.add(new RevertNodeAction(type, "Revert"));
    actions.add(new ReloadNodeAction(type, "Reload"));
    return actions;
  }
}