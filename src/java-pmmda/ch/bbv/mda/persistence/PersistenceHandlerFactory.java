/* 
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.persistence;


import java.io.File;

import ch.bbv.mda.persistence.eclipse.RosePersistenceHandler;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The factory handler creates persistency handlers for various supported
 * formats. The factory manages the instances and their creation.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class PersistenceHandlerFactory {
  /**
   * The handler responsible for the serialized representation of a model.
   */
  private SerializePersistenceHandler serialHandler;

  /**
   * The handler responsible for the XML representation of a model.
   */
  private XmlPersistenceHandler xmlHandler;

  /**
   * The handler responsible for the MOF NSUML representation of a model.
   */
  private NsUmlPersistenceHandler nsUmlHandler;

  /**
   * Returns the best match for the given file. The selection of the optimal
   * persistency handler is based on the extension of the file.
   * @param file file, which best persistency handler should be infered.
   * @return the optimal handler if found otherwise null.
   * @pre file != null
   */
  public PersistenceHandler getHandler(File file) {
    assert (file != null);
    String filename = file.getName().toLowerCase();
    PersistenceHandler persistencyHandler = null;
    if (filename.endsWith(".ser")) {
      persistencyHandler = getSerializeHandler();
    } else if (filename.endsWith(".xml")) {
      persistencyHandler = getXmlHandler();
    } else if (filename.endsWith(".xmi") || filename.endsWith(".zargo")) {
      persistencyHandler = getNsUmlHandler();
    } else if (filename.endsWith(".ecore")) {
      persistencyHandler = getEcoreHandler();
    } else if (filename.endsWith(".mdl") || filename.endsWith(".cat")) {
      persistencyHandler = getRoseHandler();
    }
    return persistencyHandler;
  }

  /**
   * Returns the XML persistency format handler.
   * @return the XML persistency handler.
   */
  public PersistenceHandler getXmlHandler() {
    if (xmlHandler == null) {
      xmlHandler = new XmlPersistenceHandler();
    }
    return xmlHandler;
  }

  /**
   * Returns the serialize persistency format handler.
   * @return the serialize persistency format handler.
   */
  public PersistenceHandler getSerializeHandler() {
    if (serialHandler == null) {
      serialHandler = new SerializePersistenceHandler();
    }
    return serialHandler;
  }

  /**
   * Returns the XMI 1.0 / UML 1.3 persistency format handler.
   * @return the serialize persistency format handler.
   */
  public PersistenceHandler getNsUmlHandler() {
    if (nsUmlHandler == null) {
      nsUmlHandler = new NsUmlPersistenceHandler();
    }
    return nsUmlHandler;
  }

  /**
   * Returns the XMI 1.1 / UML 1.4 persistency format handler.
   * @return the serialize persistency format handler.
   */
  public PersistenceHandler getMdrHandler() {
    return new XmlPersistenceHandler();
  }

  /**
   * Returns the Ecore persistency format handler.
   * @return the requested persistency handler.
   */
  public PersistenceHandler getEcoreHandler() {
    return null; // TODO
  }

  /**
   * Returns the Javadoc handler persistency format handler.
   * @return the requested persistency handler.
   */
  public PersistenceHandler getJavaDocHandler() {
    return new XmlPersistenceHandler();
  }
  
  public PersistenceHandler getRoseHandler() {
    return (PersistenceHandler)ReflectUtilities.newInstance("ch.bbv.mda.persistence.eclipse.RosePersistenceHandler");
  } 
}