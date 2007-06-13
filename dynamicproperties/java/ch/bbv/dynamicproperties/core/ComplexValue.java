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

package ch.bbv.dynamicproperties.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import ch.bbv.dynamicproperties.pmmda.Visitor;

/**
 * @author UeliKurmann
 */
public class ComplexValue extends PropertyValue {

    private Map<Property, PropertyValue> properties;

    /**
     * Constructor
     * 
     * @param property
     *            the property
     */
    public ComplexValue(Property property) {
        setProperty(property);
        properties = new Hashtable<Property, PropertyValue>();
    }

    /**
     * Default Constructor used by Hibernate
     * 
     */
    public ComplexValue() {
        super();
    }

    /**
     * Returns the attribute value
     * 
     * @param attribute
     *            the attribute
     * @return the attribute value
     */
    public PropertyValue getPropertyValue(Property attribute) {
        return properties.get(attribute);
    }

    /**
     * Adds an attribute value
     * 
     * @param value
     */
    public void addPropertyValue(PropertyValue value) {
        properties.put(value.getProperty(), value);
    }

    /**
     * Returns the list of all attribute values
     * 
     * @return the attributes
     */
    public List<PropertyValue> getProperties() {
        return new ArrayList<PropertyValue>(properties.values());
    }

    /**
     * Returns the attribute with the given name
     * 
     * @param attributeName
     *            the attribute name
     * @return the attribute value with the given name
     */
    public PropertyValue getPropertyValue(String attributeName) {
        for (Property attribute : properties.keySet()) {
            if (attribute.getName().equals(attributeName)) {
                return getPropertyValue(attribute);
            }
        }
        return null;
    }

    /**
     * Tests if an attribute with the given name already exists in this type
     * 
     * @param name
     *            the name of the attribute
     * @return boolean
     */
    public boolean contains(String name) {
        for (PropertyValue attributeValue : properties.values()) {
            if (attributeValue.getProperty().getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the value of a property
     * 
     * @param propertyName
     * @param object
     */
    public void setValue(String propertyName, Serializable object) {
        if (this.getPropertyValue(propertyName) != null) {
            Serializable oldValue = this.getPropertyValue(propertyName);
            this.getPropertyValue(propertyName).setValue(object);
            propertyChangeSupport.firePropertyChange(propertyName, oldValue, object);
        } else {
            throw new RuntimeException("Property not found!");
        }
    }

    public void setValue(Serializable value) {
        ComplexValue sourceValue = (ComplexValue) value;
        if (sourceValue.getProperty().getType() == sourceValue.getProperty().getType()) {
            for (PropertyValue property : sourceValue.getProperties()) {
                this.getPropertyValue(property.getName()).setValue(property);
            }
            propertyChangeSupport.firePropertyChange(getName(), null, value);
        } else {
            throw new RuntimeException("Cast not allowed");
        }
    }

    public Serializable getValue() {
        return this;
    }

    @Override
    protected void traverse(Visitor visitor) {
        super.traverse(visitor);
        for (PropertyValue value : properties.values()) {
            value.accept(visitor);
        }
    }
}
