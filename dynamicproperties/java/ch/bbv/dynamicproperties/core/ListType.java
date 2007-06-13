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

import ch.bbv.dynamicproperties.pmmda.Visitor;
import ch.bbv.dynamicproperties.visualization.IEditor;
import ch.bbv.dynamicproperties.visualization.IViewer;

/**
 * @author UeliKurmann
 */
public class ListType extends PropertyType {

    private PropertyType type;

    /**
     * Constructor
     * 
     * @param name
     *            the name of the list type
     * @param type
     *            the type of the list elements
     * @param viewer
     *            the list viewer
     * @param editor
     *            the list editor
     */
    public ListType(String name, PropertyType type, Class<? extends IViewer> viewer, Class<? extends IEditor> editor) {
        super(name, viewer, editor);
        setElementType(type);
    }

    public ListType(String name, PropertyType type) {
        this(name, type, null, null);
    }

    /**
     * Default Constructor used by Hibernate
     */
    public ListType() {
        super();
    }

    /**
     * Returns the attribute type of the elements
     * 
     * @return the attribute type of the elements
     */
    public PropertyType getElementType() {
        return this.type;
    }

    /**
     * Sets the attribute type of the elements
     * 
     * @param type
     */
    public void setElementType(PropertyType type) {
        this.type = type;
    }

    public PropertyValue createPropertyValue(Property attribute) {
        return new ListValue(attribute);
    }

    @Override
    public Property createProperty(String name, String format, Serializable defaultValue) {
        return new Property(name, this, format, defaultValue);
    }

    @Override
    protected void traverse(Visitor visitor) {
        super.traverse(visitor);
        type.accept(visitor);
    }
}