/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.persistency;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.mda.MetaModel;

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
public class SerializePersistencyHandler implements PersistencyHandler {
  /**
   * Logger interface of the class (see Logging framework).
   */
  private static Log log = LogFactory.getLog(SerializePersistencyHandler.class);

  public MetaModel readModel(File file) {
    MetaModel project = null;
    try {
      ObjectInput in = new ObjectInputStream(new FileInputStream(file));
      project = (MetaModel) in.readObject();
      in.close();
    } catch (IOException ioe) {
      log.error(ioe);
    } catch (ClassNotFoundException cnfe) {
      log.error(cnfe);
    }
    return project;
  }

  public void writeModel(MetaModel project, File file) {
    try {
      ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file));
      out.writeObject(project);
      out.close();
    } catch (IOException ioe) {
      log.error(ioe);
    }
  }

  public void updateModel(MetaModel project, File file) {
  }
}