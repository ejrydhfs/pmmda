/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.utilities;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author AdrianBachofen Implements a simple handler for dealing with resources
 *         and localization
 */
public class ResourceUtility {

  public static ResourceUtility resourceUtility;

  public static ResourceBundle resourceBundle;

  public ResourceUtility(String qualifiedName, Locale locale) {
    resourceUtility = this;
    resourceBundle = ResourceBundle.getBundle(qualifiedName, locale);
  }

  public static ResourceBundle getResourceBundle() {
    return resourceBundle;
  }

  public static String getString(String resource) {
    return resourceBundle.getString(resource);
  }
}