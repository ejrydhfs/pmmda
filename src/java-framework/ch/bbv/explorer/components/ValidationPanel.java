/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.components;

import java.util.Collections;
import java.util.List;

/**
 * The validation panel displays a list of validation messages. The messages are
 * displayed in a table.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ValidationPanel extends TablePanel {
  /**
   * List of displayed validation results.
   */
  private List results;

  /**
   * Name of the columns of the validation panel.
   */
  public static final TableCell.Cell[] CELLS;

  static {
    CELLS = new TableCell.Cell[2];
    CELLS[0] = new TableCell.Cell("severityText", "severity", Integer.TYPE, false);
    CELLS[1] = new TableCell.Cell("text", "text", String.class, false);
  }
  
  /**
   * Default constructor of the class.
   */
  public ValidationPanel() {
    super(Collections.EMPTY_LIST, new TableCell(CELLS));
  }

  /**
   * Sets the validation results in the panel. The previous values are
   * discarded.
   * @param results list of validation results to display.
   */
  public void setResults(List results) {
    this.results = results;
    setModel(results);
  }

  /**
   * Returns true if the panel contains validation messages.
   * @return true if messages are available otherwise false.
   */
  public boolean hasResults() {
    return ((results != null) && (results.size() > 0));
  }
}