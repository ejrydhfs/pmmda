/*
 * copyright MarcelBaumann (Apache Sofware License 1.1)
 * $Id$ 
 */
package ch.bbv.mda.ui.actions;

import java.awt.Cursor;

import javax.swing.JOptionPane;

import ch.bbv.application.Locator;
import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.validation.ValidationRule;
import ch.bbv.dog.validation.ValidationRuleResults;
import ch.bbv.dog.validation.Validator;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerGlobalActionImpl;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.ui.MdaExplorer;
import ch.bbv.utilities.ResourceUtilities;

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
   * Name of the action as displayed in the menu.
   */
  private static final String ACTION = "Generate";

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
    getExplorer().getFrame().setCursor(
        Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    MetaModel model = (MetaModel) getExplorer().getRoot().getDataObject();
    CartridgeFactory factory = CartridgeFactory.getInstance();
    if (model != null) {
      Validator validator = (Validator) Locator.getInstance().getComponent(
          Validator.NAME);
      ValidationRuleResults results = new ValidationRuleResults();
      AbstractVisitorFunctor visitor = DataObjectMgr.getVisitorFor(model
          .getClass());
      validator.validate(model, visitor, results);
      ((MdaExplorer) getExplorer()).getValidationPanel().setResults(results);
      if (false && results.getCount(ValidationRule.ERROR) > 0) {
        JOptionPane.showMessageDialog(getExplorer().getFrame(),
            ResourceUtilities.getString("mda.ui.errorMsg.MODEL_HAS_ERRORS"),
            ResourceUtilities.getString("mda.ui.errorMsg.ERROR"),
            JOptionPane.ERROR_MESSAGE);
        getExplorer().getFrame().setCursor(
            Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        lastResult = false;
        return lastResult;
      }

      factory.execute(cartridge, model);
    }
    getExplorer().getFrame().setCursor(
        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
    buffer.append(ACTION).append(" ").append(cartridge.getPrefix());
    return buffer.toString();
  }
}