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
import ch.bbv.dynamicproperties.visualization.IEditor;
import ch.bbv.dynamicproperties.visualization.IViewer;

/**
 * @author UeliKurmann
 */
public abstract class PropertyValue extends DataObjectImpl implements Serializable {

    private Property property;

    /**
     * Default Constructor used by Hibernate
     * 
     */
    public PropertyValue() {
        super();
    }

    /**
     * Returns the attribute
     * 
     * @return the attribute
     */
    public Property getProperty() {
        return property;
    }

    /**
     * Sets the attribute
     * 
     * @param attribute
     *            the attribute
     */
    public void setProperty(Property attribute) {
        this.property = attribute;
    }

    /**
     * Returns the name of the attribute
     * 
     * @return the name of the attribute
     */
    public String getName() {
        return property.getName();
    }

    public String getName(String code) {
        return property.getName(code);
    }

    /**
     * Returns the viewer of the attribute
     * 
     * @return the viewer of the attribute
     */
    public IViewer getViewer() {
        return getProperty().getType().getViewer(this);
    }

    /**
     * Returns the edtior of the attribute
     * 
     * @return the editor of the attribute
     */
    public IEditor getEditor() {

        return getProperty().getType().getEditor(this);
    }

    /**
     * Returns the value
     * 
     * @return the value
     */
    public abstract Serializable getValue();

    /**
     * Sets the value
     * 
     * @param value
     *            the value
     */
    public abstract void setValue(Serializable value);

    public void accept(Visitor visitor) {
        assert (visitor != null);
        visitor.setDepth(visitor.getDepth() + 1);
        visitor.visit(this);
        traverse(visitor);
        visitor.setDepth(visitor.getDepth() - 1);
    }

    protected void traverse(Visitor visitor) {
        property.accept(visitor);
    }

}
