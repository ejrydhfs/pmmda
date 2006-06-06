/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui.actions;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerNewAction;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.TreeChecker;
import ch.bbv.mda.resources.MessagesMda;
import ch.bbv.mda.ui.MdaDocument;
import ch.bbv.mda.ui.MdaExplorer;

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
  private static final long serialVersionUID = 20041201;

  /**
   * Default constructor.
   * @param explorer explorer owning the global action
   */
  public NewAction(Explorer explorer) {
    super(explorer);
  }

  public boolean execute() {
    MdaExplorer explorer = (MdaExplorer) getExplorer();
    MdaDocument document = new MdaDocument();
    MetaModel model = document.getModel();
    model.setName(MessagesMda.titleDefaultModel);
    model.setFilename("");
    model.accept(new TreeChecker());
    explorer.setDocument(document);
    lastResult = true;
    return lastResult;
  }
}