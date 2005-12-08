/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.formats;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.app.Velocity;

import ch.bbv.application.Component;
import ch.bbv.application.ComponentImpl;

/**
 * The format manager associates parsers and emitters with the extensions of the
 * files they support.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class FormatManager extends ComponentImpl implements Component {
  /**
   * Name of the component.
   */
  public static final String NAME = "Format Manager";

  /**
   * Registered emitters for the given extensions.
   */
  private Map<String, ContactEmitter> emitters;

  /**
   * Registered parsers for the given extensions.
   */
  private Map<String, ContactParser> parsers;

  /**
   * Default constructor of the class.
   * @throws Exception if Velocity initialization encountered a problem.
   */
  public FormatManager() throws Exception {
    super(NAME);
    emitters = new HashMap<String, ContactEmitter>();
    parsers = new HashMap<String, ContactParser>();
    Properties properties = new Properties();
    properties.setProperty("file.resource.cache", "false");
    properties.setProperty("file.resource.loader.path", "./classes");
    properties.setProperty("parser.pool.size", "10");
    Velocity.init(properties);
  }

  public ContactEmitter getEmitter(String filename) {
    String extension = filename.substring(filename.indexOf("."), filename
        .length());
    return emitters.get(extension);
  }

  public Collection<ContactParser> getParsers() {
    return parsers.values();
  }

  public ContactParser getParser(String filename) {
    String extension = filename.substring(filename.indexOf("."), filename
        .length());
    return parsers.get(extension);
  }

  public void register(ContactEmitter emitter, String extension) {
    emitters.put(extension, emitter);
  }

  public void register(ContactParser parser, String extension) {
    parsers.put(extension, parser);
  }
}
