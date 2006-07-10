/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.ui;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import ch.bbv.explorer.ExplorerDocument;
import ch.bbv.explorer.ExplorerDocumentImpl;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.generators.TreeChecker;
import ch.bbv.mda.persistence.PersistenceException;
import ch.bbv.mda.persistence.PersistenceHandler;
import ch.bbv.mda.persistence.PersistenceHandlerFactory;
import ch.bbv.mda.pim.CodeTypeMgr;

import com.jgoodies.validation.ValidationResult;

/**
 * The document represents a document loaded in memory containing a MDA model
 * and associated resources.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MdaDocument extends ExplorerDocumentImpl implements
    ExplorerDocument {
  /**
   * The directory name containing all reference code values of the model.
   */
  public static final String CODES_DIRECTORY = "codes";

  /**
   * The MDA model of an application.
   */
  private MetaModel model;

  /**
   * Default constructor of the class.
   */
  public MdaDocument() {
    model = new MetaModel();
  }

  /**
   * Returns the MDA model of the document.
   * @return the MDA model property
   */
  public MetaModel getModel() {
    return model;
  }

  /**
   * Loads the MDA model and initializes the associated resources.
   * @param file file containing the MDA model
   * @param vResults warnings and errors discovered during load
   * @throws PersistenceException if model could not be written
   */
  public void load(File file, ValidationResult vResults) throws PersistenceException {
    assert file != null;
    PersistenceHandlerFactory factory = new PersistenceHandlerFactory();
    PersistenceHandler handler = factory.getHandler(file);
    assert (handler != null);
    MetaModel loadedModel = handler.readModel(file, vResults);
    if (loadedModel != null) {
      model = loadedModel;
    }
    model.accept(new TreeChecker());
    model.setFilename(file.toString());
    File directory = new File(model.getFilename()).getParentFile();
    model.getCodeTypeMgr().reset(new File(directory, CODES_DIRECTORY));
    setNew(false);
    setModified(false);
    setName(model.getName());
    setFile(file);
  }

  /**
   * Saves the MDA model and the associated resources.
   * @param vResults warnings and errors during save operation.
   * @throws PersistenceException if model could not be written
   * @throws IOException if code values could not be written
   */
  public void save(ValidationResult vResults) throws PersistenceException, IOException {
    PersistenceHandlerFactory factory = new PersistenceHandlerFactory();
    File file = getFile();
    PersistenceHandler handler = factory.getHandler(file);
    handler.writeModel(model, file, vResults);
    model.getCodeTypeMgr().saveAll();
    setNew(false);
    setModified(false);
    setName(model.getName());
  }

  /**
   * Saves the MDA model and the associated resources in the new file.
   * @param file file in which the model should be saved
   * @param vResults warnings and errors during save as operation.
   * @throws PersistenceException if model could not be written
   * @throws IOException if code values could not be written
   */
  public void saveAs(File file, ValidationResult vResults) throws PersistenceException, IOException {
    PersistenceHandlerFactory factory = new PersistenceHandlerFactory();
    CodeTypeMgr codeTypeMgr = model.getCodeTypeMgr();
    File root = codeTypeMgr.getRoot();
    File newRoot = file.getParentFile();
    File target = new File(newRoot, CODES_DIRECTORY);
    if (root != null) {
      if (!target.exists()) {
        target.mkdir();
      }
      if (!root.equals(target)) {
        FileUtils.copyDirectory(root, target);
      }
    }
    codeTypeMgr.setRoot(target);
    codeTypeMgr.saveAll();
    PersistenceHandler handler = factory.getHandler(file);
    handler.writeModel(model, file, vResults);
    model.setFilename(file.getAbsolutePath());
    setNew(false);
    setModified(false);
    setName(model.getName());
    setFile(file);
  }
}
