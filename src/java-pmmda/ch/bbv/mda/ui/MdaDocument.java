/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui;

import java.io.File;
import java.io.IOException;

import ch.bbv.mda.MetaModel;
import ch.bbv.mda.TreeChecker;
import ch.bbv.mda.operators.FileOperators;
import ch.bbv.mda.persistency.PersistencyException;
import ch.bbv.mda.persistency.PersistencyHandler;
import ch.bbv.mda.persistency.PersistencyHandlerFactory;
import ch.bbv.mda.pim.CodeTypeMgr;

/**
 * The document represents a document loaded in memory containing a MDA model
 * and associated resources.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MdaDocument {
  /**
   * The directory name containing all reference code values of the model.
   */
  public static final String CODES_DIRECTORY = "codes";

  /**
   * The MDA model of an application.
   */
  private MetaModel model;

  /**
   * The code type manager handling the reference codes of the model.
   */
  private CodeTypeMgr codeTypeMgr;

  /**
   * Default constructor of the class.
   */
  public MdaDocument() {
    this.codeTypeMgr = new CodeTypeMgr();
    this.model = new MetaModel();
  }

  /**
   * Returns the MDA model of the document.
   * @return the MDA model property.
   */
  public MetaModel getModel() {
    return model;
  }

  /**
   * Returns the code type manager of the document.
   * @return the code type manager property.
   */
  public CodeTypeMgr getCodeTypeMgr() {
    return codeTypeMgr;
  }

  /**
   * Loads the MDA model and initializes the associated resources.
   * @param file file containing the MDA model.
   * @throws PersistencyException if model could not be written.
   */
  public void load(File file) throws PersistencyException {
    assert (file != null);
    PersistencyHandlerFactory factory = new PersistencyHandlerFactory();
    PersistencyHandler handler = factory.getHandler(file);
    assert (handler != null);
    model = parseModel(handler, file);
    model.setFilename(file.toString());
    File directory = new File(model.getFilename()).getParentFile();
    codeTypeMgr.reset(new File(directory, CODES_DIRECTORY));
  }

  /**
   * Saves the MDA model and the associated resources.
   * @throws PersistencyException if model could not be written.
   * @throws IOException if code values could not be written.
   */
  public void save() throws PersistencyException, IOException {
    PersistencyHandlerFactory factory = new PersistencyHandlerFactory();
    File file = new File(model.getFilename());
    PersistencyHandler handler = factory.getHandler(file);
    handler.writeModel(model, file);
    codeTypeMgr.saveAll();
  }

  /**
   * Saves the MDA model and the associated resources in the new file.
   * @param file file in which the model should be saved.
   * @throws PersistencyException if model could not be written.
   * @throws IOException if code values could not be written.
   */
  public void saveAs(File file) throws PersistencyException, IOException {
    PersistencyHandlerFactory factory = new PersistencyHandlerFactory();
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
    PersistencyHandler handler = factory.getHandler(file);
    handler.writeModel(model, file);
  }

  /**
   * Parses given file with given handler
   * @param handler handler to parse the given file.
   * @param file file containing the model to be parsed.
   * @throws PersistencyException an exception was encountered when loading the
   *           model.
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
