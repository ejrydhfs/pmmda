/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer;

import java.awt.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;

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
  public static final String FILE_MENU = "File";

  public static final String HELP_MENU = "Help";

  public static final String EDIT_MENU = "Edit";

  private static final int NEW_DOCUMENT = 0;

  private static final int MODIFIED_DOCUMENT = 1;

  private static final int SAVED_DOCUMENT = 2;

  private int state;

  private Map menus;

  /**
   * Standard global action to manipulate documents.
   */
  private ExplorerGlobalAction newAction;

  /**
   * Standard global action to manipulate documents.
   */
  private ExplorerGlobalAction openAction;

  /**
   * Standard global action to manipulate documents.
   */
  private ExplorerGlobalAction openLastAction;

  /**
   * Standard global action to manipulate documents.
   */
  private ExplorerGlobalAction closeLastAction;

  /**
   * Standard global action to manipulate documents.
   */
  private ExplorerGlobalAction saveAction;

  /**
   * Standard global action to manipulate documents.
   */
  private ExplorerGlobalAction saveAsAction;

  /**
   * Standard global action to manipulate documents.
   */
  private ExplorerGlobalAction exitAction;

  /**
   * Default constructor of the class.
   */
  public ExplorerMenuBar() {
    menus = new HashMap();

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
    assert (name != null);
    return (JMenu) menus.get(name);
  }

  /**
   * Adds the menu at the given position in the menu bar.
   * @param menu menu to add.
   * @param index index of the menu in the menu bar.
   * @pre (menu != null) && (menu.getName() != null)
   * @see #getMenu(String)
   */
  public void add(JMenu menu, int index) {
    assert ((menu != null) && (menu.getName() != null));
    menus.put(menu.getName(), menu);
    super.add(menu, index);
  }

  public void registerDocumentFunctions(ExplorerGlobalAction newAction,
      ExplorerGlobalAction openAction, ExplorerGlobalAction openLastAction,
      ExplorerGlobalAction saveAction, ExplorerGlobalAction saveAsAction,
      ExplorerGlobalAction exitAction) {
    this.newAction = newAction;
    this.openAction = openAction;
    this.openLastAction = openLastAction;
    this.saveAction = saveAction;
    this.saveAsAction = saveAsAction;
    this.exitAction = exitAction;
    setActionEnabled(newAction, true);
    setActionEnabled(openAction, true);
    setActionEnabled(openLastAction, true);
    setActionEnabled(saveAction, false);
    setActionEnabled(saveAsAction, false);
    setActionEnabled(exitAction, true);
  }

  /**
   * Called when a document is created.
   */
  public void documentCreated() {
    state = NEW_DOCUMENT;
    setActionEnabled(saveAction, false);
    setActionEnabled(saveAsAction, true);
  }

  /**
   * Called when a document is opened.
   */
  public void documentOpened() {
    state = SAVED_DOCUMENT;
    setActionEnabled(saveAction, true);
    setActionEnabled(saveAsAction, true);
  }

  /**
   * Called when a document is saved.
   */
  public void documentSaved() {
    state = SAVED_DOCUMENT;
    setActionEnabled(saveAction, true);
    setActionEnabled(saveAsAction, true);
  }

  /**
   * Called when a document is modified.
   */
  public void documentModified() {
    state = MODIFIED_DOCUMENT;
  }

  /**
   * Sets the explorer to the menu bar and all its subcomponents.
   * @param explorer the new explorer owning the menu bar.
   */
  public void setExplorer(Explorer explorer) {
    for (Iterator i = menus.values().iterator(); i.hasNext();) {
      JMenu menu = (JMenu) i.next();
      Component[] globalNodes = menu.getMenuComponents();
      for (int j = 0; j < globalNodes.length; j++) {
        if (globalNodes[j] instanceof JMenuItem) {
          Action action = ((JMenuItem) globalNodes[j]).getAction();
          if (action instanceof ExplorerGlobalAction) {
            ((ExplorerGlobalAction) action).setExplorer(explorer);
          }
        }
      }
    }
  }

  protected void setActionEnabled(ExplorerGlobalAction action, boolean enabled) {
    if (action != null) {
      action.setEnabled(enabled);
    }
  }
}