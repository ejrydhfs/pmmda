/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ch.bbv.explorer.Explorer;

/**
 * The class provides a default implementation for the explorer global action.
 * Global actions are available at application level and are often part of the
 * menu bar.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class ExplorerGlobalActionImpl extends AbstractAction
  implements ExplorerGlobalAction {
  /** Name of the global action.
   */
  private String name;
  
  /** Explorer owning the global action.
   */
  private Explorer explorer;

  /** Constructor of the class.
   * @param name name of the action.
   */
  public ExplorerGlobalActionImpl(String name) {
    super(name);
    setEnabled(true);
  }

	/** Returns the name of the global action.
	 * @return name of the action.
	 */
  public String getName() {
    return name;
  }

	/** Returns the explorer owning the global action.
	 * @return the owning explorer if the action is registered otherwise null.
	 * @see #setExplorer
	 */
  public Explorer getExplorer() {
    return explorer;
  }

	/** Sets the explorer owning the global action.
	 * @param explorer new explorer owning the global action.
	 * @see #getExplorer
	 */
  public void setExplorer(Explorer explorer) {
    this.explorer = explorer;
  }

	/** Initializes the global action.
	 */
  public void initialize() {
  }
  
	/** 
	 * Performs the action associated with the event.
	 * @param event event to process.
	 */
	public void actionPerformed(ActionEvent event) {
		execute();
	}
}
