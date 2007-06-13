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
 * 2007/06/06       Ueli Kurmann     Initial Release
 ***********************************************************************/

package ch.bbv.dynamicproperties.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.bbv.dynamicproperties.pmmda.Visitor;
import ch.bbv.dynamicproperties.visualization.IEditor;
import ch.bbv.dynamicproperties.visualization.IViewer;

/**
 * This class defines the layout of a complex datatype. A complex datatype
 * consists of other attributes whereas each attribute may have different types.
 * 
 * @author Ueli Kurmann
 * @version 0.01
 */

public class ComplexType extends PropertyType {

    private List<Property> properties;

    /**
     * Constructor
     * 
     * @param name
     *            the name of the complex type
     * @param viewer
     *            the viewer
     * @param editor
     *            the editor
     */
    public ComplexType(String name, Class<? extends IViewer> viewer, Class<? extends IEditor> editor) {
        super(name, viewer, editor);
        properties = new ArrayList<Property>();
    }

    public ComplexType(String name) {
        this(name, null, null);
    }

    /**
     * Default Constructor used by Hibernate
     * 
     */
    public ComplexType() {
        super();
        properties = new ArrayList<Property>();
    }

    /**
     * Returns the name of this complex type
     * 
     * @param propertyName
     *            the name of this complex type
     * @return the name of this complex type
     */
    public Property getPropertyType(String propertyName) {

        for (Property property : properties) {
            if (property.getName().equals(propertyName)) {
                return property;
            }
        }
        return null;
    }

    /**
     * Adds an attribute to this type
     * 
     * @param property
     *            the attribute to be added
     */
    public void addProperty(Property property) {
        properties.add(property);
    }

    /**
     * Returns the list of all attributes of this complex type
     * 
     * @return the list of all attributes of this complex type
     */
    public List<Property> getProperties() {
        return new ArrayList<Property>(properties);
    }

    /**
     * Returns the list of all attribute names of this complex type
     * 
     * @return the list of all attribute names of this complex type
     */
    public List<String> getPropertyNames() {
        List<String> names = new ArrayList<String>();
        for (Property property : properties) {
            names.add(property.getName());
        }
        return names;
    }

    /**
     * Tests if the attribute with the given name exists in the complex type
     * definition.
     * 
     * @param name
     *            the name of the attribute
     * @return boolean
     */
    public boolean contains(String name) {
        for (Property attribute : properties) {
            if (attribute.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public ComplexValue createPropertyValue(Property attribute) {
        ComplexValue value = new ComplexValue(attribute);
        for (Property childProperty : properties) {
            value.addPropertyValue(childProperty.getType().createPropertyValue(childProperty));
        }
        return value;
    }

    @Override
    public Property createProperty(String name, String format, Serializable defaultValue) {
        return new Property(name, this, format, defaultValue);
    }

    @Override
    protected void traverse(Visitor visitor) {
        super.traverse(visitor);
        for (Property property : properties) {
            property.accept(visitor);
        }
    }
}