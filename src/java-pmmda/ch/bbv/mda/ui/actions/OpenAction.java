/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.ui.actions;

import java.io.File;

import javax.swing.JFileChooser;

import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.actions.ExplorerOpenAction;
import ch.bbv.explorer.actions.SimpleFileFilter;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.TreeChecker;
import ch.bbv.mda.persistency.PersistencyException;
import ch.bbv.mda.persistency.PersistencyHandler;
import ch.bbv.mda.persistency.PersistencyHandlerFactory;
import ch.bbv.mda.ui.TreeConstructor;

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
  }

  public void execute() {
    try {
      openModel(getSelectedFile());
    } catch (PersistencyException pe) {
      System.out.println("File Not Found");
    }
  }

  /**
   * Opens the model in the given file programatically.
   * @param file file containing the model to open.
   * @throws PersistencyException an exception was encountered when loading the
   *         model.
   * @pre file != null
   */
  public void openModel(File file) throws PersistencyException {
    assert (file != null);
    MetaModel model = null;
    PersistencyHandlerFactory factory = new PersistencyHandlerFactory();
    PersistencyHandler handler = factory.getHandler(file);
    assert (handler != null);
    try {
      model = parseModel(handler, file);
    } catch (com.thoughtworks.xstream.alias.CannotResolveClassException e) {
      handler = factory.getAlternateHandler(file);
      model = parseModel(handler, file);
    }
    if (model == null)
      return;
    TreeConstructor constructor = new TreeConstructor(getExplorer()
        .getNodeTypeFactory());
    model.accept(constructor);
    model.setFilename(file.toString());
    Explorer explorer = getExplorer();
    explorer.setRoot(constructor.getRoot());
    explorer.getMenuBar().documentSaved();
  }

  /**
   * Parses given file with given handler
   * @param handler handler to parse the given file.
   * @param file file containing the model to be parsed.
   * @throws PersistencyException an exception was encountered when loading the
   *         model.
   * @return MetaModel containing parsed file
   */
  private MetaModel parseModel(PersistencyHandler handler, File file)
      throws PersistencyException {
    MetaModel model = handler.readModel(file);
    if (model == null)
      return null;
    model.accept(new TreeChecker());
    return model;
  }
}