/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.utilities;

import java.util.Iterator;
import java.util.Properties;

/**
 * The property utilities class provides convenience methods to increase the
 * usefulness of properties.
 * @author marcelbaumann
 * @version $Revision$
 */
public final class PropertyUtilities {
  /**
   * private constructor for a static class.
   */
  private PropertyUtilities() {
    // private constructor of static class
  }

  /**
   * Returns true if at least one property contains a variable.
   * @param properties properties to check.
   * @return true if a variable was found otherwise false.
   * @pre properties != null
   */
  public static boolean containsVariables(Properties properties) {
    assert (properties != null);

    boolean contains = false;
    for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
      if (containsVariable(properties.getProperty((String) i.next()))) {
        contains = true;
        break;
      }
    }
    return contains;
  }

  /**
   * Expands the properties values and resolves all variable names. A property
   * can contain variables defined as ${name} where the characters of name are
   * [azAZ_-]. Nested variables are not supported. Each variable must have a
   * value. No circular dependencies should be defined.
   * @param properties properties containing variables to expand.
   * @return the expanded properties.
   * @pre properties != null
   */
  public static Properties expand(Properties properties) {
    assert (properties != null);
    Properties expandedProperties = new Properties();
    for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();
      String value = properties.getProperty(key);
      if (!containsVariable(value)) {
        expandedProperties.setProperty(key, value);
      }
    }
    // resolve the variables if possible.
    boolean solved = true;
    while ((properties.size() > expandedProperties.size()) && solved) {
      solved = false;
      for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
        String key = (String) i.next();
        if (!expandedProperties.containsKey(key)) {
          String value = expand(properties.getProperty(key), expandedProperties);
          if (value != null) {
            solved = true;
            expandedProperties.setProperty(key, value);
          }
        }
      }
    }

    // copy over the unresolved properties
    if (properties.size() != expandedProperties.size()) {
      for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
        String key = (String) i.next();
        if (!expandedProperties.containsKey(key)) {
          expandedProperties.setProperty(key, properties.getProperty(key));
        }
      }
    }
    return expandedProperties;
  }

  /**
   * Returns true if the property contains at least one variable.
   * @param property property to parse.
   * @return true if the property contains a variable otherwise false.
   * @pre property != null
   */
  public static boolean containsVariable(String property) {
    assert (property != null);
    return getVariable(property, 0) != null;
  }

  /**
   * Expands the property and replace all variables with their values. If at
   * least one variable cannot be resolved return null.
   * @param property property to expand.
   * @param properties properties containing all the currently known values.
   * @return the expanded property if sucessful otherwise null.
   */
  private static String expand(String property, Properties properties) {
    String expandedProperty = property;
    int fromIndex = 0;
    String variable = getVariable(property, fromIndex);
    while (variable != null) {
      fromIndex = property.indexOf(variable) + variable.length();

      String value = properties.getProperty(getVariableName(variable));
      if (value != null) {
        expandedProperty = expandedProperty.replaceAll(
            transformVariableToRegex(variable), value);
      } else {
        expandedProperty = null;
        break;
      }
      variable = getVariable(property, fromIndex);
    }
    return expandedProperty;
  }

  /**
   * Returns the next variable of the property starting at the given position.
   * @param property the property to search.
   * @param fromIndex the index to start the search from.
   * @return the next variable if found otherwise null.
   */
  private static String getVariable(String property, int fromIndex) {
    String variable = null;
    int index = property.indexOf("${", fromIndex);
    if (index != -1) {
      int counter = 1;
      int pos = index + 2;
      while ((pos < property.length()) && (counter > 0)) {
        switch (property.charAt(pos)) {
        case '{':
          counter++;
          break;
        case '}':
          counter--;
          break;
        }
        pos++;
      }
      if (counter == 0) {
        variable = property.substring(index, pos);
      }
    }
    return variable;
  }

  /**
   * Transforms a variable name into the compatible regular expression
   * equivalent.
   * @param variable variable to transform.
   * @return the regular expression representation of the variable.
   */
  private static String transformVariableToRegex(String variable) {
    StringBuilder buffer = new StringBuilder();
    buffer.append('\\').append('$').append('\\').append('{').append(
        getVariableName(variable)).append('\\').append('}');
    return buffer.toString();
  }

  /**
   * Returns the name of the given variable.
   * @param variable variable which name should be extracted.
   * @return the name of the variable.
   */
  private static String getVariableName(String variable) {
    return variable.substring(2, variable.length() - 1);
  }
}