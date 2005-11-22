/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.actions;

import ch.bbv.explorer.Explorer;

/**
 * The save action for the user interface of application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class ExplorerOpenLast extends ExplorerGlobalActionImpl {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Name of the action as displayed in the menu.
   */
  private static final String ACTION = "Open Last...";

  /**
   * Constructor of the instances of the class.
   * @param explorer explorer owning the global action.
   */
  public ExplorerOpenLast(Explorer explorer) {
    super(ACTION, explorer);
  }
}
