/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.components;

import java.util.List;

/**
 * The validation panel displays a list of validation messages.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ValidationPanel extends TablePanel {
  private List results;

  /**
   * Name of the columns of the validation panel.
   */
  public static final String[] COLUMN_NAMES = { "level", "text" };

  /**
   * Default constructor of the class.
   */
  public ValidationPanel() {
    super(null, new TableCell(COLUMN_NAMES));
  }

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