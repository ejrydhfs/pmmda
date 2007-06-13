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

import java.util.Date;
import java.util.List;

import ch.bbv.dynamicproperties.visualization.IEditor;
import ch.bbv.dynamicproperties.visualization.IViewer;

public abstract class EnumerationType extends PropertyType {

    /**
     * Constructor
     * 
     * @param name
     *            the name
     * @param viewer
     *            the viewer
     * @param editor
     *            the editor
     */
    public EnumerationType(String name, Class<? extends IViewer> viewer, Class<? extends IEditor> editor) {
        super(name, viewer, editor);
    }

    public EnumerationType() {
        super();
    }

    /**
     * Adds a key/enumeration pair to the collection. If the key already exists
     * an exception is thrown.
     * 
     * @param key
     *            the key
     * @param enumeration
     *            the enumeration
     */
    public abstract void addEnum(Integer key, EnumerationElement enumeration);

    /**
     * Adds an enumeration to the collection. The key becomes the maximal key +
     * 1 of the existing enumerations. If there is no maximal key, 1 is taken.
     * 
     * @param enumeration
     *            the enumeration
     */
    public abstract void addEnum(EnumerationElement enumeration);

    /**
     * Adds an enumeration element to the type
     * 
     * @param shortDescription
     * @param description
     */
    public abstract void addEnum(String shortDescription, String description);

    /**
     * Adds an enumeration element to the type
     * 
     * @param code
     * @param sortOrder
     * @param shortDescription
     * @param description
     * @param active
     * @param fromDate
     * @param toDate
     */
    public abstract void addEnum(int code, int sortOrder, String shortDescription, String description, boolean active,
            Date fromDate, Date toDate);

    /**
     * Returns the enumeration with the specified key
     * 
     * @param key
     *            the key of the enumeration
     * @return the enumeration
     */
    public abstract EnumerationElement getEnum(Integer key);

    /**
     * Returns the key of a given enumeration.
     * 
     * @param enumeration
     *            the enumeration
     * @return the key
     */
    public abstract Integer getKey(String enumeration);

    /**
     * Returns a list of all enumerations
     * 
     * @return list of all enumerations
     */
    public abstract List<EnumerationElement> getEnumerations();

    @Override
    public PropertyValue createPropertyValue(Property attribute) {
        return new EnumerationValue(attribute);
    }
}
