/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.actions;

import javax.swing.JOptionPane;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerMenuBar;

/**
 * The save action for the user interface of <strong>pmMDA </strong>
 * application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerExitAction extends ExplorerGlobalActionImpl {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Name of the action as displayed in the menu.
   */
  private static final String ACTION = "Exit";

  /**
   * Constructor of the instances of the class.
   * @param explorer explorer owning the global action.
   */
  public ExplorerExitAction(Explorer explorer) {
    super(ACTION, explorer);
  }

  public boolean execute() {
    Explorer explorer = getExplorer();
    int choice;
    switch (explorer.getDocument().getState()) {
    case CREATED:
      choice = JOptionPane.showConfirmDialog(null, "Save Changes",
          "Save Changes", JOptionPane.YES_NO_OPTION);
      if (choice == JOptionPane.YES_OPTION) {
        explorer.getMenuBar().getStandardAction(ExplorerMenuBar.Names.SAVE_AS)
            .actionPerformed(null);
      }
      break;
    case MODIFIED:
      choice = JOptionPane.showConfirmDialog(null, "Save As Changes",
          "Save As Changes", JOptionPane.YES_NO_OPTION);
      if (choice == JOptionPane.YES_OPTION) {
        if (!explorer.getMenuBar()
            .getStandardAction(ExplorerMenuBar.Names.SAVE).execute()) {
        }
      }
      break;
    }
    explorer.close();
    lastResult = true;
    return lastResult;
  }
}
