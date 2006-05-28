/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer;

import java.util.Collection;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import ch.bbv.application.Locator;
import ch.bbv.dog.validation.Validator;
import ch.bbv.explorer.actions.RemoveNodeAction;
import ch.bbv.explorer.nodeactions.*;

/**
 * The default popup selector provides standard popup menus for node types.
 * @author Marcel Baumann
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
   * Validation action for the node type.
   */
  private AbstractNodeAction validate;

  /**
   * Specific actions for the node type.
   */
  private List<AbstractNodeAction> actions;

  /**
   * Constructor of the class. A default validation action and no node specific
   * actions are created.
   * @param type user interface node type the selector is associated to
   * @pre type != null
   */
  public PopupSelectorDefault(UiNodeType type) {
    this(type, null, null);
  }

  /**
   * Constructor of the class.
   * @param type user interface node type the selector is associated to
   * @param validate the validate action to use. If null the default validate
   *          action is created under the condition that a validator component
   *          is defined in the application
   * @param actions specific actions to the user interface node type
   * @pre type != null
   */
  public PopupSelectorDefault(UiNodeType type, AbstractNodeAction validate,
      List<AbstractNodeAction> actions) {
    assert type != null;
    this.type = type;
    if (validate == null) {
      Validator validator = (Validator) Locator.getInstance().getComponent(
          Validator.NAME);
      if (validator != null) {
        this.validate = new ValidateNodeAction(type, "Validate", validator);
      }
    } else {
      this.validate = validate;
    }
    this.actions = actions;
  }

  public JPopupMenu select(ExplorerNode node) {
    assert node.getNodeType() == type;
    if (menu == null) {
      UiNodeType type = node.getNodeType();
      menu = new JPopupMenu();
      menu.add(new DisplayNodeAction(type, "View"));
      if (type.isEditable(null)) {
        menu.add(new EditNodeAction(type, "Edit"));
      }
      Collection<UiRelation> relations = type.getRelations();
      if (relations.size() > 0) {
        menu.addSeparator();
        UiNodeTypeFactory factory = type.getFactory();
        JMenu createMenu = new JMenu("Add...");
        for (UiRelation relation : relations) {
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
   * @param type node tzpe for which the root node actions shold be created
   * @return the menu of actions available to a root node
   */
  private JMenu createRootNodeActions(UiNodeType type) {
    JMenu actionsMenu = new JMenu("Actions...");
    actionsMenu.add(new ExpandNodeAction(type, "Expand All"));
    actionsMenu.add(new CollapseNodeAction(type, "Collapse All"));
    if (validate != null) {
      actionsMenu.add(validate);
    }
    if ((actions != null) && (actions.size() > 0)) {
      menu.addSeparator();
      for (AbstractNodeAction action : actions) {
        actionsMenu.add(action);
      }
    }
    menu.addSeparator();
    actionsMenu.add(new SaveNodeAction(type, "Save"));
    actionsMenu.add(new RevertNodeAction(type, "Revert"));
    actionsMenu.add(new ReloadNodeAction(type, "Reload"));
    return actionsMenu;
  }
}