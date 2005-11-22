/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.components;

import java.util.Collections;
import java.util.List;

import ch.bbv.dog.validation.ValidationRuleResult;

/**
 * The validation panel displays a list of validation messages. The messages are
 * displayed in a table.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ValidationPanel extends TablePanel {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * List of displayed validation results.
   */
  private List<ValidationRuleResult> results;

  /**
   * Name of the columns of the validation panel.
   */
  public static final TableCell.Cell[] CELLS;

  static {
    CELLS = new TableCell.Cell[2];
    CELLS[0] = new TableCell.Cell("severityText", "severity", Integer.TYPE,
        false);
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
  public void setResults(List<ValidationRuleResult> results) {
    this.results = results;
    setModel(results);
  }

  /**
   * Clears the content of the validation panel.
   */
  public void clear() {
    results = Collections.emptyList();
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