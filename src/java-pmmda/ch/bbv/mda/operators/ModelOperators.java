/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.operators;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaProperty;

/**
 * The utility class provides utility functions for generators activities.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ModelOperators {
  /**
   * Returns the identifier with its first letter in lowercase.
   * @param identifier identifier to manipulate.
   * @return new representation of the identifier.
   */
  public static String identifierFirstLower(String identifier) {
    assert ((identifier != null) && (identifier.length() > 0));

    StringBuilder buffer = new StringBuilder(identifier);
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

    StringBuilder buffer = new StringBuilder(identifier);
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

    StringBuilder buffer = new StringBuilder();
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
   * @return the context name or null if qualified name is null.
   */
  public static String getContextName(String qualifiedName) {
    if (qualifiedName == null) {
      return null;
    }
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
   * @return the component name or null if qualified name is null.
   */
  public static String getComponentName(String qualifiedName) {
    if (qualifiedName == null) {
      return null;
    }
    int index = qualifiedName.lastIndexOf(".");
    String className = qualifiedName;
    if (index > 0) {
      className = qualifiedName.substring(index + 1);
    }
    return className;
  }

  /**
   * Returns an identifier representation of the qualified name.
   * @param qualifiedName qualified name to transform.
   * @return the transformed qualified name.
   */
  public static String qualifiedNameToIdentifier(String qualifiedName) {
    return qualifiedName.replaceAll(".", "_");
  }

  /**
   * Returns all fields of the given class and of all its ancestors in the given
   * lists based on their type.
   * @param clazz class which fields should be generated.
   * @return properties of the class and its direct and indirect ancestors.
   */
  public List<MetaProperty> getTransitiveFieldsFor(MetaClass clazz) {
    List<MetaProperty> properties = new ArrayList<MetaProperty>();
    properties.addAll(clazz.getProperties());
    for (MetaClass ancestor : clazz.retrieveAncestors()) {
      properties.addAll(getTransitiveFieldsFor(ancestor));
    }
    return properties;
  }

  /**
   * Returns all fields of the given class and of all its ancestors in the given
   * lists based on their type.
   * @param clazz class which fields should be generated.
   * @return properties of the class and its direct and indirect ancestors.
   */
  public List<MetaIndexedProperty> getTransitiveIndexedFieldsFor(MetaClass clazz) {
    List<MetaIndexedProperty> properties = new ArrayList<MetaIndexedProperty>();
    properties.addAll(clazz.getIndexedProperties());
    properties.addAll(clazz.getIndexedProperties());
    for (MetaClass ancestor : clazz.retrieveAncestors()) {
      properties.addAll(getTransitiveIndexedFieldsFor(ancestor));
    }
    return properties;
  }
}
