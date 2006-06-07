/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.actions;

import java.awt.event.ActionEvent;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerDocument;
import ch.bbv.explorer.resources.Messages;

/**
 * The save action for the user interface of application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class ExplorerSaveAction extends ExplorerGlobalActionImpl {
  /**
   * Constructor of the instances of the class.
   * @param explorer explorer owning the global action
   */
  public ExplorerSaveAction(Explorer explorer) {
    super(Messages.actionSave, explorer);
  }

  @Override
  public void actionPerformed(ActionEvent action) {
    execute();
    ExplorerDocument document = getExplorer().getDocument();
    document.setModified(false);
    getExplorer().getMenuBar().documentChanged(document);
  }
}
