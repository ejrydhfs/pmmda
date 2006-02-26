/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ch.bbv.explorer.actions.ExplorerGlobalAction;

/**
 * The explorer menu defines a standard menu bar and menu items for an
 * application based on the explorer framework. The following items are defined.
 * <ul>
 * <dt>File</dt>
 * <dd>The file submenu provides a new, open, save, save as, and exit
 * functions. The open and save as use the standard file selection dialog.</dd>
 * <dt>Help</dt>
 * <dd>The help submenu provides a help and an about functions. The about
 * function has a simple splash window.</dd>
 * </ul>
 * <p>
 * A state machine is defined for the file submenu command. The usual rules
 * applies for the model related actions.
 * </p>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerMenuBar extends JMenuBar {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Name of the pull down-menu containing file related actions.
   */
  public static final String FILE_MENU = "File";

  /**
   * Name of the pull down-menu containing help related actions.
   */
  public static final String HELP_MENU = "Help";

  /**
   * Name of the pull down-menu containing edition related actions.
   */
  public static final String EDIT_MENU = "Edit";

  public static enum Names {
    NEW, OPEN, OPEN_LAST, SAVE, SAVE_AS, CLOSE, EXIT
  };

  /**
   * Maps containing all menus.
   */
  private Map<String, JMenu> menus;

  private Map<Names, ExplorerGlobalAction> actions;

  /**
   * Explorer to which the menu bar belongs.
   */
  private Explorer explorer;

  /**
   * Default constructor of the class.
   * @param explorer explorer to which the menu bar belongs.
   * @pre explorer != null
   */
  public ExplorerMenuBar(Explorer explorer) {
    this.explorer = explorer;
    menus = new HashMap<String, JMenu>();
    actions = new HashMap<Names, ExplorerGlobalAction>();
    JMenu menu = new JMenu(FILE_MENU);
    add(menu);
    menus.put(FILE_MENU, menu);
    menu = new JMenu(HELP_MENU);
    add(menu);
    menus.put(HELP_MENU, menu);
  }

  /**
   * Returns the menu associated with the name.
   * @param name name of the requested menu.
   * @return the requested menu if found otherwise null.
   * @pre name != null
   * @see #add(JMenu, int)
   */
  public JMenu getMenu(String name) {
    assert name != null;
    return menus.get(name);
  }

  /**
   * Adds the menu at the given position in the menu bar.
   * @param menu menu to add.
   * @param index index of the menu in the menu bar.
   * @pre (menu != null) && (menu.getName() != null)
   * @see #getMenu(String)
   */
  public void add(JMenu menu, int index) {
    assert (menu != null) && (menu.getName() != null);
    menus.put(menu.getName(), menu);
    super.add(menu, index);
  }

  /**
   * Registers a standard action into the menu bar.
   * @param name name of the standard action.
   * @param action standard action to register.
   * @see #getStandardAction(Names)
   */
  public void putStandardAction(Names name, ExplorerGlobalAction action) {
    actions.put(name, action);
  }

  /**
   * Returns the explorer global action with the given name.
   * @param name name of the action to retrieve.
   * @return the requested action if found otherwise null.
   * @see #putStandardAction(Names, ExplorerGlobalAction)
   */
  public ExplorerGlobalAction getStandardAction(Names name) {
    return actions.get(name);
  }

  /**
   * Adds a standard action to the given menu.
   * @param menu menu to which the action should be added.
   * @param name name of the action as defined in the menu bar.
   * @param action action to add.
   */
  public void addStandardAction(JMenu menu, Names name,
      ExplorerGlobalAction action) {
    menu.add(new JMenuItem(action));
    putStandardAction(name, action);
  }

  /**
   * Change event about the status of the current displayed document.
   * @param document the document which status changed.
   */
  public void documentChanged(ExplorerDocument document) {
    setActionEnabled(actions.get(Names.NEW), true);
    setActionEnabled(actions.get(Names.OPEN), true);
    setActionEnabled(actions.get(Names.CLOSE), true);
    setActionEnabled(actions.get(Names.EXIT), true);
    if (document != null) {
      boolean modified = document.isModified();
      boolean news = document.isNew();
      if (news) {
        explorer.getFrame().setTitle("");
      } else {
        explorer.getFrame().setTitle(
            explorer.getRoot().getName() + (modified ? " * " : ""));
      }
      setActionEnabled(actions.get(Names.SAVE), !news && modified);
      setActionEnabled(actions.get(Names.SAVE_AS), news || modified);
    } else {
      setActionEnabled(actions.get(Names.SAVE), false);
      setActionEnabled(actions.get(Names.SAVE_AS), false);      
    }
  }

  /**
   * Sets the enabled flag to the action if defined.
   * @param action action to update.
   * @param enabled new value of the enabled flag.
   */
  protected void setActionEnabled(ExplorerGlobalAction action, boolean enabled) {
    if (action != null) {
      action.setEnabled(enabled);
    }
  }
}