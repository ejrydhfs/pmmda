/* 
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.persistence;

import java.io.File;

import ch.bbv.mda.MetaModel;

/**
 * The persistency handler is responsible to write and read a complete MDA
 * project. The classes implementing the interface support various persistency
 * schemes. Methods are provided to read, import, write and update projects.
 * Complex operations are realized with a concrete implementation of the
 * persistency policy.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface PersistenceHandler {
  /**
   * Reads a file, extracts the information and return the meta model stored in
   * the file.
   * @param file file containing the MDA model to extract.
   * @return the read meta model graph.
   * @throws PersistenceException A input/output error occured. The exception
   *           contains the original thrown exception.
   */
  MetaModel readModel(File file) throws PersistenceException;

  /**
   * Writes a model to a file. The previous content of the file is discarded.
   * @param model mode to persist in the file.
   * @param file file which should contain the persistent representation of the
   *          model.
   * @throws PersistenceException A input/output error occured. The exception
   *           contains the original thrown exception.
   */
  void writeModel(MetaModel model, File file) throws PersistenceException;

  /**
   * Updates the model stored in the given file.
   * @param model meta model used to update the UML model stored in the file.
   * @param file file containing the model to update.
   * @throws PersistenceException A input/output error occured. The exception
   *           contains the original thrown exception.
   */
  void updateModel(MetaModel model, File file) throws PersistenceException;
}