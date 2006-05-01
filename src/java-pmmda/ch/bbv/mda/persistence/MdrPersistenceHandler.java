/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.uml.UmlPackage;

import ch.bbv.mda.MetaModel;
import ch.bbv.mda.persistence.mdr.MdaTransformer;
import ch.bbv.mda.persistence.mdr.MdrTransformer;

public class MdrPersistenceHandler implements PersistenceHandler {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(MdrPersistenceHandler.class);

  /**
   * Transformer from NSUML to MDA.
   */
  private MdrTransformer mdrTransformer;

  /**
   * Transformer from MDA to NSUML.
   */
  private MdaTransformer mdaTransformer;

  /**
   * Default constructor of the class.
   */
  public MdrPersistenceHandler() {
    mdrTransformer = new MdrTransformer();
    mdaTransformer = new MdaTransformer();
  }

  public MetaModel readModel(File file) throws PersistenceException {
    try {
      UmlPackage uml = mdrTransformer.loadArgoModel(file);
    } catch (FileNotFoundException e) {
      log.error(e);
    } catch (IOException e) {
      log.error(e);
    }
    return null;
  }

  public void writeModel(MetaModel model, File file)
      throws PersistenceException {
    throw new PersistenceException();
  }

  public void updateModel(MetaModel model, File file)
      throws PersistenceException {
    throw new PersistenceException();
  }

}
