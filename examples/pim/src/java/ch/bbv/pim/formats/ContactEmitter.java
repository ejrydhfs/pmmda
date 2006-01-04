/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.formats;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import ch.bbv.pim.AddressBook;
import ch.bbv.pim.JournalEntry;

/**
 * The abstract class for all emitters of contacts descriptions.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class ContactEmitter {
  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(ContactEmitter.class);

  /**
   * All date format should be stored as ISO formatted dates.
   */
  private SimpleDateFormat format;

  /**
   * Emits the given address book into the file
   * @param book address book to emit.
   * @param records file in which the records should be written.
   * @throws IOException if an error occured when writing the file.
   * @pre (book != null) && (records != null)
   */
  public abstract void emit(AddressBook book, File records) throws IOException;

  /**
   * Default constructor of the class.
   */
  public ContactEmitter() {
    format = new SimpleDateFormat("yyyy-MM-dd");
  }

  /**
   * Transforms a list of journal entries into their corresponding XML
   * representation.
   * @param entries entries to transform.
   * @param prettyPrinting flag indicating if notes should be printed printed.
   *          If the flag is false the content of the notes does not contain any
   *          new lines. These characters are also removed from the note text.
   * @return the XML representation of the journal entries.
   * @pre entries != null
   */
  public String transform(List<JournalEntry> entries, boolean prettyPrinting) {
    assert entries != null;
    char separator = prettyPrinting ? '\n' : ' ';
    StringBuilder buffer = new StringBuilder();
    buffer.append("<notes>").append(separator);
    for (JournalEntry entry : entries) {
      buffer.append("<note");
      append(buffer, "date", format.format(entry.getCreation()));
      append(buffer, "type", entry.getType().getShortDescription());
      int duration = entry.getDuration();
      if (duration > 0) {
        append(buffer, "duration", Integer.toString(duration));
      }
      append(buffer, "author", entry.getAuthor());
      buffer.append(">").append(entry.getText().replace("\n", " "));
      buffer.append("</note>").append(separator);
    }
    buffer.append("</notes>");
    return buffer.toString();
  }

  /**
   * Processes the given velocity template with the given context.
   * @param template template to process.
   * @param context context of the template to process.
   * @return the result of the template processing as string.
   * @pre (template != null) && (context != null)
   */
  protected String processTemplate(Template template, VelocityContext context) {
    assert (template != null) && (context != null);
    StringWriter writer = new StringWriter();
    try {
      template.merge(context, writer);
    } catch (MethodInvocationException e) {
      log.error(e);
    } catch (ParseErrorException e) {
      log.error(e);
    } catch (ResourceNotFoundException e) {
      log.error(e);
    } catch (Exception e) {
      log.error(e);
    }
    return writer.toString();
  }

  /**
   * Loads the velocity template with the given name.
   * @param name name of the template to load.
   * @return the template if found otherwise null.
   * @pre name != null
   */
  protected Template loadTemplate(String name) {
    Template template = null;
    try {
      template = Velocity.getTemplate(name);
    } catch (ParseErrorException e) {
      log.error(e);
    } catch (ResourceNotFoundException e) {
      log.error(e);
    } catch (Exception e) {
      log.error(e);
    }
    return template;
  }

  /**
   * Appends a key value pair if the value is not null.
   * @param buffer buffer in which the pair is added.
   * @param key key to add.
   * @param value value to add if not null.
   */
  private void append(StringBuilder buffer, String key, String value) {
    if ((value != null) && (value.trim().length() > 0)) {
      buffer.append(" ").append(key).append("=\"").append(value).append("\"");
    }
  }
}
