/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.ui.actions;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerGlobalActionImpl;
import ch.bbv.explorer.components.SimpleDialog;
import ch.bbv.pim.ui.RelocateContactsView;

public class RelocateContactsAction extends ExplorerGlobalActionImpl {
  /**
   * Serial version identifier.
   */
  private static final long serialVersionUID = -7077680772873942481L;

  /**
   * Constructor of the class.
   * @param name name of the action
   * @param explorer explorer owning the global action
   */
  public RelocateContactsAction(String name, Explorer explorer) {
    super(name, explorer);
  }

  public boolean execute() {
    System.out.println("Called");
    SimpleDialog dialog = new SimpleDialog(getExplorer().getFrame(), false,
        false);
    RelocateContactsView view = new RelocateContactsView(getExplorer());
    dialog.setView(view);
    dialog.setVisible(true);
    return true;
  }

}
