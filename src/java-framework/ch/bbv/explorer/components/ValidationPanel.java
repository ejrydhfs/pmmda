/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.components;

import java.util.List;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ValidationPanel extends TablePanel {
  /**
   * Name of the columns of the validation panel.
   */
  public static final String[] COLUMN_NAMES = { "level", "text" };

  private List results;

  public ValidationPanel() {
    super(null, new TableCell(COLUMN_NAMES));
  }

  public void setResults(List results) {
    this.results = results;
    setModel(results);
  }

  public boolean hasResults() {
    return ((results != null) && (results.size() > 0));
  }
}