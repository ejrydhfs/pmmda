/*
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the
 * License. $Id$
 */
package ch.bbv.mda.ui.actions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerFileAction;
import ch.bbv.mda.persistence.PersistenceException;
import ch.bbv.mda.ui.MdaDocument;
import ch.bbv.mda.ui.MdaExplorer;
import ch.bbv.utilities.ResourceUtility;
import ch.bbv.utilities.SystemUtilities;

/**
 * The save as action for the user interface of <em>pmMDA</em> application.
 * The action semantic requests handling of various cases.
 * <ul>
 * <li>Saving a model to the XML format is implemented with XML serialization
 * open source library.</li>
 * <li>Saving a model to the zargo format requires the creation of a file
 * structure compatible to ArgoUML zargo format. The action first copies an
 * empty template to the destination directory and rename it with the user
 * specified name. When the model data is written into this file.</li>
 * </ul>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class SaveAsAction extends ExplorerFileAction {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Logger for all instance of the class.
   */
  private static Log log = LogFactory.getLog(SaveAsAction.class);

  /**
   * Default constructor.
   * @param explorer explorer owning the global action.
   */
  public SaveAsAction(Explorer explorer) {
    super("SaveAs...", explorer);
    FileTypes.addFileFilters(getFileChooser());
  }

  public boolean execute() {
    if (!shouldOverwriteFile()) {
      return false;
    }
    File file = getSelectedFile();
    String extension = SystemUtilities.getFileExtension(file).toLowerCase();
    try {
      if (Arrays.binarySearch(FileTypes.ARGOUML_EXT, extension) >= 0) {
        writeModel(file);
      } else if (Arrays.binarySearch(FileTypes.POSEIDON_EXT, extension) >= 0) {
        JOptionPane.showMessageDialog(null,
            "Poseidon Save Option Not Supported", "Not Yet Implemented",
            JOptionPane.ERROR_MESSAGE);
      } else if (Arrays.binarySearch(FileTypes.XML_EXT, extension) >= 0) {
        writeModel(file);
      } else if (Arrays.binarySearch(FileTypes.SERIALIZE_EXT, extension) >= 0) {
        writeModel(file);
      } else if (Arrays.binarySearch(FileTypes.XMI_EXT, extension) >= 0) {
        writeModel(file);
      } else {
        JOptionPane.showMessageDialog(getExplorer().getFrame(), ResourceUtility
            .getString("mda.ui.sorryMsg.ILLEGAL_FILE_NAME_OR_TYPE"),
            ResourceUtility.getString("mda.ui.sorryMsg.SORRY"),
            JOptionPane.ERROR_MESSAGE);
      }
    } catch (IOException e) {
      log.error(e);
      return false;
    }
    return true;
  }

  /**
   * Writes the model to a new ArgoUML file. The file is copied from a template
   * before writing the model into it.
   * @param file new file for the model.
   * @throws IOException if the model could not be saved.
   */
  private void writeModel(File file) throws IOException {
    try {
      MdaExplorer explorer = (MdaExplorer) getExplorer();
      MdaDocument document = explorer.getDocument();
      document.saveAs(file);
    } catch (PersistenceException e) {
      log.error(e);
    }
  }
}