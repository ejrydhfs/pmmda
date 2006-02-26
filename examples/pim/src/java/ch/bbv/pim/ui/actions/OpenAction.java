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
import ch.bbv.pim.ui.actions.FileTypes;
import ch.bbv.pim.formats.ContactParser;
import ch.bbv.pim.ui.PimApplication;
import ch.bbv.pim.ui.PimDocument;
import ch.bbv.pim.ui.PimExplorer;

/**
 * The open action implements the loading of a complete meta model. The model is
 * first loaded from the persistency storage and a graph of meta objects are
 * instantiated. Second the elements are completed with missing requested tagged
 * values - for example if new cartridges were defined -. Last the explorer tree
 * is constructed.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class OpenAction extends ExplorerFileAction {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Name of the action as displayed in the menu.
   */
  private static final String ACTION = "Open...";

  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(OpenAction.class);

  /**
   * Default constructor.
   * @param explorer explorer owning the global action.
   */
  public OpenAction(Explorer explorer) {
    super(ACTION, explorer);
    FileTypes.addFileFilters(getFileChooser());
    shouldSaveAndProceed = true;
  }

  public boolean execute() {
    File file = getSelectedFile();
    PimExplorer explorer = (PimExplorer) getExplorer();
    switchWaitCursor(true);
    try {
      ContactParser parser = PimApplication.getApplication().getFormatManager()
          .getParser(file.getCanonicalPath());
      if (parser != null) {
        PimDocument document = new PimDocument();
        parser.synchronize(document.getAddressBook(), file);
        document.setFile(file);
        explorer.setDocument(document);
      } else {
        JOptionPane.showMessageDialog(getExplorer().getFrame(),
            "File extension not supported in the application",
            "File extension error", JOptionPane.ERROR_MESSAGE);

      }
    } catch (IOException e) {
      log.error("Error when reading file");
    } finally {
      switchWaitCursor(false);
    }
    lastResult = true;
    return lastResult;
  }

  protected int display() {
    return getFileChooser().showOpenDialog(getExplorer().getFrame());
  }
}