/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.explorer.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The save as action starts the file selection dialog. Upon sucessful selection
 * of a file the model is saved in the selected file and the associated format.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class ExplorerOpenAction extends ExplorerGlobalActionImpl {
  private Log log = LogFactory.getLog(ExplorerOpenAction.class);

  private JFileChooser fileChooser;

  private File selectedFile;

  /**
   * Default constructor of the class.
   */
  public ExplorerOpenAction() {
    super("Open");
    fileChooser = new JFileChooser(getName());
    initialize();
  }

  /** 
   * Returns the selected file.
   * @return the selected file.
   */
  public File getSelectedFile() {
    return selectedFile;
  }

  /**
   * Returns the file chooser associated with this action.
   * @return the Swing file chooser.
   */
  public JFileChooser getFileChooser() {
    return fileChooser;
  }

  public void actionPerformed(ActionEvent event) {
    int option = fileChooser.showOpenDialog(null);
    switch (option) {
    case JFileChooser.APPROVE_OPTION:
      selectedFile = fileChooser.getSelectedFile();
      if (selectedFile != null) {
        log.trace("File Selected " + selectedFile);
        execute();
      }
      break;
    }
  }
}