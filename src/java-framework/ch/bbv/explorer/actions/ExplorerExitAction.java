/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.actions;


/**
 * The save action for the user interface of <strong>pmMDA</strong> application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerExitAction extends ExplorerGlobalActionImpl {
	/** Default constructor.
	 */
  public ExplorerExitAction() {
    super("Exit");
    initialize();
  }

  public void execute() {
		getExplorer().shouldClose();
  }
}
