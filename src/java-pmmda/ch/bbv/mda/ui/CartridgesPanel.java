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
   * Cells of the columns of the cartridge information tuples.
   */
  private static final TableCell.Cell[] CELLS;

  static {
    CELLS = new TableCell.Cell[6];
    CELLS[0] = new TableCell.Cell("name", "name", String.class, false);
    CELLS[1] = new TableCell.Cell("prefix", "prefix", String.class, false);
    CELLS[2] = new TableCell.Cell("version", "version", String.class, false);
    CELLS[3] = new TableCell.Cell("loaded", "loaded", Boolean.class, false);
    CELLS[4] = new TableCell.Cell("active", "active", Boolean.class, true);
    CELLS[5] = new TableCell.Cell("classname", "classname", String.class, false);
  }

  /**
   * Constructor of the class.
   * @param factory cartridge factory.
   */
  public CartridgesPanel(CartridgeFactory factory) {
    super(factory.getCartridgeDescriptors(), new TableCell(CELLS));
  }
}