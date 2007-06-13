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

import ch.bbv.dog.DataObjectImpl;
import ch.bbv.dynamicproperties.pmmda.Visitor;

/**
 * @author Ueli Kurmann
 */
public class Property extends DataObjectImpl implements Serializable {

    private String name;

    private Serializable defaultValue;

    private String format;

    private PropertyType type;

    private Multilingualism translation;

    private String description;

    /**
     * Constructor
     * 
     * @param name
     *            the name of the property
     * @param type
     *            the type of the property
     * @param format
     *            format information
     * @param defaultValue
     *            the default value of the property. can be null.
     * 
     * @pre name != null && type != null
     * 
     */
    public Property(String name, PropertyType type, String format, Serializable defaultValue) {
        assert (name != null && type != null);

        this.name = name;
        this.defaultValue = defaultValue;
        this.format = format;
        this.type = type;

        this.translation = new Multilingualism();
    }

    /**
     * Creates a new Property.
     * 
     * @param name
     *            the name of the proeprty
     * @param type
     *            the type of the property
     */
    public Property(String name, PropertyType type) {
        this(name, type, "", type.getDefaultValue());
    }

    /**
     * Default Constructor used by Hibernate
     * 
     */
    public Property() {
    }

    /**
     * Factory method to create a new value instance of this property
     * 
     * @return a new value instance of this property
     */
    public PropertyValue createInstance() {
        return type.createPropertyValue(this);
    }

    /**
     * Returns the default value of the property
     * 
     * @return the default value of the property
     */
    public Serializable getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the property's default value
     * 
     * @param defaultValue
     *            the default value to set
     */
    public void setDefaultValue(Serializable defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the format string of the property
     * 
     * @return
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format string of the property
     * 
     * @param format
     *            the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Returns the property's name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name of the property. If available, the translated name is
     * returned. if no translation is available, the default name is returned.
     * 
     * @param code
     *            the language code
     * @return the translated name
     */
    public String getName(String code) {
        if (translation != null) {
            return translation.getTranslation(code) != null ? translation.getTranslation(code) : name;
        }
        return name;
    }

    /**
     * Sets the name
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the property type
     * 
     * @return the type
     */
    public PropertyType getType() {
        return type;
    }

    /**
     * Sets the type
     * 
     * @param type
     *            the type to set
     */
    public void setType(PropertyType type) {
        this.type = type;
    }

    /**
     * Returns the multilingualism object
     * 
     * @return the multilingualism object
     */
    public Multilingualism getTranslation() {
        return translation;
    }

    /**
     * Sets the translation
     * 
     * @param translation
     *            the translation object
     */
    public void setTranslation(Multilingualism translation) {
        this.translation = translation;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
        // TODO association? type.accept(visitor);
        if (translation != null) {
            translation.accept(visitor);
        }
    }
}
