/*
 * copyright MarcelBaumann (Apache Sofware License 1.1)
 * $Id$ 
 */
package ch.bbv.mda.ui.actions;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerGlobalActionImpl;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;

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
   * @param cartridge cartridge which should be called.
   * @param explorer explorer owning the global action.
   */
  public GenerateCartridgeAction(Cartridge cartridge, Explorer explorer) {
    super(createActionName(cartridge), explorer);
    this.cartridge = cartridge;
  }

  public boolean execute() {
    MetaModel model = (MetaModel) getExplorer().getRoot().getDataObject();
    CartridgeFactory factory = CartridgeFactory.getInstance();
    if (model != null) {
      factory.execute(cartridge, model);
    }
    return true;
  }

  /**
   * Creates tha action name for the given cartridge.
   * @param cartridge cartridge which action name should be created.
   * @return the name of the generation action.
   */
  private static String createActionName(Cartridge cartridge) {
    StringBuilder buffer = new StringBuilder();
    buffer.append(ACTION).append(" ").append(cartridge.getPrefix());
    return buffer.toString();
  }
}