/* 
 * copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.utilities;

/**
 * A set of convenience methods to manipulate identifiers and transform them to
 * respect the coding guidelines of the Java programming language. The methods
 * only works for the English language.
 * @author MarcelBaumann
 * @version $Revision$
 */
public final class TextUtilities {

  /**
   * Default constructor of a static class declared as private.
   */
  private TextUtilities() {
    // private constructor of static class
  }

  /**
   * Removes all special characters from an identifier.
   * @param identifier identifier to clean-up.
   * @return legal identifiers after removal of special characters.
   */
  public static String removeSpecialCharacters(String identifier) {
    String tmp = identifier.toLowerCase();
    String result = "";
    for (int i = 0; i < tmp.length(); i++) {
      if (tmp.charAt(i) >= 'a' && tmp.charAt(i) <= 'z') {
        result += identifier.charAt(i);
      }
    }
    return result;
  }

  /**
   * Returns the identifier with its first letter in uppercase.
   * @param identifier identifier to manipulate.
   * @return new representation of the identifier.
   */
  public static String identifierFirstUpper(String identifier) {
    assert ((identifier != null) && (identifier.length() > 0));

    StringBuilder buffer = new StringBuilder(identifier);
    buffer.setCharAt(0, Character.toUpperCase(buffer.charAt(0)));
    return buffer.toString();
  }

  /**
   * Returns the identifier in the singular form.
   * @param identifier identifier to manipulate.
   * @return new representation of the identifier.
   */
  public static String identifierSingular(String identifier) {
    assert (identifier != null);

    String name;
    if (identifier.endsWith("ies")) {
      name = identifier.substring(0, identifier.length() - 3) + "y";
    } else if (identifier.endsWith("ses")) {
      name = identifier.substring(0, identifier.length() - 2);
    } else if (identifier.endsWith("es")) {
      name = identifier.substring(0, identifier.length() - 1);
    } else if (identifier.endsWith("ss")) {
      name = identifier;
    } else if (identifier.endsWith("s")) {
      name = identifier.substring(0, identifier.length() - 1);
    } else {
      name = identifier;
    }
    return name;
  }

  /**
   * Returns the identifier in the plural form.
   * @param identifier identifier to manipulate.
   * @return new representation of the identifier.
   */
  public static String identifierPlural(String identifier) {
    assert (identifier != null);

    String name;
    if (identifier.endsWith("y")) {
      name = identifier.substring(0, identifier.length() - 1) + "ies";
    } else if (identifier.endsWith("s")) {
      name = identifier;
    } else {
      name = identifier + "s";
    }
    return name;
  }

}