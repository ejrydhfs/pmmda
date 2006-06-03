/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui.actions;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import ch.bbv.application.Locator;
import ch.bbv.dog.validation.Validator;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.StatusBar;
import ch.bbv.explorer.actions.ExplorerGlobalActionImpl;
import ch.bbv.explorer.nodeactions.ValidateNodeAction;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.properties.MessagesMda;
import ch.bbv.utilities.ResourceUtilities;

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
   * Default constructor.
   * @param explorer explorer owning the global action
   */
  public GenerateAllAction(Explorer explorer) {
    super(MessagesMda.actionGenerateAll, explorer);
  }

  public boolean execute() {
    getExplorer().getFrame().setCursor(
        Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    MetaModel model = (MetaModel) getExplorer().getRoot().getDataObject();
    CartridgeFactory factory = CartridgeFactory.getInstance();
    if (model != null) {
      Validator validator = (Validator) Locator.getInstance().getComponent(
          Validator.NAME);
      ValidateNodeAction.validateTree(model, validator, getExplorer());
      if (getExplorer().getValidationPanel().getResults().hasErrors()) {
        JOptionPane.showMessageDialog(getExplorer().getFrame(),
            ResourceUtilities.getString("mda.ui.errorMsg.MODEL_HAS_ERRORS"),
            ResourceUtilities.getString("mda.ui.errorMsg.ERROR"),
            JOptionPane.ERROR_MESSAGE);
        getExplorer().getFrame().setCursor(
            Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        lastResult = false;
        return lastResult;
      }
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
    getExplorer().getFrame().setCursor(
        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    lastResult = true;
    return lastResult;
  }
}