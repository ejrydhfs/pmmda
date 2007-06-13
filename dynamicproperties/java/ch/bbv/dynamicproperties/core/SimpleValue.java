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

/**
 * @author Ueli Kurmann
 */
public class SimpleValue extends PropertyValue {

    private Serializable value;

    /**
     * Constructor
     * 
     * @param property
     *            the attribute
     */
    public SimpleValue(Property property) {
        this(property, property.getDefaultValue());
    }

    /**
     * Constructor
     * 
     * @param attribute
     *            the attribute
     * @param value
     *            the value of the attribute
     */
    public SimpleValue(Property attribute, Serializable value) {
        setProperty(attribute);
        checkType(value);
        this.value = value;
    }

    /**
     * Default Constructor used by Hibernate
     * 
     */
    public SimpleValue() {
        super();
    }

    /**
     * Checks if the value is compatible with the data type
     * 
     * @param value
     *            the value object to validate
     * @return true if the type of value is compatible
     */
    private boolean checkType(Object value) {
        if (value == null || ((SimpleType) getProperty().getType()).getType().isInstance(value)) {
            return true;
        } else {
            throw new RuntimeException("Type incompatiblity: Expects"
                    + ((SimpleType) getProperty().getType()).getType() + " not " + value.getClass());
        }
    }

    /**
     * Sets the simple value
     * 
     * @param value
     *            the value to set
     */
    public void setValue(Serializable value) {
        checkType(value);
        Serializable oldValue = this.value;
        this.value = value;
        propertyChangeSupport.firePropertyChange(getName(), oldValue, value);
    }

    /**
     * Returns the simple value
     * 
     * @return the value
     */
    public Serializable getValue() {
        return this.value;
    }
}