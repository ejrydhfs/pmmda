/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.persistence;

import ch.bbv.mda.*;

public class ExternalTransformer {
  /**
   * Encoding rule of the XML file containing the XMI data.
   */
  public static final String XML_ENCODING = "UTF-8";

  /**
   * Flags all system packages found in the model and updates all type names in
   * the properties. A system package name is identified through a regular
   * expresssion. See the Java package naming conventions for a detailed
   * discussion.
   * @param model model to post process
   * @pre model != null
   */
  protected void postProcessModel(MetaModel model) {
    assert model != null;
    for (MetaPackage metaPackage : model.getPackages()) {
      if (isSystemPackage(metaPackage)) {
        metaPackage.setSystem(true);
        for (MetaClass clazz : metaPackage.getClasses()) {
          clazz.setSystem(true);
          for (MetaProperty property : clazz.getProperties()) {
            property.retrieveClassifier();
          }
          for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
            property.retrieveClassifier();
          }
        }
      }
    }
  }

  /**
   * Returns true if the package is identified as a system package.
   * @param packages package to check
   * @return true if the package belongs to the Java system otherwise false
   * @pre packages != null
   */
  protected boolean isSystemPackage(MetaPackage packages) {
    assert packages != null;
    String name = packages.getName();
    return ((name != null) && (name.startsWith("java.")
        || name.startsWith("javax.") || name.equals("java") || name
        .equals("javax")));
  }

  /**
   * Return the boolean value of the string as used in NSUML models.
   * @param value string value to convert
   * @return the boolean value of the string representation
   */
  protected boolean booleanValue(String value) {
    return (value != null && value.equalsIgnoreCase("true"));
  }
}
