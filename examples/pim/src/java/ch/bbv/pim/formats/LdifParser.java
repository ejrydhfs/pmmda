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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;

import ch.bbv.pim.*;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The LDIF parser parses a list of LDIF records and updates the internal
 * database with the new information. The following rules are defined.
 * <ul>
 * <li>Currently only the LDIF format of the Mozilla Thunderbird exported
 * adress book is supported. The reason is that currently only this format
 * interest us. Seee the history of the project for more details.</li>
 * <li>A contact is equivalent to a record in the database if and only if the
 * the distinguished name and the email address are the same. The distinguised
 * name is composed of the firstname, middlename and lastname.</li>
 * <li>A company is equivalent to a record in the database if and only if the
 * name and URL are the same.</li>
 * <li>The LDIF format indentifies base64 encoded UTF-8 string with two
 * semicolons after the key.</li>
 * </ul>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class LdifParser extends ContactParser {
  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(LdifParser.class);

  /**
   * The mapping betweeng identifiers of the format and properties in the model.
   */
  private static Map<String, String> tokens;

  static {
    tokens = new HashMap<String, String>();
    tokens.put("dn:", null);
    tokens.put("objectclass:", null);
    tokens.put("description:", null);
    tokens.put("givenName:", "firstname");
    tokens.put("sn:", "lastname");
    tokens.put("cn:", null);
    tokens.put("mail:", "workEmail");
    tokens.put("mozillaSecondEmail:", "homeEmail");
    tokens.put("modifytimestamp:", "timezone");
    tokens.put("telephoneNumber:", "workNumber");
    tokens.put("homePhone:", "homeNumber");
    tokens.put("mobile:", "workMobileNumber");
    tokens.put("facsimileTelephoneNumber:", "workFaxNumber");
    tokens.put("homePostalAddresss:", "homeAddress.street");
    tokens.put("mozillaHomeLocalityName:", "homeAddress.city");
    tokens.put("mozillaHomePostalCode:", "homeAddress.postalCode");
    tokens.put("mozillaHomeCountryName:", "homeAddress.country");
    tokens.put("postalAddress:", "workAddress.street");
    tokens.put("mozillaPostalAddress2:", "workAddress.poBox");
    tokens.put("l:", "workAddress.city");
    tokens.put("st:", "workAddress.state");
    tokens.put("postalCode:", "workAddress.postalCode");
    tokens.put("c:", "workAddress.country");
    tokens.put("title:", "title");
    tokens.put("o:", "organization");
    tokens.put("ou:", "department");
    tokens.put("workurl:", "workUrl");
    // tokens.put("custom1", "organization.phoneNumber");
    // tokens.put("custom2", "birthday");
    // tokens.put("custom3", "partnerName");
    // tokens.put("custom4", "boss");
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
      String value;
      if (isBase64Encoded(line)) {
        value = line.substring(index + 2, line.length());
        value = new String(Base64.decodeBase64(value.getBytes()), "UTF-8");
      } else {
        value = (index >= line.length() ? null : line.substring(index + 1, line
            .length()));
      }
      if (tokens.containsKey(prefix)) {
        if ("dn:".equals(prefix)) {
          parseDistinguishedName(value);
        } else if ("description:".equals(prefix)) {
          try {
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
              if (contact.getWorkAddress() == null) {
                contact.setWorkAddress(new Address());
              }
              if ("c:".equals(prefix)) {
                ReflectUtilities.setProperty(contact, property,
                    findCountry(value));
              } else {
                ReflectUtilities.setProperty(contact, property, value);
              }
            } else if (isHomeAddress(prefix)) {
              if (contact.getHomeAddress() == null) {
                contact.setHomeAddress(new Address());
              }
              if ("mozillaHomeCountryName:".equals(prefix)) {
                ReflectUtilities.setProperty(contact, property,
                    findCountry(value));
              } else {
                ReflectUtilities.setProperty(contact, property, value);
              }
            } else if (isUrl(prefix) && (value != null)) {
              ReflectUtilities.setProperty(contact, property, new URL(value));
            } else if ("o:".equals(prefix)) {
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
    int namePos = line.indexOf("cn=");
    int mailPos = line.indexOf("mail=");
    int seperatorPos = line.indexOf(",");
    String distinguishedName = null;
    String email = null;
    if (namePos >= 0) {
      distinguishedName = line.substring(namePos + 3,
          (seperatorPos < 0 ? line.length() : seperatorPos)).trim();
    }
    if (mailPos >= 0) {
      email = line.substring(mailPos + 5, line.length());
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
    return "telephoneNumber:".equals(key) || "homePhone:".equals(key)
        || "mobile:".equals(key) || "facsimileTelephoneNumber:".equals(key);
  }

  /**
   * Returns true if the tag represents a home address otherwise false.
   * @param key tag to check.
   * @return Flag indicating if the tag is a home address.
   */
  private boolean isHomeAddress(String key) {
    return "homePostalAddresss:".equals(key)
        || "mozillaHomeLocalityName:".equals(key)
        || "mozillaHomePostalCode:".equals(key)
        || "mozillaHomeCountryName:".equals(key);
  }

  /**
   * Returns true if the tag represents a work address otherwise false.
   * @param key tag to check.
   * @return Flag indicating if the tag is a work address.
   */
  private boolean isWorkAddress(String key) {
    return "postalAddress:".equals(key) || "mozillaPostalAddress2:".equals(key)
        || "l:".equals(key) || "st:".equals(key) || "postalCode:".equals(key)
        || "c:".equals(key);
  }

  /**
   * Returns true if the tag represents a URL otherwise false.
   * @param key tag to check.
   * @return Flag indicating if the tag is a URL.
   */
  private boolean isUrl(String key) {
    return "workurl:".equals(key);
  }

  private boolean isBase64Encoded(String line) {
    return line.indexOf(":: ") != -1;
  }
}
