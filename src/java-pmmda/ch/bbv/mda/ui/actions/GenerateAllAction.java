/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui.actions;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.StatusBar;
import ch.bbv.explorer.actions.ExplorerGlobalActionImpl;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;

/**
 * Generate the artifacts for all registered and active catridges. If the
 * validation of the model detects errors a warning message is displayed.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class GenerateAllAction extends ExplorerGlobalActionImpl {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Name of the action as displayed in the menu.
   */
  private static final String ACTION = "Generate All";

  /**
   * Default constructor.
   * @param explorer explorer owning the global action.
   */
  public GenerateAllAction(Explorer explorer) {
    super(ACTION, explorer);
  }

  /**
   * Executes all cartridges available in a row. Sort order as found.
   * @see ch.bbv.explorer.actions.ExplorerGlobalAction#execute()
   */
  public boolean execute() {
    MetaModel model = (MetaModel) getExplorer().getRoot().getDataObject();
    CartridgeFactory factory = CartridgeFactory.getInstance();
    if (model != null) {
      List<Cartridge> cartridges = new ArrayList<Cartridge>(factory
          .getCartridges());
      StatusBar statusBar = getExplorer().getStatusBar();
      for (int i = 0; i < cartridges.size(); i++) {
        Cartridge cartridge = cartridges.get(i);
        statusBar.setInfo("Generate" + cartridge.getPrefix());
        statusBar.setProgress(i / cartridges.size() * 100);
        factory.execute(cartridge, model);
      }
      statusBar.setProgress(100);
      statusBar.setInfo("Generate All done");
      statusBar.setProgress(0);
    }
    lastResult = true;
    return lastResult;
  }
}