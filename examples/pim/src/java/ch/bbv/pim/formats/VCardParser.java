/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.formats;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;

import ch.bbv.pim.*;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The VCard parser parses a list of VCARD records and updates the internal
 * database with the new information. The following rules are defined.
 * <ul>
 * <li>A contact is equivalent to a record in the database if and only if the
 * the distinguished name and the email address are the same. The distinguised
 * name is composed of the firstname, middlename and lastname. The challenge is
 * that both information are only available late during the parsing process.</li>
 * <li>A company is equivalent to a record in the database if and only if the
 * name and URL are the same.</li>
 * </ul>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class VCardParser extends ContactParser {
  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(VCardParser.class);

  /**
   * The mapping betweeng identifiers of the format and properties in the model.
   */
  private static Map<String, String> tokens;

  static {
    tokens = new HashMap<String, String>();
    tokens.put("begin:vcard", null);
    tokens.put("version:", null);
    tokens.put("fn:", null);
    tokens.put("n:", null);
    tokens.put("title:", "title");
    tokens.put("org:", "");
    tokens.put("url:", "workUrl");
    tokens.put("adr;type=work:", "");
    tokens.put("label;type=work:", null);
    tokens.put("adr;type=home:", "");
    tokens.put("label;type=home:", null);
    tokens.put("tel;type=work:", "workNumber");
    tokens.put("tel;type=home:", "homeNumber");
    tokens.put("tel;type=cell:", "workMobileNumber");
    tokens.put("email;type=internet,pref:", "workEmail");
    tokens.put("email;type=internet:", "homeEmail");
    tokens.put("note:", null);
    tokens.put("end:vcard", null);
  }

  /**
   * The currently process contact.
   */
  private Contact contact;

  @Override
  protected void parseContact(AddressBook book) throws IOException {
    String line = readLine();
    while (line.length() == 0) {
      line = readLine();
    }
    while (line.length() != 0) {
      int index = line.indexOf(":") + 1;
      String prefix = line.substring(0, index);
      String value = (index >= line.length() ? null : line.substring(index,
          line.length()));
      if (tokens.containsKey(prefix)) {
        if ("fn:".equals(prefix)) {
          parseDistinguishedName(value);
        } else if ("n:".equals(prefix)) {
          parseName(value);
        } else if ("note:".equals(prefix)) {
          try {
            StringBuilder notes = new StringBuilder(value);
            while (!value.contains("</notes>")) {
              value = readLine();
              notes.append(value);
            }
            value = notes.toString();
            parseDescription(contact, value);
          } catch (JDOMException e) {
            log.error("Notes have erroneous format", e);
          } catch (ParseException e) {
            log.error("Notes have erroneous format", e);
          }
        } else {
          String property = tokens.get(prefix);
          if (property != null) {
            if (isPhoneNumber(prefix) && (value != null)) {
              ReflectUtilities.setProperty(contact, property, new PhoneNumber(
                  value));
            } else if (isWorkAddress(prefix)) {
              Address address = contact.getWorkAddress();
              if (address == null) {
                address = new Address();
                contact.setWorkAddress(address);
              }
              extract(address, value);
            } else if (isHomeAddress(prefix)) {
              Address address = contact.getHomeAddress();
              if (address == null) {
                address = new Address();
                contact.setHomeAddress(address);
              }
              extract(address, value);
            } else if (isUrl(prefix) && (value != null)) {
              ReflectUtilities.setProperty(contact, property, new URL(value));
            } else if ("org:".equals(prefix)) {
              if (contact.getOrganization() == null) {
                Organization organization = book.findOrganization(value);
                if (organization == null) {
                  organization = new Organization();
                  organization.setName(value);
                  book.addOrganization(organization);
                }
                organization.addContact(contact);
                contact.setOrganization(organization);
              }
            } else {
              ReflectUtilities.setProperty(contact, property,
                  value != null ? value.trim() : value);
            }
          }
        }
      }
      line = readLine();
    }
  }

  /**
   * Parses the distinguished name and retrieves the associated contact from the
   * database. If not found a new one is created. A distinguished name only
   * contain a name or an email address.
   * @param line line containing the distinguished name.
   */
  private void parseDistinguishedName(String line) {
    int mailPos = line.indexOf("[");
    String distinguishedName = null;
    String email = null;
    if (mailPos >= 0) {
      int endPos = line.lastIndexOf("]");
      email = line.substring(mailPos + 1, (endPos > mailPos ? endPos : line
          .length()));
    }
    if ((mailPos == -1) || (mailPos > 0)) {
      distinguishedName = line.substring(0, (mailPos > 0 ? mailPos - 1 : line
          .length()));
    }
    contact = getBook().findContact(distinguishedName, email);
    if (contact == null) {
      contact = new Contact();
      log.info("Adding contact " + line);
      setLanguageAndGender(contact, distinguishedName);
    }
  }

  /**
   * Returns true if the tag represents a phone number otherwise false.
   * @param key tag to check.
   * @return Flag indicating if the tag is a phone number.
   */
  private boolean isPhoneNumber(String key) {
    return "tel;type=work:".equals(key) || "tel;type=home:".equals(key)
        || "tel;type=cell:".equals(key)
        || "facsimileTelephoneNumber:".equals(key);
  }

  /**
   * Returns true if the tag represents a home address otherwise false.
   * @param key tag to check.
   * @return Flag indicating if the tag is a home address.
   */
  private boolean isHomeAddress(String key) {
    return "adr;type=home:".equals(key);
  }

  /**
   * Returns true if the tag represents a work address otherwise false.
   * @param key tag to check.
   * @return Flag indicating if the tag is a work address.
   */
  private boolean isWorkAddress(String key) {
    return "adr;type=work:".equals(key);
  }

  /**
   * Returns true if the tag represents a URL otherwise false.
   * @param key tag to check.
   * @return Flag indicating if the tag is a URL.
   */
  private boolean isUrl(String key) {
    return "url:".equals(key);
  }

  /**
   * PO Box 2210;;Blumenrain 10;Luzern;;6002;Switzerland
   * @param address adress to complete.
   * @param line line to parse.
   */
  private void extract(Address address, String line) {
    try {
      StringTokenizer tokenizer = new StringTokenizer(line, ";", true);
      String value = tokenizer.nextToken();
      if (!";".equals(value) && (value.trim().length() > 0)) {
        address.setPoBox(value);
        value = tokenizer.nextToken();
      }
      value = tokenizer.nextToken();
      if (!";".equals(value) && (value.trim().length() > 0)) {
        value = tokenizer.nextToken();
      }
      value = tokenizer.nextToken();
      if (!";".equals(value) && (value.trim().length() > 0)) {
        address.setStreet(value);
        value = tokenizer.nextToken();
      }
      value = tokenizer.nextToken();
      if (!";".equals(value) && (value.trim().length() > 0)) {
        address.setCity(value);
        value = tokenizer.nextToken();
      }
      value = tokenizer.nextToken();
      if (!";".equals(value) && (value.trim().length() > 0)) {
        address.setState(value);
        value = tokenizer.nextToken();
      }
      value = tokenizer.nextToken();
      if (!";".equals(value) && (value.trim().length() > 0)) {
        address.setPostalCode(value);
        value = tokenizer.nextToken();
      }
      value = tokenizer.nextToken();
      if (!";".equals(value) && (value.trim().length() > 0)) {
        address.setCountry(findCountry(value));
      }
    } catch (NoSuchElementException e) {
      // end of address string reached before completing processing
      log.debug(line, e);
    }
  }

  /**
   * Parses the name of the contact from the VCARD name field "n:".
   * @param line line to parse.
   */
  private void parseName(String line) {
    try {
      StringTokenizer tokenizer = new StringTokenizer(line, ";", true);
      String value = tokenizer.nextToken();
      if (!";".equals(value) && (value.trim().length() > 0)) {
        contact.setLastname(value);
        value = tokenizer.nextToken();
      }
      value = tokenizer.nextToken();
      if (!";".equals(value) && (value.trim().length() > 0)) {
        contact.setFirstname(value);
        value = tokenizer.nextToken();
      }
      value = tokenizer.nextToken();
      if (!";".equals(value) && (value.trim().length() > 0)) {
        contact.setMiddlename(value);
        value = tokenizer.nextToken();
      }
      value = tokenizer.nextToken();
      // ignore the title of the person, already parsed in the distinguished
      // name
      value = tokenizer.nextToken();
    } catch (NoSuchElementException e) {
      // end of address string reached before completing processing
      log.debug(line, e);
    }
  }
}
