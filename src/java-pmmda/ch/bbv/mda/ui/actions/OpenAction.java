/*
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the
 * License. $Id$
 */
package ch.bbv.mda.ui.actions;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerFileAction;
import ch.bbv.mda.persistence.PersistenceException;
import ch.bbv.mda.ui.MdaDocument;
import ch.bbv.mda.ui.MdaExplorer;
import ch.bbv.utilities.SystemUtilities;

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
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(OpenAction.class);

  /**
   * Name of the action as displayed in the menu.
   */
  private static final String ACTION = "Open...";

  /**
   * Default constructor.
   * @param explorer explorer owning the global action.
   */
  public OpenAction(Explorer explorer) {
    super(ACTION, explorer);
    shouldSaveAndProceed = true;
    FileTypes.addFileFilters(getFileChooser());
  }

  public boolean execute() {
    try {
      MdaExplorer explorer = (MdaExplorer) getExplorer();
      MdaDocument document = new MdaDocument();
      File file = getSelectedFile();
      String extension = SystemUtilities.getFileExtension(file);
      if(extension == null) { // in case where user thinks that the extension comes from the file filter we get the first match from the filter.
        extension = FileTypes.getBestMatchingExtension(getFileChooser().getFileFilter().getDescription());
        if(extension != null) file = new File(file.getPath()+"."+extension);
      }
      document.load(file);
      explorer.setDocument(document);
    } catch (PersistenceException e) {
      log.debug("File Not Found", e);
      return false;
    }
    return true;
  }
  
  protected int display() {
    return getFileChooser().showOpenDialog(getExplorer().getFrame());
  }
}