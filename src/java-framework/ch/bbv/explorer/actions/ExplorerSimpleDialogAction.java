/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.explorer.actions;

import javax.swing.JFrame;

import ch.bbv.explorer.Explorer;
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
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  private static final Class[] TYPES = { JFrame.class };

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
   * Constructor of the instances of the class.
   * @param name name of the action.
   * @param clazz simple dialog class of the dialog to be displayed.
   * @param explorer explorer owning the global action.
   */
  public ExplorerSimpleDialogAction(String name, Class clazz, Explorer explorer) {
    super(name, explorer);
    this.clazz = clazz;
  }

  public boolean execute() {
    if (dialog == null) {
      Object[] objects = { getExplorer().getFrame() };
      dialog = (SimpleDialog) ReflectUtilities.newInstance(clazz, TYPES,
          objects);
    }
    dialog.setVisible(true);
    return true;
  }
}