/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.ui;

import ch.bbv.explorer.components.TableCell;
import ch.bbv.explorer.components.TablePanel;
import ch.bbv.mda.generators.CartridgeFactory;

/**
 * The cartridges panel displays information about all the cartridges known in
 * the application. It implements a view on a list of cartridge descriptors. The
 * columns are read-only with the exception of the active and loaded columns.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CartridgesPanel extends TablePanel {
  /**
   * Name of the columns of the cartridge information tuples.
   */
  private static final String[] COLUMN_NAMES = { "name", "prefix", "version",
      "loaded", "active", "classname" };

  private static final boolean[] ACTIVE_COLUMNS = { false, false, false, false,
      true, false };

  /**
   * Constructor of the class.
   * @param factory cartridge factory.
   */
  public CartridgesPanel(CartridgeFactory factory) {
    super(factory.getCartridgeDescriptors(), new TableCell(COLUMN_NAMES,
        COLUMN_NAMES, ACTIVE_COLUMNS));
  }
}