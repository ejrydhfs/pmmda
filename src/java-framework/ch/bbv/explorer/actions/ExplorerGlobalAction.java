/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.explorer.actions;

import javax.swing.Action;

import ch.bbv.explorer.Explorer;

/**
 * The class defines a global action executed on the model displayed in the
 * explorer owning the action. Global actions are normally menu items in the
 * menu bar of the explorer.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface ExplorerGlobalAction extends Action {
  /**
   * Returns the name of the action as displayed on the user interface.
   * @return name of the action.
   */
  String getName();

  /**
   * Returns the the explorer owning the global action.
   * @return the owning explorer.
   * @see #setExplorer(Explorer)
   */
  Explorer getExplorer();

  /**
   * Sets the the explorer owning the global action.
   * @param explorer the owning explorer.
   * @see #getExplorer()
   */
  void setExplorer(Explorer explorer);

  /**
   * Executes the action.
   */
  void execute();

  /**
   * Flag indicating if the action is active for the given node.
   * @return true if the action is active otherwise false.
   */
  boolean isEnabled();

  /**
   * Initializes the global action. The method is based on the two phases
   * initialization to avoid constructor troubles.
   */
  void initialize();

}