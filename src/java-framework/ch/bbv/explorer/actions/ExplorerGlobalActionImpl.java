/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerDocument;
import ch.bbv.explorer.ExplorerMenuBar;
import ch.bbv.explorer.ExplorerMenuBar.Names;
import ch.bbv.explorer.resources.Messages;

/**
 * The class provides a default implementation for the explorer global action.
 * Global actions are available at application level and are often part of the
 * menu bar.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class ExplorerGlobalActionImpl extends AbstractAction implements
    ExplorerGlobalAction {
  /**
   * Explorer owning the global action.
   */
  private Explorer explorer;

  /**
   * The previous value of the cursor if the wait cursor was switched on
   * otherwise null.
   */
  private Cursor cursor;

  /**
   * result of the last action executed
   */
  protected boolean lastResult = false;

  /**
   * Constructor of the class.
   * @param name name of the action
   * @param explorer explorer owning the global action
   */
  public ExplorerGlobalActionImpl(String name, Explorer explorer) {
    super(name);
    this.explorer = explorer;
    setEnabled(true);
  }

  /**
   * Returns the name of the global action.
   * @return name of the action
   */
  public String getName() {
    return (String) getValue(Action.NAME);
  }

  /**
   * Returns the explorer owning the global action.
   * @return the owning explorer if the action is registered otherwise null
   * @see #setExplorer(Explorer)
   */
  public Explorer getExplorer() {
    return explorer;
  }

  /**
   * Sets the explorer owning the global action.
   * @param explorer new explorer owning the global action
   * @see #getExplorer()
   */
  public void setExplorer(Explorer explorer) {
    this.explorer = explorer;
  }

  /**
   * Performs the action associated with the event.
   * @param event event to process
   */
  public void actionPerformed(ActionEvent event) {
    lastResult = execute();
  }

  /**
   * Returns the result of the last action executed. False if no action has been
   * executed before.
   */
  public boolean getLastResult() {
    return lastResult;
  }

  /**
   * Asks the user if his changes should be saved. If yes the changes are saved.
   * The returned value states if the action was successful and the action
   * should proceed with the requested function or if the user aborted the
   * action.
   * @return flag indicating if the selected action should proceed or not
   */
  protected boolean saveChangesAndProceed() {
    boolean sucess = true;
    ExplorerDocument document = getExplorer().getDocument();
    if ((document != null) && document.isModified()) {
      ExplorerMenuBar menuBar = getExplorer().getMenuBar();
      String name = document.getName();
      if (name == null) {
        name = Messages.undefinedFilename;
      }
      int selection = displayMessage(Messages.msgSaveChanges, name);
      switch (selection) {
      case 0:
        if (document.isNew()) {
          menuBar.getStandardAction(Names.SAVE_AS).actionPerformed(null);
          sucess = menuBar.getStandardAction(Names.SAVE_AS).getLastResult();
        } else {
          sucess = menuBar.getStandardAction(Names.SAVE).execute();
        }
        if (sucess) {
          document.setNew(false);
          document.setModified(false);
          menuBar.documentChanged(document);
        }
        break;
      case 1:
        sucess = true;
        break;
      case 2:
      case -1:
        sucess = false;
      }
    }
    return sucess;
  }

  /**
   * Displays a confirmation message for a document with a defined persistent
   * storage.
   * @param message the message to display with one parameter for the file name
   * @param parameter the parameter in the message
   * @return the option selected from the user
   */
  protected int displayMessage(String message, String parameter) {
    String text = MessageFormat.format(message, new Object[] { parameter });
    return JOptionPane.showConfirmDialog(getExplorer().getFrame(), text,
        Messages.titleActionConfirmation, JOptionPane.YES_NO_CANCEL_OPTION);
  }

  /**
   * Switches the wait cursor on and off.
   * @param switchOn true if the wait cursor should be switched on otherwise
   *          false
   */
  protected void switchWaitCursor(boolean switchOn) {
    JFrame frame = getExplorer().getFrame();
    if (switchOn) {
      cursor = frame.getCursor();
      frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    } else {
      frame.setCursor((cursor != null) ? cursor : Cursor
          .getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      cursor = null;
    }
  }
}
