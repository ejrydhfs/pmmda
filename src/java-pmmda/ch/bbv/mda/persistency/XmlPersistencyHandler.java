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

import com.thoughtworks.xstream.XStream;

/**
 * The XML persistency handler uses the Xstream library to create a legible XML
 * representation of a meta model.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class XmlPersistencyHandler implements PersistencyHandler {
  /**
   * Logger interface of the class (see Logging framework).
   */
  private static Log log = LogFactory.getLog(XmlPersistencyHandler.class);

  public MetaModel readModel(File file) throws PersistencyException {
    MetaModel model = null;
    try {
      FileReader reader = new FileReader(file);
      XStream stream = new XStream();
      model = (MetaModel) stream.fromXML(reader);
      reader.close();
      model.accept(new InitVisitor());
    } catch (FileNotFoundException e) {
      log.error(e);
    } catch (IOException e) {
      log.error(e);
    }
    return model;
  }

  public void writeModel(MetaModel model, File file) {
    try {
      FileWriter writer = new FileWriter(file);
      XStream stream = new XStream();
      writer.write(stream.toXML(model));
      writer.close();
    } catch (FileNotFoundException e) {
      log.error(e);
    } catch (IOException e) {
      log.error(e);
    }
  }

  public void updateModel(MetaModel project, File file)
      throws PersistencyException {
    // TODO Auto-generated method stub
  }
}