/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.about;

import java.util.List;

import ch.bbv.application.Library;
import ch.bbv.explorer.components.TableCell;
import ch.bbv.explorer.components.TablePanel;
import ch.bbv.explorer.properties.Messages;

/**
 * A panel containing a table that lists the libraries used in a project.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class LibraryPanel extends TablePanel {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

 
  /**
   * Constructs a library panel.
   * @param libraries a list of libraries
   */
  public LibraryPanel(List<Library> libraries) {
    super(libraries, new TableCell(Messages.librariesColumnNames));
  }
}