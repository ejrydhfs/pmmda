/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.actions;

import ch.bbv.explorer.Explorer;

/**
 * The save action for the user interface of <strong>pmMDA </strong>
 * application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerExitAction extends ExplorerGlobalActionImpl {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Default constructor.
   */
  public ExplorerExitAction() {
    super("Exit");
    initialize();
  }

  public void execute() {
    Explorer explorer = getExplorer();
    explorer.close();
  }
}
