/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.actions;

import ch.bbv.explorer.components.SimpleDialog;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The simple dialog action display a simple dialog when a menu item is
 * selected.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ExplorerSimpleDialogAction extends ExplorerGlobalActionImpl {
  /**
   * Class of the simple dialog to display.
   */
  private Class clazz;

  /**
   * Buffer containing the simple dialog to display. The dialog is created using
   * lazy evaluation.
   */
  private SimpleDialog dialog;

  /**
   * Constructor of the class.
   * @param name name of the action.
   * @param clazz simple dialog class of the dialog to be displayed.
   */
  public ExplorerSimpleDialogAction(String name, Class clazz) {
    super(name);
    this.clazz = clazz;
  }

  public void execute() {
    if (dialog == null) {
      dialog = (SimpleDialog) ReflectUtilities.createInstance(clazz);
    }
    dialog.setVisible(true);
  }
}