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
 * @author AdrianBachofen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenerateAllAction extends ExplorerGlobalActionImpl {

	/** Default constructor.
	 */
  public GenerateAllAction() {
    super("Generate All");
    initialize();
  }

	/** Executes all cartridges available in a row. Sort order as found.
	 * @see ch.bbv.explorer.actions.ExplorerGlobalAction#execute()
	 */
	public void execute() {
		MetaModel model = (MetaModel)getExplorer().getRoot().getDataObject();
		CartridgeFactory factory = CartridgeFactory.getInstance();
		if (model != null) {
		    Iterator cartridges = CartridgeFactory.getInstance().getCartridges().iterator();
		    while (cartridges.hasNext()) {
		    	factory.execute((Cartridge)cartridges.next(), model);
		    }
		}
	}

}
