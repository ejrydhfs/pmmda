/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.explorer.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerDocument;

/**
 * The save as action starts the file selection dialog. Upon sucessful selection
 * of a file the model is saved in the selected file and the associated format.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class ExplorerFileAction extends ExplorerGlobalActionImpl {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(ExplorerFileAction.class);

  /**
   * File choose to select the file to process.
   */
  private JFileChooser fileChooser;

  /**
   * Selected file to process.
   */
  private File selectedFile;

  /**
   * FIXME: hack until design not corrected.
   */
  protected boolean shouldSaveAndProceed = false;
  
  /**
   * Constructor of the instances of the class.
   * @param name name of the action node.
   * @param explorer explorer owning the global action.
   */
  public ExplorerFileAction(String name, Explorer explorer) {
    super(name, explorer);
    fileChooser = new JFileChooser(getName());
  }

  /**
   * Returns the selected file.
   * @return the selected file.
   * @see #setSelectedFile(File)
   */
  public File getSelectedFile() {
    return selectedFile;
  }

  /**
   * Sets the selected file.
   * @param selectedFile new selected file to set.
   * @see #getSelectedFile()
   */
  public void setSelectedFile(File selectedFile) {
    this.selectedFile = selectedFile;
  }
  /**
   * Returns the file chooser associated with this action.
   * @return the Swing file chooser.
   */
  public JFileChooser getFileChooser() {
    return fileChooser;
  }

  public void actionPerformed(ActionEvent event) {
    if (shouldSaveAndProceed) {
      if (!saveChangesAndProceed()) {
        return;
      }
    }
    fileChooser.setCurrentDirectory(getDirectory());
    int option = display();
    switch (option) {
    case JFileChooser.APPROVE_OPTION:
      selectedFile = fileChooser.getSelectedFile();
      if (selectedFile != null) {
        log.trace("File Selected " + selectedFile);
        if(execute()) {
          try {
            StringBuilder buffer = new StringBuilder();
            buffer.append(getName()).append(" ").append(
                selectedFile.getCanonicalPath());
            getExplorer().getStatusBar().setInfo(buffer.toString());
          } catch (IOException e) {
            log.error(e);
          }
        }
      }
      break;
    }
  }

  /**
   * Checks if the file to overwrite exists and if yes ask the user if he wants
   * to overwrite the file.
   * @return true if the file should be overwriten otherwise false.
   */
  protected boolean shouldOverwriteFile() {
    if (getSelectedFile() != null && getSelectedFile().exists()) {
      int selection = displayMessage("actions.FILE_OVERWRITE", getSelectedFile());
      return (selection != 1);
    }
    return true;
  }

  /**
   * Returns the directory from which the file should be first searched. If a
   * file was previously selected use the directory containing it. If not and
   * the current document has a persistent representation, use the directory
   * containing it. Otherwise use the home directory of the user.
   * @return the requested directory.
   */
  protected File getDirectory() {
    File directory = null;
    if (selectedFile != null) {
      directory = selectedFile.getParentFile();
    } else {
      ExplorerDocument document = getExplorer().getDocument();
      if (document != null) {
        File file = document.getFile();
        if (file != null) {
          directory = file.getParentFile();
        }
      }
    }
    return directory;
  }
  
  /**
   * Displays the file selection dialog with the needed options.
   * @return result of the file selection activity.
   */
  protected abstract int display();
}