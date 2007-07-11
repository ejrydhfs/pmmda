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

package ch.bbv.dynamicproperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectImpl;
import ch.bbv.dynamicproperties.core.DynamicProperty;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyType;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.core.SimpleType;
import ch.bbv.dynamicproperties.pmmda.Visitor;
import ch.bbv.dynamicproperties.visualization.editor.BooleanEditor;
import ch.bbv.dynamicproperties.visualization.editor.DateEditor;
import ch.bbv.dynamicproperties.visualization.editor.DoubleEditor;
import ch.bbv.dynamicproperties.visualization.editor.IntegerEditor;
import ch.bbv.dynamicproperties.visualization.editor.StringEditor;
import ch.bbv.dynamicproperties.visualization.viewer.BooleanViewer;
import ch.bbv.dynamicproperties.visualization.viewer.DateViewer;
import ch.bbv.dynamicproperties.visualization.viewer.StringViewer;

public class PropertyInstanceManager extends DataObjectImpl {

    private Map<Class<? extends DynamicProperty>, PropertiesOfAClass> lookupTable;

    private static PropertyInstanceManager singletonInstance;

    private List<PropertyType> propertyTypes;

    private List<Property> properties;

    private static Hashtable<Class<?>, PropertyType> systemTypes;

    /**
     * Initialises a static hashtable containing a set of system types
     */
    static {
        systemTypes = new Hashtable<Class<?>, PropertyType>();
        systemTypes.put(Integer.class, new SimpleType("int", Integer.class, StringViewer.class, IntegerEditor.class));
        systemTypes.put(Double.class, new SimpleType("double", Double.class, StringViewer.class, DoubleEditor.class));
        systemTypes.put(Boolean.class, new SimpleType("boolean", Boolean.class, BooleanViewer.class,
                BooleanEditor.class));
        systemTypes.put(String.class, new SimpleType("string", String.class, StringViewer.class, StringEditor.class));
        systemTypes.put(Date.class, new SimpleType("date", Date.class, DateViewer.class, DateEditor.class));
    }

    /**
     * Private constructor to avoid multiple instances
     * 
     */
    protected PropertyInstanceManager() {
        lookupTable = new Hashtable<Class<? extends DynamicProperty>, PropertiesOfAClass>();
        propertyTypes = new ArrayList<PropertyType>();
        properties = new ArrayList<Property>();
    }

    /**
     * Singleton Pattern
     * 
     * @return returns the single instance of the DataObjectManager
     */
    public static PropertyInstanceManager instance() {
        if (singletonInstance == null) {
            singletonInstance = new PropertyInstanceManager();
        }
        return singletonInstance;
    }

    /**
     * Adds a property to the map.
     * 
     * @param dataObject
     * @param property
     */
    public void addProperty(Class<? extends DynamicProperty> dataObject, Property property) {
        if (!lookupTable.containsKey(dataObject)) {
            lookupTable.put(dataObject, new PropertiesOfAClass());
        }
        lookupTable.get(dataObject).addProperty(property);
    }

    /**
     * Adds a list of properties to the map.
     * 
     * @param dataObject
     * @param properties
     */
    public void addProperty(Class<DynamicProperty> dataObject, List<Property> properties) {
        for (Property property : properties) {
            addProperty(dataObject, property);
        }
    }

    /**
     * Returns a list of all properties of a data object
     * 
     * @param dataObject
     * @return list of all properties of a data object
     */
    public List<Property> getProperties(Class<? extends DynamicProperty> dataObject) {

        PropertiesOfAClass result = lookupTable.get(dataObject);
        if (result == null) {
            result = new PropertiesOfAClass();
            lookupTable.put(dataObject, result);
        }
        return result.getProperties();
    }

    /**
     * Returns a list of new instantiated
     * 
     * @param dataObject
     * @return
     */
    public List<PropertyValue> createProperties(Class<? extends DataObject> dataObject) {
        List<PropertyValue> values = new ArrayList<PropertyValue>();
        PropertiesOfAClass properties = lookupTable.get(dataObject);
        if (properties != null) {
            for (Property property : properties.getProperties()) {
                values.add(property.createInstance());
            }
        }
        return values;
    }

    public List<Class<? extends DynamicProperty>> getDataObjects() {
        return new ArrayList<Class<? extends DynamicProperty>>(lookupTable.keySet());
    }

    public void addDataObject(Class<? extends DynamicProperty> dynamicProperty) {
        if (!lookupTable.containsKey(dynamicProperty)) {
            lookupTable.put(dynamicProperty, new PropertiesOfAClass());
        }
    }

    /**
     * Removes a property from the dataobject class
     * 
     * @param clazz
     * @param property
     * @return
     */
    public boolean removeProperty(Class<?> clazz, Property property) {
        return lookupTable.get(clazz).getProperties().remove(property);
    }

    /*
     * Sets the singleton instance
     */
    public static void setSingletonInstance(PropertyInstanceManager propertyInstanceManager) {
        if (propertyInstanceManager != null) {
            singletonInstance = propertyInstanceManager;
        }
    }

    @Override
    public void setModified(boolean modified) {
        super.setModified(modified);
        if (modified) {
            for (PropertiesOfAClass elem : lookupTable.values()) {
                elem.setModified(true);
            }
        }
    }

    /**
     * Adds a property type
     * 
     * @param type
     *            the type to add
     * @return if the operation was successful
     */
    public boolean addType(PropertyType type) {
        return propertyTypes.add(type);
    }

    public boolean removeType(PropertyType type) {
        if (isTypeUsed(type)) {
            return false;
        }
        return propertyTypes.remove(type);
    }

    public boolean isTypeUsed(PropertyType type) {
        for (PropertiesOfAClass clazz : lookupTable.values()) {
            for (Property property : clazz.getProperties()) {
                if (property.getType().equals(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the attribute type with the given name
     * 
     * @param name
     *            the name of the attribute type
     * @return the attribute type
     */
    public PropertyType getPropertyType(String name) {
        for (PropertyType type : propertyTypes) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public List<PropertyType> getTypes() {
        return propertyTypes;
    }

    /**
     * Returns the attribute type of the corresponding system type
     * 
     * @param clazz
     *            the system type
     * @return the attribute type
     */
    public static PropertyType getType(Class<?> systemType) {
        return systemTypes.get(systemType);
    }

    /**
     * Returns the list of all system attribute types
     * 
     * @return the list of all system attribute types
     */
    public List<PropertyType> getSystemPropertyTypes() {
        return new ArrayList<PropertyType>(systemTypes.values());
    }

    public Property getProperty(String name) {
        for (Property property : properties) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    /**
     * 
     * @param visitor
     */
    public void accept(Visitor visitor) {
        assert (visitor != null);
        visitor.setDepth(visitor.getDepth() + 1);
        visitor.visit(this);
        traverse(visitor);
        visitor.setDepth(visitor.getDepth() - 1);
    }

    private void traverse(Visitor visitor) {
        for (Property property : properties) {
            property.accept(visitor);
        }

        for (PropertyType type : propertyTypes) {
            type.accept(visitor);
        }

        for (PropertiesOfAClass property : lookupTable.values()) {
            property.accept(visitor);
        }
    }
}
