/*
 * copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the
 * License. $Id$
 */
package ch.bbv.mda.ui.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerDocument;
import ch.bbv.explorer.actions.ExplorerFileAction;
import ch.bbv.explorer.properties.Messages;
import ch.bbv.mda.persistence.PersistenceException;
import ch.bbv.mda.ui.MdaDocument;
import ch.bbv.mda.ui.MdaExplorer;
import ch.bbv.utilities.ResourceUtilities;

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
    super(Messages.actionSaveAs, explorer);
    MdaFileTypes.getInstance().addFileFilters(getFileChooser());
  }

  public boolean execute() {
    if (!shouldOverwriteFile()) {
      lastResult = false;
      return lastResult;
    }
    File file = getSelectedFile();
    switchWaitCursor(true);
    try {
      String extension = FilenameUtils.getExtension(file.getCanonicalPath());
      if (extension == null) {
        file = MdaFileTypes.getInstance().normalizeFile(
            getFileChooser().getFileFilter(), file);
      }
      extension = FilenameUtils.getExtension(file.getCanonicalPath());
      if (extension == null) {
        JOptionPane.showMessageDialog(getExplorer().getFrame(),
            ResourceUtilities
                .getString("mda.ui.errorMsg.ILLEGAL_FILE_NAME_OR_TYPE"),
            ResourceUtilities.getString("mda.ui.errorMsg.ERROR"),
            JOptionPane.ERROR_MESSAGE);
        lastResult = false;
        return lastResult;
      }
      extension = extension.toLowerCase();
      if (Arrays.binarySearch(MdaFileTypes.ARGOUML_EXT, extension) >= 0) {
        writeModel(file);
      } else if (Arrays.binarySearch(MdaFileTypes.POSEIDON_EXT, extension) >= 0) {
        JOptionPane.showMessageDialog(null,
            "Poseidon Save Option Not Supported", "Not Yet Implemented",
            JOptionPane.ERROR_MESSAGE);
      } else if ((Arrays.binarySearch(MdaFileTypes.XML_EXT, extension) >= 0)
          || (Arrays.binarySearch(MdaFileTypes.SERIALIZE_EXT, extension) >= 0)
          || (Arrays.binarySearch(MdaFileTypes.XMI_EXT, extension) >= 0)
          || (Arrays.binarySearch(MdaFileTypes.RATIONAL_ROSE_EXT, extension) >= 0)) {
        writeModel(file);
      } else {
        JOptionPane.showMessageDialog(getExplorer().getFrame(),
            ResourceUtilities
                .getString("mda.ui.sorryMsg.ILLEGAL_FILE_NAME_OR_TYPE"),
            ResourceUtilities.getString("mda.ui.sorryMsg.SORRY"),
            JOptionPane.ERROR_MESSAGE);
      }
    } catch (Exception e) {
      lastResult = false;
      return lastResult;
    } finally {
      switchWaitCursor(false);
    }
    ExplorerDocument document = getExplorer().getDocument();
    document.setModified(false);
    getExplorer().getMenuBar().documentChanged(document);
    lastResult = true;
    return lastResult;
  }

  /**
   * Writes the model to a new ArgoUML file. The file is copied from a template
   * before writing the model into it.
   * @param file new file for the model.
   * @throws PersistenceException if the model could not be saved.
   */
  private void writeModel(File file) throws PersistenceException {
    try {
      MdaExplorer explorer = (MdaExplorer) getExplorer();
      MdaDocument document = explorer.getDocument();
      document.saveAs(file);
    } catch (PersistenceException e) {
      log.error(e);
      if (e.getCause() instanceof FileNotFoundException) {
        // This cause indicates that writter attempt to write a locked file.
        JOptionPane.showMessageDialog(null, e.getLocalizedMessage(),
            ResourceUtilities.getString("mda.ui.sorryMsg.SORRY"),
            JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(getExplorer().getFrame(),
            ResourceUtilities
                .getString("mda.ui.sorryMsg.UNSUPPORTED_FILE_TYPE_FOR_SAVE"),
            ResourceUtilities.getString("mda.ui.sorryMsg.SORRY"),
            JOptionPane.ERROR_MESSAGE);
      }
      throw e;
    } catch (NullPointerException e) {
      log.error(e);
      JOptionPane.showMessageDialog(getExplorer().getFrame(), ResourceUtilities
          .getString("mda.ui.sorryMsg.UNSUPPORTED_FILE_TYPE_FOR_SAVE"),
          ResourceUtilities.getString("mda.ui.sorryMsg.SORRY"),
          JOptionPane.ERROR_MESSAGE);
      throw new PersistenceException(e);
    } catch (IOException e) {
      log.error(e);
      throw new PersistenceException(e);
    }
  }

  @Override
  protected int display() {
    return getFileChooser().showSaveDialog(getExplorer().getFrame());
  }
}