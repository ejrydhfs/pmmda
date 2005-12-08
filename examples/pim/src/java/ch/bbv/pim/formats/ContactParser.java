/*
 * copyright 2000-2005 Marcel Baumann Licensed under the Apache License, Version
 * 2.0 (the "Licence"); you may not use this file except in compliance with the
 * License. $Id$
 */
package ch.bbv.pim.formats;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import ch.bbv.dog.reference.ReferenceCodeUtilities;
import ch.bbv.pim.*;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The abstract class for all parsers of contacts descriptions.
 * @author MarcelBaumann
 * @version $Revision$
 */
public abstract class ContactParser {
  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(ContactParser.class);

  /**
   * Date format of the date field in the notes.
   */
  private static SimpleDateFormat format1 = new SimpleDateFormat(
      "yyyy-MM-dd HH:mm:ss");

  /**
   * Date format of the date field in the notes.
   */
  private static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * The default country used in the parser.
   */
  private Country defaultCountry;

  /**
   * The default language used in the parser.
   */
  private Language defaultLanguage;

  /**
   * The reader of the file to import.
   */
  private BufferedReader reader;

  /**
   * The address book being synchronized.
   */
  private AddressBook book;

  /**
   * The time when the parsing process was started. The value is used to insure
   * that all default time values for all parsed contacts are the same.
   */
  private Date now;

  /**
   * Synchronizes the address book with the contact records stored in the file.
   * @param book address book to synchronize.
   * @param records file containing the new contact records.
   * @throws IOException if the file manipulation encountered a problem.
   * @pre (book != null) && ((records != null) && records.canRead())
   */
  public void synchronize(AddressBook book, File records) throws IOException {
    assert (book != null) && ((records != null) && records.canRead());
    reader = new BufferedReader(new InputStreamReader(new FileInputStream(
        records)));
    this.book = book;
    now = new Date();
    try {
      while (true) {
        parseContact(book);
      }
    } catch (IOException e) {
      log.info("End Of File reached");
    } finally {
      reader.close();
    }
  }

  /**
   * Returns the default country used in the parser.
   * @return the default country.
   * @see #setDefaultCountry(Country)
   */
  public Country getDefaultCountry() {
    return defaultCountry;
  }

  /**
   * Sets the default country of the parser.
   * @param defaultCountry The defaultCountry to set.
   * @see #getDefaultCountry()
   */
  public void setDefaultCountry(Country defaultCountry) {
    this.defaultCountry = defaultCountry;
  }

  /**
   * Returns the default language used in the parser.
   * @return the defaultLanguage.
   * @see #setDefaultLanguage(Language)
   */
  public Language getDefaultLanguage() {
    return defaultLanguage;
  }

  /**
   * Sets the default language of the parser.
   * @param defaultLanguage the defaultLanguage to set.
   * @see #getDefaultLanguage()
   */
  public void setDefaultLanguage(Language defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
  }

  /**
   * Parses one contact from the file containing a set of contact.
   * @param book address book to synchronize with.
   * @throws IOException if an error occured when reading the contact from the
   *           file.
   * @pre book != null
   */
  protected abstract void parseContact(AddressBook book) throws IOException;

  /**
   * Finds the country with the given short description.
   * @param country name of the country to find.
   * @return the requested country if found otherwise null.
   */
  protected Country findCountry(String country) {
    if (country != null) {
      return (Country) ReferenceCodeUtilities.find(Country.getCodes(), country);
    }
    return null;
  }

  protected String readLine() throws IOException {
    String line = reader.readLine();
    if (line == null) {
      throw new IOException("End of File");
    }
    return line.trim();
  }

  /**
   * Returns the addresss book.
   * @return the address book being synchronized.
   */
  protected AddressBook getBook() {
    return book;
  }

  /**
   * Sets the string property with the new value. Empty strings default to null
   * @param object object which property should be set.
   * @param property property to set.
   * @param value new value of the property.
   */
  protected void setProperty(Object object, String property, String value) {
    if (value != null) {
      value = value.trim();
      if (value.length() == 0) {
        value = null;
      }
    }
    ReflectUtilities.setProperty(object, property, value);

  }

  protected void addToOrganization(Contact contact, String name) {
    if (contact.getOrganization() == null) {
      Organization organization = book.findOrganization(name);
      if (organization == null) {
        organization = new Organization();
        organization.setName(name);
        book.addOrganization(organization);
      }
      organization.addContact(contact);
    }
  }

  /**
   * The language and gender of the contact is inferred based on the prefix he
   * is given.
   * @param contact contact to complete.
   * @param line line containing the prefix information.
   * @pre contact != null
   */
  protected void setLanguageAndGender(Contact contact, String line) {
    assert contact != null;
    if (line != null) {
      if (line.indexOf("Frau ") != -1) {
        contact.setMale(false);
        contact.setLanguage(Language.GERMAN);
      } else if (line.indexOf("Herr ") != -1) {
        contact.setMale(true);
        contact.setLanguage(Language.GERMAN);
      } else if (line.indexOf("Monsieur ") != -1) {
        contact.setMale(true);
        contact.setLanguage(Language.FRENCH);
      } else if ((line.indexOf("Madame ") != -1)
          || (line.indexOf("Mademoiselle") != -1)) {
        contact.setMale(false);
        contact.setLanguage(Language.FRENCH);
      } else if (line.indexOf("Mrs ") != -1) {
        contact.setMale(false);
        contact.setLanguage(Language.ENGLISH);
      } else if ((line.indexOf("Mister ") != -1) || (line.indexOf("Mr ") != -1)) {
        contact.setMale(true);
        contact.setLanguage(Language.ENGLISH);
      }
    }
  }

  /**
   * Parses the description and updates the journal entries of the contact. The
   * notes are a XML structure cotaining a list of notes. A note has the
   * following attributes.
   * <dl>
   * <dt>date</dt>
   * <dd>the date and time when the note was taken. The format is either ISO
   * 8601 standard YYYY-MM-DD</dd>
   * <dt>duration</dt>
   * <dd>optional attribute defining the duration of the note event in seconds.</dd>
   * <dt>author</dt>
   * <dd>the author of the note event as string.</dd>
   * <dt>type</dt>
   * <dd>enumeration of note types: email, phone, meeting, mailing, , letter,
   * exhibition</dd>
   * </dl>
   * @param contact contact to which the journal entries should be added.
   * @param line line containing the description.
   * @throws IOException if an IO problem occurs.
   * @throws JDOMException if a XML parsing error occurs.
   * @throws ParseException if a date format is erroneous.
   * @pre contact != null
   */
  protected void parseDescription(Contact contact, String line)
      throws JDOMException, IOException, ParseException {
    if ((line == null) || (line.trim().length() == 0)) {
      return;
    }
    SAXBuilder builder = new SAXBuilder();
    Document document = builder.build(new StringReader(line));
    List<Element> notes = document.getRootElement().getChildren("note");
    for (Element element : notes) {
      JournalEntry entry = new JournalEntry();
      entry.setType(JournalEntryType.NOTE);
      entry.setCreation(now);
      entry.setCreation(transform(element.getAttributeValue("date")));
      Attribute duration = element.getAttribute("duration");
      if (duration != null) {
        entry.setDuration(duration.getIntValue());
      }
      entry.setAuthor(element.getAttributeValue("author"));
      entry.setText(element.getText());
      contact.addEntry(entry);
    }
  }

  /**
   * transforms the string representation into a date.
   * @param text text to transform.
   * @return the transformed date.
   * @throws ParseException if the text has a wrong format.
   */
  private Date transform(String text) throws ParseException {
    try {
      return format1.parse(text);
    } catch (ParseException e) {
      return format2.parse(text);
    }
  }
}
