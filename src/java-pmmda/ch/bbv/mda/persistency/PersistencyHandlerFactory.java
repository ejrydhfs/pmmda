/* 
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.persistency;

import java.io.File;

/**
 * The factory handler creates persistency handlers for various supported
 * formats. The factory manages the instances and their creation.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class PersistencyHandlerFactory {
  /**
   * The handler responsible for the serialized representation of a model.
   */
  private SerializePersistencyHandler serialHandler;

  /**
   * The handler responsible for the XML representation of a model.
   */
  private XmlPersistencyHandler xmlHandler;

  /**
   * The handler responsible for the MOF NSUML representation of a model.
   */
  private NsUmlPersistencyHandler nsUmlHandler;

  /**
   * Returns the best match for the given file. The selection of the optimal
   * persistency handler is based on the extension of the file.
   * @param file file, which best persistency handler should be infered.
   * @return the optimal handler if found otherwise null.
   * @pre file != null
   */
  public PersistencyHandler getHandler(File file) {
    assert(file != null);
    String filename = file.getName().toLowerCase();
    PersistencyHandler persistencyHandler = null;
    if (filename.endsWith(".ser")) {
      persistencyHandler = getSerializeHandler();
    } else if (filename.endsWith(".xml")) {
      persistencyHandler = getXmlHandler();
    } else if (filename.endsWith(".xmi") || filename.endsWith(".zargo")) {
      persistencyHandler = getNsUmlHandler();
    } else if (filename.endsWith(".mdl")) {
      persistencyHandler = getRoseHandler();
    } else if (filename.endsWith(".ecore")) {
      persistencyHandler = getEcoreHandler();
    }
    return persistencyHandler;
  }

  /**
   * Returns the an alternate best match for the given file. The selection of
   * the optimal persistency handler is based on the extension of the file.
   * @param file file, which best persistency handler should be infered.
   * @return the optimal handler if found otherwise null.
   * @pre file != null
   */
  public PersistencyHandler getAlternateHandler(File file) {
    assert(file != null);
    String filename = file.getName().toLowerCase();
    PersistencyHandler persistencyHandler = null;
    if (filename.endsWith(".xmi") || filename.endsWith(".xml")) {
      persistencyHandler = getNsUmlHandler();
    }
    return persistencyHandler;
  }

  /**
   * Returns the XML persistency format handler.
   * @return the XML persistency handler.
   */
  public PersistencyHandler getXmlHandler() {
    if (xmlHandler == null) {
      xmlHandler = new XmlPersistencyHandler();
    }
    return xmlHandler;
  }

  /**
   * Returns the serialize persistency format handler.
   * @return the serialize persistency format handler.
   */
  public PersistencyHandler getSerializeHandler() {
    if (serialHandler == null) {
      serialHandler = new SerializePersistencyHandler();
    }
    return serialHandler;
  }

  /**
   * Returns the XMI 1.0 / UML 1.3 persistency format handler.
   * @return the serialize persistency format handler.
   */
  public PersistencyHandler getNsUmlHandler() {
    if (nsUmlHandler == null) {
      nsUmlHandler = new NsUmlPersistencyHandler();
    }
    return nsUmlHandler;
  }

  /**
   * Returns the XMI 1.1 / UML 1.4 persistency format handler.
   * @return the serialize persistency format handler.
   */
  public PersistencyHandler getMdrHandler() {
    return new XmlPersistencyHandler();
  }

  public PersistencyHandler getEcoreHandler() {
    //return new EcorePersistencyHandler();
    return null; // TODO
  }

  public PersistencyHandler getJavaDocHandler() {
    return new XmlPersistencyHandler();
  }

  public PersistencyHandler getRoseHandler() {
    //return new RosePersistencyHandler();
    return null; // TODO
  }

}