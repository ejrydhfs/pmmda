/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui.actions;

import ch.bbv.explorer.*;
import ch.bbv.explorer.actions.ExplorerGlobalActionImpl;
import ch.bbv.mda.MetaFactory;
import ch.bbv.mda.MetaModel;

/**
 * The new action creates a new MDA model in the user interface. No file is
 * associated with the new model.
 * @author marcelbaumann
 * @version $Revision$
 */
public class NewAction extends ExplorerGlobalActionImpl {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Default constructor of the class.
   */
  public NewAction() {
    super("New");
  }

  public void execute() {
    Explorer explorer = getExplorer();
    UiNodeTypeFactory factory = explorer.getNodeTypeFactory();
    explorer.getMenuBar().documentCreated();
    UiNodeType nodeType = factory.getNodeTypeForBo(ch.bbv.mda.MetaModel.class);
    MetaModel model = new MetaModel();
    MetaFactory.getInstance().initialize(model);
    model.setName("default");
    ExplorerNode root = new ExplorerNode(nodeType, model, model);
    explorer.setRoot(root);
    explorer.getMenuBar().documentCreated();
  }

}