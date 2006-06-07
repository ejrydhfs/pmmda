/*
 * copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the
 * License. $Id$
 */
package ch.bbv.pim.ui.actions;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerFileAction;
import ch.bbv.explorer.resources.Messages;
import ch.bbv.pim.formats.ContactEmitter;
import ch.bbv.pim.ui.PimApplication;
import ch.bbv.pim.ui.PimDocument;
import ch.bbv.pim.ui.PimExplorer;

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
  private static final long serialVersionUID = 1020674104541804861L;

  /**
   * Logger for all instance of the class.
   */
  private static Log log = LogFactory.getLog(SaveAsAction.class);

  /**
   * Constructor of the class.
   * @param explorer explorer owning the global action
   */
  public SaveAsAction(Explorer explorer) {
    super(Messages.actionSave, explorer);
    PimFileTypes.getInstance().addFileFilters(getFileChooser());
  }

  public boolean execute() {
    if (!shouldOverwriteFile()) {
      lastResult = false;
      return lastResult;
    }
    File file = getSelectedFile();
    switchWaitCursor(true);
    PimExplorer explorer = (PimExplorer) getExplorer();
    PimDocument document = explorer.getDocument();
    try {
      file = PimFileTypes.getInstance().normalizeFile(
          getFileChooser().getFileFilter(), file);
      ContactEmitter emitter = PimApplication.getApplication()
          .getFormatManager().getEmitter(file.getCanonicalPath());
      if (emitter != null) {
        emitter.emit(document.getAddressBook(), file);
        document.setFile(file);
        explorer.setDocument(document);
      } else {
        JOptionPane.showMessageDialog(explorer.getFrame(),
            "File extension not supported in the application",
            "File extension error", JOptionPane.ERROR_MESSAGE);

      }
    } catch (IOException e) {
      log.error(e);
      lastResult = false;
      return lastResult;
    } finally {
      switchWaitCursor(false);
    }
    return lastResult;
  }

  @Override
  protected int display() {
    return getFileChooser().showSaveDialog(getExplorer().getFrame());
  }
}