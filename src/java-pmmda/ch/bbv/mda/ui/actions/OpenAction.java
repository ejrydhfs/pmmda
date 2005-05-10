/*
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the
 * License. $Id$
 */
package ch.bbv.mda.ui.actions;

import java.io.File;

import javax.swing.JFileChooser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.actions.ExplorerOpenAction;
import ch.bbv.explorer.actions.SimpleFileFilter;
import ch.bbv.mda.persistency.PersistencyException;
import ch.bbv.mda.ui.MdaDocument;
import ch.bbv.mda.ui.MdaExplorer;

/**
 * The open action implements the loading of a complete meta model. The model is
 * first loaded from the persistency storage and a graph of meta objects are
 * instantiated. Second the elements are completed with missing requested tagged
 * values - for example if new cartridges were defined -. Last the explorer tree
 * is constructed.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class OpenAction extends ExplorerOpenAction {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(OpenAction.class);

  public void initialize() {
    JFileChooser fileChooser = getFileChooser();
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(
        FileTypes.RATIONAL_ROSE_EXT, FileTypes.RATIONAL_ROSE_TEXT));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(
        FileTypes.ARGOUML_EXT, FileTypes.ARGOUML_TEXT));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(
        FileTypes.SERIALIZE_EXT, FileTypes.SERIALIZE_TEXT));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(
        FileTypes.POSEIDON_EXT, FileTypes.POSEIDON_TEXT));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(FileTypes.XML_EXT,
        FileTypes.XML_TEXT));
    fileChooser.addChoosableFileFilter(new SimpleFileFilter(FileTypes.XMI_EXT,
        FileTypes.XMI_TEXT));
  }

  public void execute() {
    try {
      openModel(getSelectedFile());
    } catch (PersistencyException e) {
      log.debug("File Not Found", e);
    }
  }

  /**
   * Opens the model in the given file programatically.
   * @param file file containing the model to open.
   * @throws PersistencyException an exception was encountered when loading the
   *           model.
   * @pre file != null
   */
  public void openModel(File file) throws PersistencyException {
    assert (file != null);
    MdaExplorer explorer = (MdaExplorer) getExplorer();
    MdaDocument document = new MdaDocument();
    document.load(file);
    explorer.setDocument(document);
    getExplorer().getMenuBar().documentSaved();
  }
}