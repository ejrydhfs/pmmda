/* 
 * copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.persistence;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

import ch.bbv.utilities.ReflectUtilities;

/**
 * The factory handler creates persistency handlers for various supported
 * formats. The factory manages the instances and their creation.
 * 
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
   * 
   * @param file
   *          file, which best persistency handler should be inferred.
   * @return the optimal handler if found otherwise null.
   * @pre file != null
   */
  public PersistenceHandler getHandler(File file) {
    assert file != null;
    String filename = file.getName().toLowerCase();
    PersistenceHandler handler = null;
    if (filename.endsWith(".ser")) {
      handler = getSerializeHandler();
    } else if (filename.endsWith(".xml")) {
      handler = getXmlHandler();
    } else if (filename.endsWith(".zargo")) {
      handler = getZargoHandler(file);
    } else if (filename.endsWith(".xmi")) {
      handler = getXMIHandler(file);
    } else if (filename.endsWith(".zargo2")) {
      handler = getMdrHandler();
    } else if (filename.endsWith(".ecore")) {
      handler = getEcoreHandler();
    } else if (filename.endsWith(".mdl") || filename.endsWith(".cat")) {
      handler = getRoseHandler();
    }
    return handler;
  }

  /**
   * Returns the XML persistency format handler.
   * 
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
   * 
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
   * 
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
   * 
   * @return the serialize persistency format handler.
   */
  public PersistenceHandler getMdrHandler() {
    return new MdrPersistenceHandler();
  }

  /**
   * Returns the Ecore persistency format handler.
   * 
   * @return the requested persistency handler.
   */
  public PersistenceHandler getEcoreHandler() {
    return null; // TODO
  }

  /**
   * Returns the Javadoc handler persistency format handler.
   * 
   * @return the requested persistency handler.
   */
  public PersistenceHandler getJavaDocHandler() {
    return new XmlPersistenceHandler();
  }

  public PersistenceHandler getRoseHandler() {
    return (PersistenceHandler) ReflectUtilities
        .newInstance("ch.bbv.mda.persistence.eclipse.RosePersistenceHandler");
  }

  /**
   * Decides whether the nsUml or the mdr handler should be returned. for xmi
   * 1.0 the nsUml handler is used for xmi 1.2 the mdr handler is used
   * 
   * @param file
   *          the zargo file
   * @return the zargo handler
   */
  private PersistenceHandler getZargoHandler(File file) {
    PersistenceHandler handler = null;
    try {
      InputStream source = null;
      ZipInputStream input = null;
      FileInputStream inputStream = new FileInputStream(file);

      input = new ZipInputStream(inputStream);
      String name = input.getNextEntry().getName();
      while (!name.endsWith(".xmi")) {
        name = input.getNextEntry().getName();
      }

      source = new BufferedInputStream(input);
      handler = getXMIHandler(source);

    } catch (Exception e) {
      // in case of file not found return the older version
      handler = getNsUmlHandler();
    }
    return handler;

  }

  /**
   * Returns the appropriate xmi handler (nsuml for xmi 1.0 or mdr for xmi 1.2).
   * If file not accessible or otherwise unavailable return handler for xmi 1.0
   * 
   * @param file
   *          to determine xmi version of
   * @return PersistenceHandler to parse file
   */
  private PersistenceHandler getXMIHandler(File file) {
    try {
      return getXMIHandler(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      // do nothing
    }
    return getNsUmlHandler();
  }

  /**
   * Returns the appropriate xmi handler (nsuml for xmi 1.0 or mdr for xmi 1.2).
   * If source unavailable return handler for xmi 1.0
   * 
   * @param source
   *          InputStream to determine xmi version of
   * @return PersistenceHandler to parse file
   */
  private PersistenceHandler getXMIHandler(InputStream source) {
    // read the first 500 characters of the file
    // the part we are looking for should be within the first 120 characters
    int character;
    int counter = 0;
    StringBuffer sb = new StringBuffer();
    try {
      while (counter < 500 && (character = source.read()) != -1) {
        sb.append((char) character);
        counter++;
      }
    } catch (IOException e) {
      // if unable to determine version provide the older handler
      return getNsUmlHandler();
    }
    // pattern: xmi.version = "nummer"
    Pattern pattern = Pattern
        .compile("xmi.version(\\s)*=(\\s)*['|\"]([0-9]+\\.[0-9]+)['|\"]");

    Matcher matcher = pattern.matcher(sb);
    // find matching
    if (matcher.find()) {
      if (matcher.group(3).equals("1.0")) {
        return getNsUmlHandler();
      } else if (matcher.group(3).equals("1.2")) {
        return getMdrHandler();
      } else {
        throw new RuntimeException("XMI Version " + matcher.group(3)
            + " is not supported.");
      }
    }
    return getNsUmlHandler();
  }
}