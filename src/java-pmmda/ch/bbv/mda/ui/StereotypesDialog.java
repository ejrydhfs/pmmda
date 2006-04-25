/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui;

import java.util.List;

import javax.swing.JFrame;

import ch.bbv.explorer.components.ListsTransferPanel;
import ch.bbv.explorer.components.SimpleDialog;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.Stereotype;

public class StereotypesDialog extends SimpleDialog {
  MetaItemView view;
  
  private ListsTransferPanel panel;

  /**
   * Constructor of the class.
   * @param owner the frame owning the dialog
   */
  public StereotypesDialog(JFrame owner) {
    super(owner, true, false);
    panel = new ListsTransferPanel();
  }

  public void populate(MetaItemView view, List<Stereotype> stereotypes, 
      MetaElement element, boolean allowDuplicates) {
    this.view = view;
    panel.setLists(element.getModel().getStereotypes(), stereotypes, allowDuplicates);
    setView(panel);
  }

  @Override
  protected void processOk() {
     view.updateStereotypes((List<Stereotype>)panel.getItems());
  }
}
