/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui.actions;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.explorer.StatusBar;
import ch.bbv.explorer.actions.ExplorerGlobalActionImpl;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;

/**
 * Generate the artifacts for all registered and active catridges.
 * @author AdrianBachofen
 */
public class GenerateAllAction extends ExplorerGlobalActionImpl {

  /**
   * Default constructor.
   */
  public GenerateAllAction() {
    super("Generate All");
    initialize();
  }

  /**
   * Executes all cartridges available in a row. Sort order as found.
   * @see ch.bbv.explorer.actions.ExplorerGlobalAction#execute()
   */
  public void execute() {
    MetaModel model = (MetaModel) getExplorer().getRoot().getDataObject();
    CartridgeFactory factory = CartridgeFactory.getInstance();
    if (model != null) {
      List cartridges = new ArrayList(factory.getCartridges());
      StatusBar statusBar = getExplorer().getStatusBar();
      for (int i =0; i < cartridges.size(); i++) {
        Cartridge cartridge = (Cartridge)cartridges.get(i);
        statusBar.setInfo("Generate" + cartridge.getPrefix());
        statusBar.setProgress(i / cartridges.size() * 100);
        factory.execute(cartridge, model);
      }
      statusBar.setProgress(100);
      statusBar.setInfo("Generate All done");
      statusBar.setProgress(0);
    }
  }
}