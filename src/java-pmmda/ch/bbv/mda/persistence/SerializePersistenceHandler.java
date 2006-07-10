/* 
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.persistence;

import java.io.*;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.explorer.resources.Messages;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.resources.MessagesMda;

import com.jgoodies.validation.ValidationResult;

/**
 * The serialize persistency handler uses the serialize mechanism of the Java
 * environment to persist a tree of Java beans. The serialize function uses only
 * the standard serialize mechanism. The advantage of this format is the speed
 * of the transformation operations. The disadvantage is that the serialize
 * mechanism is not resilient to class structure modification. Use this format
 * only to exchange models between instances of the <em>pmMDA</em> having the
 * same version number.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class SerializePersistenceHandler implements PersistenceHandler {
  /**
   * Logger interface of the class (see Logging framework).
   */
  private static Log log = LogFactory.getLog(SerializePersistenceHandler.class);

  public MetaModel readModel(File file, ValidationResult vResults) throws PersistenceException {
    MetaModel project = null;
    try {
      ObjectInput in = new ObjectInputStream(new FileInputStream(file));
      project = (MetaModel) in.readObject();
      in.close();
    } catch (InvalidClassException ice) {
      log.error(ice);
      JOptionPane.showMessageDialog(null, MessagesMda.msgOldFormat,
          Messages.titleWarning, JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
      throw new PersistenceException(e);
    }
    return project;
  }

  public void writeModel(MetaModel project, File file, ValidationResult vResults)
      throws PersistenceException {
    try {
      ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file));
      out.writeObject(project);
      out.close();
    } catch (Exception e) {
      throw new PersistenceException(e);
    }
  }

  public void updateModel(MetaModel project, File file, ValidationResult vResults)
      throws PersistenceException {
    throw new PersistenceException();
  }
}