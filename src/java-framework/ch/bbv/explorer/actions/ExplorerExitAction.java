/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.actions;

import javax.swing.JOptionPane;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerDocument;
import ch.bbv.explorer.ExplorerMenuBar;
import ch.bbv.explorer.resources.Messages;

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
   * Constructor of the instances of the class.
   * @param explorer explorer owning the global action
   */
  public ExplorerExitAction(Explorer explorer) {
    super(Messages.actionExit, explorer);
  }

  public boolean execute() {
    lastResult = true;
    Explorer explorer = getExplorer();
    ExplorerDocument document = explorer.getDocument();
    int choice = JOptionPane.YES_OPTION;
    if ((document != null) && document.isModified()) {
      choice = displayMessage(Messages.msgSaveChanges, document.getName());
      if (choice == JOptionPane.YES_OPTION) {
        if (document.isNew()) {
          explorer.getMenuBar()
              .getStandardAction(ExplorerMenuBar.Names.SAVE_AS)
              .actionPerformed(null);
        } else {
          lastResult = explorer.getMenuBar().getStandardAction(
              ExplorerMenuBar.Names.SAVE).execute();
        }
      }
    }
    if (choice != JOptionPane.CANCEL_OPTION) {
      explorer.close();
    }
    return lastResult;
  }
}
