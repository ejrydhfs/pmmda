/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.utilities;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author AdrianBachofen Implements a simple handler for dealing with resources
 *         and localization
 */
public class ResourceUtilities {
  /**
   * Logger of the application class.
   */
  private static Log log = LogFactory.getLog(ResourceUtilities.class);

  public static ResourceBundle resourceBundle;

  /**
   * loads all resources from the resource bundle for the default locale into
   * the static fields of the provided properties class. The key of the resource
   * must be the name of the field. The algorithm is case sensitive.
   * @param qualifiedName qualified name of the resource bundle to load
   * @param propertiesClass the class which static field should be updated
   */
  public static void loadMessages(String qualifiedName, Class propertiesClass) {
    ResourceBundle bundle = ResourceBundle.getBundle(qualifiedName, Locale
        .getDefault());
    if (bundle != null) {
      loadMessages(bundle, propertiesClass);
    }
  }

  /**
   * loads all resources from a resource bundle into the static fields of the
   * provided properties class. The key of the resource must be the name of the
   * field. The algorithm is case sensitive.
   * @param bundle bundle containing the values to update
   * @param propertiesClass the class which static field should be updated
   */
  public static void loadMessages(ResourceBundle bundle, Class propertiesClass) {
    Map<String, String> messages = new HashMap<String, String>();
    for (Enumeration<String> i = bundle.getKeys(); i.hasMoreElements();) {
      String key = i.nextElement();
      String value = bundle.getString(key);
      messages.put(key, value);
    }
    try {
      for (Field field : propertiesClass.getFields()) {
        String key = field.getName();
        Object defaultValue = field.get(messages);
        Object resource = null;
        if (field.getType() == String.class) {
          resource = messages.get(key);
          if (resource == null) {
            log.error("Could not find resource for field " + field.getName());
          }
          field.set(null, resource);
        } else if (field.getType() == String[].class) {
          String[] defaultArray = (String[]) defaultValue;
          List<String> stringList = new ArrayList<String>();
          for (int i = 0;; i++) {
            String string = messages.get(key + "_" + i);
            if (string == null) {
              break;
            }
            stringList.add(string);
          }
          if (stringList.size() != defaultArray.length) {
            log.error("Array size erroneous for field " + field.getName());
          }
          field.set(null, stringList.toArray(new String[stringList.size()]));
        }
      }
    } catch (IllegalAccessException e) {
      log.error(e);
    }
  }

  public ResourceUtilities(String qualifiedName, Locale locale) {
    resourceBundle = ResourceBundle.getBundle(qualifiedName, locale);
  }

  public static ResourceBundle getResourceBundle() {
    return resourceBundle;
  }

  public static String getString(String resource) {
    return resourceBundle.getString(resource);
  }

  /**
   * Retrieves a resource from the current classpath.
   * @param resourceName the name of the resource
   * @return the resource url
   */
  public static URL getResource(final String resourceName) {
    final String methodName = "ResourceUtils.getResource";
    checkEmpty(methodName, "resourceName", resourceName);
    final ClassLoader loader = ReflectUtilities.getClassLoader();
    return loader != null ? loader.getResource(resourceName) : null;
  }

  /**
   * Indicates whether or not the given <code>url</code> is a file.
   * @param url the URL to check
   * @return true/false
   */
  public static boolean isFile(final URL url) {
    return url != null ? new File(url.getFile()).isFile() : false;
  }

  /**
   * Checks if the argument is null or an empty String throws an
   * IllegalArgumentException if it is, does nothing if not.
   * @param methodExecuteName the name of the method we are currently executing
   * @param argumentName the name of the argument we are checking for null
   * @param argument the argument we are checking
   */
  private static void checkEmpty(final String methodExecuteName,
      final String argumentName, final String argument) {
    final String methodName = "ExceptionUtils.checkEmpty";
    if (StringUtils.isEmpty(methodExecuteName)) {
      throw new IllegalArgumentException(methodName
          + " - methodExecuteName can not be null or an empty String");
    }
    if (StringUtils.isEmpty(argumentName)) {
      throw new IllegalArgumentException(methodName
          + " - argumentName can not be null or an empty String");
    }

    // this is what the method is actually for
    if (StringUtils.isEmpty(argument)) {
      throw new IllegalArgumentException("methodName: " + methodExecuteName
          + " - '" + argumentName + "' can not be null or an empty String");
    }
  }

}