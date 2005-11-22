/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.actions;

import java.awt.event.ActionEvent;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerDocument;

/**
 * The save action for the user interface of the application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class ExplorerNewAction extends ExplorerGlobalActionImpl {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Name of the action as displayed in the menu.
   */
  private static final String ACTION = "New";
  
  /**
   * Constructor of the instances of the class.
   * @param explorer explorer owning the global action.
   */
  public ExplorerNewAction(Explorer explorer) {
    super(ACTION, explorer);
  }

  public void actionPerformed(ActionEvent action) {
    if (saveChangesAndProceed()) {
      execute();
      getExplorer().getMenuBar()
          .documentChanged(ExplorerDocument.State.CREATED);
    }
  }
}