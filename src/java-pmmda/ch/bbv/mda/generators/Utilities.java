/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.generators;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaProperty;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public class Utilities {
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
