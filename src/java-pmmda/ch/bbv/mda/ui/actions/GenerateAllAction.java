/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui.actions;

import java.util.Iterator;

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
      for (Iterator i = factory.getCartridges().iterator(); i.hasNext();) {
        factory.execute((Cartridge) i.next(), model);
      }
    }
  }
}