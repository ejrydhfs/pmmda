/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.actions;

import java.awt.event.ActionEvent;

/**
 * <p>
 * The save action for the user interface of <strong>pmMDA </strong>
 * application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerNewAction extends ExplorerGlobalActionImpl {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Default constructor.
   */
  public ExplorerNewAction() {
    super("New");
    initialize();
  }

  public void actionPerformed(ActionEvent action) {
    execute();
  }

  public void execute() {
    getExplorer().shouldClose();
  }
}