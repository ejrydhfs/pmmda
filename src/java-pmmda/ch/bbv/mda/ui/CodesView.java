/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui;

import java.util.Collections;
import java.util.Date;

import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.NodeView;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.components.TableCell;
import ch.bbv.explorer.components.TablePanel;

/**
 * The reference code values view displays all code values of a reference code
 * type. The code values are displayed as a list of values.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CodesView extends TablePanel implements NodeView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Cells of the columns of the code view tuples.
   */
  private static final TableCell.Cell[] CELLS;

  static {
    CELLS = new TableCell.Cell[7];
    CELLS[0] = new TableCell.Cell("name", "name", String.class, true);
    CELLS[1] = new TableCell.Cell("id", "id", String.class, true);
    CELLS[2] = new TableCell.Cell("order", "order", String.class, true);
    CELLS[3] = new TableCell.Cell("short description", "shortDescription",
        String.class, true);
    CELLS[4] = new TableCell.Cell("description", "description", String.class,
        true);
    CELLS[5] = new TableCell.Cell("start date", "startDate", Date.class, true);
    CELLS[6] = new TableCell.Cell("end date", "endDate", Date.class, true);
  }

  /**
   * Creates an instance of the class.
   * @param type user interface type of the nodes to display.
   * @param object parameters list for the constructor.
   * @return a new instance of the class.
   * @pre type != null
   */
  public static CodesView create(UiNodeType type, Object object) {
    return new CodesView();
  }

  /**
   * Constructor of the class.
   */
  public CodesView() {
    super(Collections.EMPTY_LIST, new TableCell(CELLS));
  }

  public void nodeChanged(ExplorerNode node) {
  }

  public void nodeInserted(ExplorerNode node, int index) {
  }

  public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
  }

  public void setModel(ExplorerNode node) {
    // setModel(node);
  }

  public void viewClosed() {
  }
}
