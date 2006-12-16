/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui.actions;

import javax.swing.JOptionPane;

import ch.bbv.application.Locator;
import ch.bbv.dog.validation.Validator;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerGlobalActionImpl;
import ch.bbv.explorer.nodeactions.ValidateNodeAction;
import ch.bbv.explorer.resources.Messages;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.resources.MessagesMda;

import com.jgoodies.validation.ValidationResult;

/**
 * The generate data object action generates the data objects for the model
 * displayed in the explorer. The cartdridge is instantiated, initialized and
 * executed. If the validation of the model detects errors a warning message is
 * displayed.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class GenerateCartridgeAction extends ExplorerGlobalActionImpl {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Cartridge associated with the action.
   */
  private Cartridge cartridge;

  /**
   * Constructor of the class.
   * @param cartridge cartridge which should be called
   * @param explorer explorer owning the global action
   */
  public GenerateCartridgeAction(Cartridge cartridge, Explorer explorer) {
    super(createActionName(cartridge), explorer);
    this.cartridge = cartridge;
  }

  public boolean execute() {
    switchWaitCursor(true);
    MetaModel model = (MetaModel) getExplorer().getRoot().getDataObject();
    if (model != null) {
      CartridgeFactory factory = CartridgeFactory.getInstance();
      if (factory.shouldValidate()) {
        Validator validator = (Validator) Locator.getInstance().getComponent(
            Validator.NAME);
        ValidateNodeAction.validateTree(model, validator, getExplorer());
        if (getExplorer().getValidationPanel().getResults().hasErrors()) {
          JOptionPane.showMessageDialog(getExplorer().getFrame(),
              MessagesMda.msgModelhasErrors, Messages.titleError,
              JOptionPane.ERROR_MESSAGE);
          switchWaitCursor(false);
          lastResult = false;
          return lastResult;
        }
      }
      ValidationResult messages = getExplorer().getValidationPanel().getResults();
      factory.execute(cartridge, model, messages);
    }
    switchWaitCursor(false);
    lastResult = true;
    return lastResult;
  }

  /**
   * Creates the action name for the given cartridge.
   * @param cartridge cartridge which action name should be created
   * @return the name of the generation action
   */
  private static String createActionName(Cartridge cartridge) {
    StringBuilder buffer = new StringBuilder();
    buffer.append(MessagesMda.actionGenerate).append(" ").append(
        cartridge.getPrefix());
    return buffer.toString();
  }
}