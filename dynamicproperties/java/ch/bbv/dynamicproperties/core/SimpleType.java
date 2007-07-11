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

import ch.bbv.dynamicproperties.visualization.IEditor;
import ch.bbv.dynamicproperties.visualization.IViewer;

public class SimpleType extends PropertyType {

    private Class<?> type;

    /**
     * Constructor
     * 
     * @param name
     *            the name of the simple type
     * @param type
     *            the system type
     * @param viewer
     *            the viewer
     * @param editor
     *            the editor
     */
    public SimpleType(String name, Class<? extends Object> type, Class<? extends IViewer> viewer,
            Class<? extends IEditor> editor) {
        super(name, viewer, editor);
        setType(type);
    }
    
    /**
     * Default Constructor
     * 
     */
    public SimpleType() {
        super();
    }

    /**
     * Sets the system type
     * 
     * @param type
     *            the system type
     */
    public void setType(Class<?> type) {
        this.type = type;
    }

    /**
     * Returns the system type
     * 
     * @return the system type
     */
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public PropertyValue createPropertyValue(Property property) {
        SimpleValue value = new SimpleValue(property);
        return value;
    }

   
    
    
}
