/* copyright 2000-2004 Marcel Baumann
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
   * Returns the identifier with its first letter in lowercase.
   * @param identifier identifier to manipulate.
   * @return new representation of the identifier.
   */
  public static String identifierFirstLower(String identifier) {
    assert ((identifier != null) && (identifier.length() > 0));

    StringBuffer buffer = new StringBuffer(identifier);
    buffer.setCharAt(0, Character.toLowerCase(buffer.charAt(0)));
    return buffer.toString();
  }

  /**
   * Returns the identifier with its first letter in uppercase.
   * @param identifier identifier to manipulate.
   * @return new representation of the identifier.
   */
  public static String identifierFirstUpper(String identifier) {
    assert ((identifier != null) && (identifier.length() > 0));

    StringBuffer buffer = new StringBuffer(identifier);
    buffer.setCharAt(0, Character.toUpperCase(buffer.charAt(0)));
    return buffer.toString();
  }

  /**
   * Returns the identifier with written uppercase and the seperation of words
   * identified with a uppercase for the first letter is transformed to a
   * underline.
   * @param identifier identifier to manipulate.
   * @return new representation of the identifier.
   */
  public static String identifierUpper(String identifier) {
    assert ((identifier != null) && (identifier.length() > 0));

    StringBuffer buffer = new StringBuffer();
    buffer.append(Character.toUpperCase(identifier.charAt(0)));
    for (int i = 1; i < identifier.length(); i++) {
      char letter = identifier.charAt(i);
      if (Character.isUpperCase(letter)) {
        buffer.append("_");
      }
      buffer.append(Character.toUpperCase(letter));
    }
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

  /**
   * Returns the context part of a qualified class name.
   * @param qualifiedName qualified name to process.
   * @return the context name.
   */
  public static String getContextName(String qualifiedName) {
    assert (qualifiedName != null);

    int index = qualifiedName.lastIndexOf(".");
    String packageName;
    if (index > 0) {
      packageName = qualifiedName.substring(0, index);
    } else {
      packageName = null;
    }
    return packageName;
  }

  /**
   * Returns the component name of a qualified class name.
   * @param qualifiedName qualified name to process.
   * @return the component name.
   */
  public static String getComponentName(String qualifiedName) {
    assert (qualifiedName != null);

    int index = qualifiedName.lastIndexOf(".");
    String className = qualifiedName;
    if (index > 0) {
      className = qualifiedName.substring(index + 1);
    }
    return className;
  }
}