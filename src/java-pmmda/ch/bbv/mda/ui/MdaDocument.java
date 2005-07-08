/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui;

import java.io.File;
import java.io.IOException;

import ch.bbv.explorer.ExplorerDocument;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.TreeChecker;
import ch.bbv.mda.operators.FileOperators;
import ch.bbv.mda.persistence.PersistenceException;
import ch.bbv.mda.persistence.PersistenceHandler;
import ch.bbv.mda.persistence.PersistenceHandlerFactory;
import ch.bbv.mda.pim.CodeTypeMgr;

/**
 * The document represents a document loaded in memory containing a MDA model
 * and associated resources.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MdaDocument implements ExplorerDocument {
  /**
   * The directory name containing all reference code values of the model.
   */
  public static final String CODES_DIRECTORY = "codes";

  /**
   * State of the document.
   */
  private State state;

  /**
   * File in which the model is stored.
   */
  private File file;

  /**
   * The MDA model of an application.
   */
  private MetaModel model;

  /**
   * Default constructor of the class.
   */
  public MdaDocument() {
    state = State.CREATED;
    model = new MetaModel();
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public File getFile() {
    return file;
  }

  /**
   * Returns the MDA model of the document.
   * @return the MDA model property.
   */
  public MetaModel getModel() {
    return model;
  }

  /**
   * Loads the MDA model and initializes the associated resources.
   * @param file file containing the MDA model.
   * @throws PersistenceException if model could not be written.
   */
  public void load(File file) throws PersistenceException {
    assert (file != null);
    PersistenceHandlerFactory factory = new PersistenceHandlerFactory();
    PersistenceHandler handler = factory.getHandler(file);
    assert (handler != null);
    model = parseModel(handler, file);
    model.setFilename(file.toString());
    File directory = new File(model.getFilename()).getParentFile();
    model.getCodeTypeMgr().reset(new File(directory, CODES_DIRECTORY));
    state = State.CLEAN;
    this.file = file;
  }

  /**
   * Saves the MDA model and the associated resources.
   * @throws PersistenceException if model could not be written.
   * @throws IOException if code values could not be written.
   */
  public void save() throws PersistenceException, IOException {
    PersistenceHandlerFactory factory = new PersistenceHandlerFactory();
    PersistenceHandler handler = factory.getHandler(file);
    handler.writeModel(model, file);
    model.getCodeTypeMgr().saveAll();
  }

  /**
   * Saves the MDA model and the associated resources in the new file.
   * @param file file in which the model should be saved.
   * @throws PersistenceException if model could not be written.
   * @throws IOException if code values could not be written.
   */
  public void saveAs(File file) throws PersistenceException, IOException {
    PersistenceHandlerFactory factory = new PersistenceHandlerFactory();
    CodeTypeMgr codeTypeMgr = model.getCodeTypeMgr();
    File root = codeTypeMgr.getRoot();
    File newRoot = file.getParentFile();
    if (root != null) {
      File source = new File(root, CODES_DIRECTORY);
      File target = new File(newRoot, CODES_DIRECTORY);
      FileOperators.copyDirectory(source, target);
    }
    codeTypeMgr.setRoot(new File(newRoot, CODES_DIRECTORY));
    codeTypeMgr.saveAll();
    model.setFilename(file.getAbsolutePath());
    PersistenceHandler handler = factory.getHandler(file);
    handler.writeModel(model, file);
    state = State.CLEAN;
    this.file = file;
  }

  /**
   * Parses given file with given handler
   * @param handler handler to parse the given file.
   * @param file file containing the model to be parsed.
   * @throws PersistenceException an exception was encountered when loading the
   *           model.
   * @return MetaModel containing parsed file
   */
  private MetaModel parseModel(PersistenceHandler handler, File file)
      throws PersistenceException {
    MetaModel model = handler.readModel(file);
    if (model == null)
      return null;
    model.accept(new TreeChecker());
    return model;
  }
}
