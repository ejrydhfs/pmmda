/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.utilities;

import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * The property utilities class provides convenience methods to increase the
 * usefulness of properties.
 * @author Marcel Baumann
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
   * Converts a properties configuration into a regular Java properties object.
   * All variables are expanded.
   * @param propertiesConfiguration properties configuration to convert.
   * @return the converted properties object.
   */
  public static Properties convert(
      PropertiesConfiguration propertiesConfiguration) {
    Properties properties = new Properties();
    for (Iterator i = propertiesConfiguration.getKeys(); i.hasNext();) {
      String key = (String) i.next();
      String value = propertiesConfiguration.getString(key).toString();
      properties.setProperty(key, value);
    }
    return properties;
  }

  /**
   * Returns the value of the attribute in the first element with the given
   * identifier value.
   * @param configuration configuration containing the attribute to retrieve.
   * @param path path containing the elements to look for.
   * @param attributeId attribute identifying the identifier value.
   * @param id identifier value looked for.
   * @param attribute the attribute to retrieve in the element with the given
   *          identifier value.
   * @return the requested attribute value if found otherwise null.
   * @pre (configuration != null) && (path != null) && (attributeId != null) &&
   *      (id != null) && (attribute != null)
   */
  public static String getAttribute(CompositeConfiguration configuration,
      String path, String attributeId, String id, String attribute) {
    assert (configuration != null) && (path != null) && (attributeId != null)
        && (id != null) && (attribute != null);
    String value = null;
    StringBuilder buffer = new StringBuilder();
    buffer.append(path);
    for (int i = 0;; i++) {
      String name = getAttributeAt(configuration, buffer, i, attributeId);
      if (name == null) {
        break;
      }
      if (id.equals(name)) {
        value = getAttributeAt(configuration, buffer, i, attribute);
        break;
      }
    }
    return value;
  }

  /**
   * Returns the requested attribute at the given index.
   * @param configuration configuration containing the attribue.
   * @param path path to the list of elements.
   * @param index index of the elements.
   * @param attribute attribute to retrieve.
   * @return the value of the attribute if found otherwise null.
   * @pre (configuration != null) && (path != null) && (index >= 0) &&
   *      (attribute != null)
   */
  public static String getAttributeAt(CompositeConfiguration configuration,
      StringBuilder path, int index, String attribute) {
    assert (configuration != null) && (path != null) && (index >= 0)
        && (attribute != null);
    int length = path.length();
    path.append("(").append(index).append(")[@").append(attribute).append("]");
    String value = configuration.getString(path.toString());
    path.setLength(length);
    return value;
  }
}