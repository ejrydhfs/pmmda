/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.ui.actions;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerNewAction;
import ch.bbv.pim.ui.PimDocument;
import ch.bbv.pim.ui.PimExplorer;

/**
 * The new action creates a new MDA model in the user interface. No file is
 * associated with the new model.
 * @author marcelbaumann
 * @version $Revision$
 */
public class NewAction extends ExplorerNewAction {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = -1996638957784362370L;

  /**
   * Constructor of the class.
   * @param explorer explorer owning the global action
   */
  public NewAction(Explorer explorer) {
    super(explorer);
  }

  public boolean execute() {
    PimExplorer explorer = (PimExplorer) getExplorer();
    PimDocument document = new PimDocument();
    explorer.setDocument(document);
    return true;
  }
}