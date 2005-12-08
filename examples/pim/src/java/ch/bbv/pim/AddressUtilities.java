/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim;

/**
 * Utilities to manipulate contact instances.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class AddressUtilities {
  /**
   * Creates a unique distinghished name for a contact. The uniqueness is
   * insured by the composition of the name and the email address.
   * @param contact contact which distinguished name should be created.
   * @return the distinguished name.
   */
  static public String createDistinguishedName(Contact contact) {
    StringBuilder buffer = new StringBuilder();
    String value = createPrefix(contact);
    if (value != null) {
      buffer.append(value).append(" ");
    }
    value = contact.getFirstname();
    if (value != null) {
      buffer.append(value).append(" ");
    }
    value = contact.getMiddlename();
    if (value != null) {
      buffer.append(value).append(" ");
    }
    value = contact.getLastname();
    if (value != null) {
      buffer.append(value);
    }
    return buffer.toString();
  }

  /**
   * Creates the prefix of the contact.
   * @param contact contact which prefix should be created.
   * @return the prefix of the contact.
   */
  static public String createPrefix(Contact contact) {
    String prefix = null;
    Language language = contact.getLanguage();
    if (language != null) {
      switch (language.getCode()) {
      case Language.ENGLISH_ID:
        prefix = contact.isMale() ? "Sir" : "Mrs";
        break;
      case Language.GERMAN_ID:
        prefix = contact.isMale() ? "Herr" : "Frau";
        break;
      case Language.FRENCH_ID:
        prefix = contact.isMale() ? "Monsieur" : "Madame";
        break;
      case Language.ITALIAN_ID:
        prefix = contact.isMale() ? "Signor" : "Signora";
        break;
      }
    }
    return prefix;
  }
}
