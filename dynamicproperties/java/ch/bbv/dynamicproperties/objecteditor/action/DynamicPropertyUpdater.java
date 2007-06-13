/*********************************************************************** 
 * Copyright 2007 BBV Software Services AG (http://www.bbv.ch)
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 * 
 * $Id$
 * 
 * History:
 * 2007/06/06      Ueli Kurmann     Initial Release
 ***********************************************************************/

package ch.bbv.dynamicproperties.objecteditor.action;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.DataObject;
import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.DynamicProperty;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyValue;

public class DynamicPropertyUpdater {

    public static void update(DynamicProperty dynamicPropertyClass, PropertyInstanceManager manager) {

        boolean modified = false;

        List<Property> properties = manager.getProperties(dynamicPropertyClass.getClass());

        // delete propertyies
        List<PropertyValue> toDelete = new ArrayList<PropertyValue>();
        for (PropertyValue value : dynamicPropertyClass.getProperties()) {
            if (!properties.contains(value.getProperty())) {
                toDelete.add(value);
                modified = true;
            }
        }

        dynamicPropertyClass.getProperties().removeAll(toDelete);

        // add properties
        for (Property property : properties) {
            if (!contains(property, dynamicPropertyClass.getProperties())) {
                dynamicPropertyClass.getProperties().add(property.createInstance());
                modified = true;
            }
        }

        ((DataObject) dynamicPropertyClass).setModified(modified);
    }

    public static boolean contains(Property property, List<PropertyValue> propertyValues) {
        for (PropertyValue value : propertyValues) {
            if (value.getProperty() == property) {
                return true;
            }
        }
        return false;
    }
}
