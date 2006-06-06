/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.actions;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.resources.Messages;

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
   * Constructor of the instances of the class.
   * @param explorer explorer owning the global action.
   */
  public ExplorerOpenLast(Explorer explorer) {
    super(Messages.actionOpenLast, explorer);
  }
}
