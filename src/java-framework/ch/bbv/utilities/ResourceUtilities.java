/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.utilities;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

/**
 * @author AdrianBachofen Implements a simple handler for dealing with resources
 *         and localization
 */
public class ResourceUtilities {
  public static ResourceBundle resourceBundle;

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