/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.formats;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.pim.AddressBook;
import ch.bbv.pim.Contact;
import ch.bbv.pim.Organization;
import ch.bbv.utilities.PropertyUtilities;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The text emitter generates a text based file of records. Each record contains
 * a contact information and the association organization. The format of the
 * text file is specified in a XML configuration file.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class TextEmitter extends ContactEmitter {
  /**
   * logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(TextEmitter.class);

  private Map<String, String> contactProperties;

  private List<String> columns;

  private char seperator = '\t';

  /**
   * Constructor of the class.
   * @param configuration XML configuration file for the generator.
   */
  public TextEmitter(File configuration) {
    assert (configuration.exists() && configuration.canRead());
    contactProperties = new HashMap<String, String>();
    columns = new ArrayList<String>();
    CompositeConfiguration config = new CompositeConfiguration();
    try {
      config.addConfiguration(new XMLConfiguration(configuration));
      extract(config, "contact.property", contactProperties, columns);
    } catch (ConfigurationException e) {
      log.error(e);
    }
  }

  @Override
  public void emit(AddressBook book, File records) throws IOException {
    assert (book != null)
        && ((records != null) && (!records.exists() || records.canWrite()));
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(records)));
    for (String column : columns) {
      writer.append(column).append(seperator);
    }
    writer.newLine();
    for (Organization organization : book.getOrganizations()) {
      for (Contact contact : organization.getContacts()) {
        for (String column : columns) {
          String property = contactProperties.get(column);
          if (property != null) {
            Object value = ReflectUtilities.getProperty(contact, property);
            writer.append(value != null ? value.toString() : "").append(
                seperator);
          }
        }
        writer.newLine();
      }
    }
    writer.close();
  }

  private void extract(CompositeConfiguration config, String path,
      Map<String, String> mappings, List<String> columns) {
    StringBuilder buffer = new StringBuilder();
    buffer.append(path);
    for (int i = 0;; i++) {
      String property = PropertyUtilities.getAttributeAt(config, buffer, i,
          "property");
      if (property == null) {
        break;
      }
      String column = PropertyUtilities.getAttributeAt(config, buffer, i,
          "column");
      mappings.put(column, property);
      columns.add(column);
    }

  }
}
