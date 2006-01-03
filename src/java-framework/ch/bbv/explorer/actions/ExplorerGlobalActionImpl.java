/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerDocument;
import ch.bbv.explorer.ExplorerMenuBar;
import ch.bbv.explorer.ExplorerMenuBar.Names;

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
   * Constructor of the class.
   * @param name name of the action.
   * @param explorer explorer owning the global action.
   */
  public ExplorerGlobalActionImpl(String name, Explorer explorer) {
    super(name);
    this.explorer = explorer;
    setEnabled(true);
  }

  /**
   * Returns the name of the global action.
   * @return name of the action.
   */
  public String getName() {
    return (String) getValue(Action.NAME);
  }

  /**
   * Returns the explorer owning the global action.
   * @return the owning explorer if the action is registered otherwise null.
   * @see #setExplorer(Explorer)
   */
  public Explorer getExplorer() {
    return explorer;
  }

  /**
   * Sets the explorer owning the global action.
   * @param explorer new explorer owning the global action.
   * @see #getExplorer()
   */
  public void setExplorer(Explorer explorer) {
    this.explorer = explorer;
  }

  /**
   * Performs the action associated with the event.
   * @param event event to process.
   */
  public void actionPerformed(ActionEvent event) {
    execute();
  }

  /**
   * Asks the user if his changes should be saved. If yes the changes are saved.
   * The returned value states if the action was sucessful and the action should
   * proceed with the requested function or if the user aborted the action.
   * @return flag indicating if the selected action should proceed or not.
   */
  protected boolean saveChangesAndProceed() {
    boolean sucess = true;
    ExplorerMenuBar menuBar = getExplorer().getMenuBar();
    ExplorerDocument document = getExplorer().getDocument();
    ExplorerDocument.State state = document.getState();
    if (state == ExplorerDocument.State.MODIFIED) {
      int selection = displayMessage("actions.SAVE_CHANGES");
      switch (selection) {
      case 0:
        File file = document.getFile();
        if (file != null) {
          sucess = menuBar.getStandardAction(Names.SAVE).execute();
        } else {
          sucess = menuBar.getStandardAction(Names.SAVE_AS).execute();
        }
        if (sucess) {
          menuBar.documentChanged(ExplorerDocument.State.CLEAN);
        }
        break;
      case 1:
        sucess = true;
        break;
      case 2:
        sucess = false;
      }
    }
    return sucess;
  }

  /**
   * Displays a confirmation message for a document with a defined persistent
   * storage.
   * @param messageKey key of the resource for the message of the dialog.
   * @return the option selected from the user.
   */
  protected int displayMessage(String messageKey) {
    return displayMessage(messageKey, getExplorer().getDocument().getFile());
  }
  
  /**
   * Displays a confirmation message for a document with a defined persistent
   * storage.
   * @param messageKey key of the resource for the message of the dialog.
   * @param file the message is related to.
   * @return the option selected from the user.
   */
  protected int displayMessage(String messageKey, File file) {
    ResourceBundle bundle = ResourceBundle
        .getBundle("ch.bbv.explorer.Messages");
    String filename = file.getName();
    String message = MessageFormat.format(bundle.getString(messageKey),
        new Object[] { filename });
    return JOptionPane.showConfirmDialog(getExplorer().getFrame(), message,
        "actions.CONFIRMATION", JOptionPane.YES_NO_CANCEL_OPTION);
  }
}
