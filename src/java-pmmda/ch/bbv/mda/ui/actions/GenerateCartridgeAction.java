/*
 * copyright MarcelBaumann (Apache Sofware License 1.1)
 * $Id$ 
 */
package ch.bbv.mda.ui.actions;

import ch.bbv.explorer.actions.ExplorerGlobalActionImpl;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;

/**
 * The generate data object action generates the data objects for the model
 * displayed in the explorer. The cartdridge is instantiated, initialized and
 * executed.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class GenerateCartridgeAction extends ExplorerGlobalActionImpl {
  /**
   * Name of the generate action as displayed in the menu.
   */
  protected static final String GENERATE_ACTION = "Generate";

  /**
   * Cartridge associated with the action.
   */
  private Cartridge cartridge;

  /**
   * Constructor of the class.
   * @param cartridge cartridge which should be called.
   */
  public GenerateCartridgeAction(Cartridge cartridge) {
    super(createActionName(cartridge));
    this.cartridge = cartridge;
  }

  public void execute() {
    MetaModel model = (MetaModel) getExplorer().getRoot().getDataObject();
    CartridgeFactory factory = CartridgeFactory.getInstance();
    if (model != null) {
      factory.execute(cartridge, model);
    }
  }

  /**
   * Creates tha action name for the given cartridge.
   * @param cartridge cartridge which action name should be created.
   * @return the name of the generation action.
   */
  private static String createActionName(Cartridge cartridge) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(GENERATE_ACTION).append(" ").append(cartridge.getPrefix());
    return buffer.toString();
  }
}