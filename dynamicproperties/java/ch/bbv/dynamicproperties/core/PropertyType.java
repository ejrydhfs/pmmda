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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ch.bbv.dog.DataObjectImpl;
import ch.bbv.dynamicproperties.pmmda.Visitor;
import ch.bbv.dynamicproperties.visualization.IEditor;
import ch.bbv.dynamicproperties.visualization.IViewer;

/**
 * @author UeliKurmann
 */
public abstract class PropertyType extends DataObjectImpl implements Serializable {

    private String name;

    private Serializable defaultValue;

    private Class<? extends IViewer> viewer;

    private Class<? extends IEditor> editor;

    /**
     * Constructor
     * 
     * @param name
     * @param viewer
     * @param editor
     */
    public PropertyType(String name, Class<? extends IViewer> viewer, Class<? extends IEditor> editor) {
        setName(name);
        setViewer(viewer);
        setEditor(editor);
    }

    /**
     * Default Constructor used by Hibernate
     * 
     */
    public PropertyType() {

    }

    /**
     * Sets the name of the attribute type
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the attribute type
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the editor of the attribute type
     * 
     * @param editor
     *            the editor to set
     * 
     */
    public void setEditor(Class<? extends IEditor> editor) {
        this.editor = editor;
    }

    /**
     * Sets the viewer of the attribute type
     * 
     * @param viewer
     *            the viewer to set
     * 
     */
    public void setViewer(Class<? extends IViewer> viewer) {
        this.viewer = viewer;
    }

    /**
     * Returns the viewer of the attribute type
     * 
     * @param value
     *            the attribute value to be used by the viewer
     * @return the viewer initialised with the given attribute value
     */
    public IViewer getViewer(PropertyValue value) {
        return (IViewer) createEditorViewInstance(viewer, value);
    }

    /**
     * Returns the editor of the attribute type
     * 
     * @param value
     *            the attribute value to be used by the editor
     * @return the editor initiailised with the given attribute value
     */
    public IEditor getEditor(PropertyValue value) {
        return getEditor(value, editor);
    }

    /**
     * Returns the editor of the attribute type
     * 
     * @param value
     *            the attribute value to be used by the editor
     * @param editor
     *            the editor
     * @return the editor initialised with the given attribute value
     */
    public IEditor getEditor(PropertyValue value, Class<? extends IEditor> editor) {
        return (IEditor) createEditorViewInstance(editor, value);
    }

    /**
     * Factory method to create an attribute of this type
     * 
     * @param name
     *            the name of the attribute
     * @param format
     *            the format of this attribute
     * @param defaultValue
     *            the default value of this attribute
     * @return the created attribute of this type
     */
    public Property createProperty(String name, String format, Serializable defaultValue) {
        return new Property(name, this, format, defaultValue);
    }
    
    /**
     * Factory method to create an attribute of this type
     * @param name the name of the property
     * @return the property
     */
    public Property createProperty(String name){
        return new Property(name, this, null, null);
    }

    /**
     * Factory to create an instance of a viewer or editor class
     * 
     * @param clazz
     *            the viewer or editor class
     * @param value
     *            the attribute value
     * @return instance of a viewer or editor
     */
    private Object createEditorViewInstance(Class<? extends Object> clazz, PropertyValue value) {
        Object object = null;
        try {
            Constructor constructor = clazz.getConstructor(new Class[] { PropertyValue.class });
            object = constructor.newInstance(new Object[] { value });
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * Sets the types default value
     * 
     * @param defaultValue
     */
    public void setDefaultValue(Serializable defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the default value of the type
     * 
     * @return the default value of the type
     */
    public Serializable getDefaultValue() {
        return defaultValue;
    }

    /**
     * Factory to create a corresponding value instance
     * 
     * @param attribute
     *            the attribute
     * @return a new value instance
     */
    public abstract PropertyValue createPropertyValue(Property attribute);
    

    @Override
    public String toString() {
        return getName();
    }

    public void accept(Visitor visitor) {
        assert (visitor != null);
        visitor.setDepth(visitor.getDepth() + 1);
        visitor.visit(this);
        traverse(visitor);
        visitor.setDepth(visitor.getDepth() - 1);
    }

    protected void traverse(Visitor visitor) {
        //
    }
}
